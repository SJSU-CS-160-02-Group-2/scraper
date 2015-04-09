import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Scrapes data from a web site.
 *
 * @author dead10ck, @date 4/2/15 11:58 AM
 */
public class ShodorScraper extends BaseScraper {
    private static final Logger log = LoggerFactory.getLogger(ShodorScraper.class);
    private static final String DOMAIN = "http://www.shodor.org";
    private static final String BASE_URI =
        "http://www.shodor.org/interactivate/activities";
    private static final String ROOT_SELECTOR = "div#listing0";

    private static final int CATEGORY_ID_GROUP_NUM = 1;
    private static final Pattern categoryIdPattern = Pattern.compile("listing0_ctg(\\d)");
    private static final Pattern relativeImagePathPattern = Pattern.compile("background-image:url\\('(.+)'\\);");

    private static final HashMap<Integer, String> CATEGORIES;
    protected static final HashMap<Integer, Set<Integer>> GRADE_LEVELS;

    static {
        CATEGORIES = new HashMap<>();
        CATEGORIES.put(0, "algebra");
        CATEGORIES.put(1, "calculus");
        CATEGORIES.put(2, "discrete");
        CATEGORIES.put(3, "fractions");
        CATEGORIES.put(4, "geometry");
        CATEGORIES.put(5, "graphs");
        CATEGORIES.put(6, "modeling");
        CATEGORIES.put(7, "number and operations");
        CATEGORIES.put(8, "probability");
        CATEGORIES.put(9, "statistics");
        CATEGORIES.put(10, "trigonometry");
        CATEGORIES.put(11, "other");

        GRADE_LEVELS = new HashMap<>();

        HashSet<Integer> threeFive = new HashSet<>();
        threeFive.add(3);
        threeFive.add(4);
        threeFive.add(5);

        HashSet<Integer> sixEight = new HashSet<>();
        sixEight.add(6);
        sixEight.add(7);
        sixEight.add(8);

        HashSet<Integer> nineTwelve = new HashSet<>();
        nineTwelve.add(9);
        nineTwelve.add(10);
        nineTwelve.add(11);
        nineTwelve.add(12);

        HashSet<Integer> undergraduate = new HashSet<>();
        undergraduate.add(13);
        undergraduate.add(14);
        undergraduate.add(15);
        undergraduate.add(16);

        GRADE_LEVELS.put(0, threeFive);
        GRADE_LEVELS.put(1, sixEight);
        GRADE_LEVELS.put(2, nineTwelve);
        GRADE_LEVELS.put(3, undergraduate);
    }

    public ShodorScraper() {
        super(BASE_URI);
    }

    public enum Endpoint {
        BY_SUBJECT("/bySubject"),
        BY_AUDIENCE("/byAudience");

        private String endpointString;

        Endpoint(String str) {
            this.endpointString = str;
        }

        /**
         * @return the endpointString
         */
        public String toString() {
            return endpointString;
        }
    }

    public Document download(Endpoint ep) throws IOException {
        String url = baseUri + ep.toString();
        return download(url);
    }

    /**
     * @return the HTML Document from the "bySubject" endpoint
     */
    public Document bySubject() throws IOException {
        return download(Endpoint.BY_SUBJECT);
    }

    /**
     * @return the HTML Document from the "byAudience" endpoint
     */
    public Document byAudience() throws IOException {
        return download(Endpoint.BY_AUDIENCE);
    }

    protected static Element rootElement(Document doc) {
        Elements elts = doc.select(ROOT_SELECTOR);

        if (elts.size() != 1) {
            throw new RuntimeException(
                String.format("Selector found %d elements in root search",
                    elts.size()));
        }

        return elts.first();
    }

    public Set<SiteEntry> scrape(Document... docs) throws Exception {
        Set<SiteEntry> sites = new HashSet<>();
        Document bySubjectDoc = docs[0];
        Document byAudienceDoc = docs[1];

        scrapeBySubject(sites, bySubjectDoc);
        scrapeByAudience(sites, byAudienceDoc);

        return sites;
    }

    public void scrapeBySubject(Set<SiteEntry> sites, Document doc) {
        Element root = rootElement(doc);

        // each child of the root node is the category div
        for (Element categoryElement : root.select("> div")) {
            String category;

            try{
                category = scrapeCategory(categoryElement);
            } catch (Exception e) {
                // skip elements that don't match this pattern
                continue;
            }

            Elements categoryChildren = categoryElement.select("> div:not(div.listingHeader)");

            for (Element siteElement : categoryChildren) {
                SiteEntry.Builder builder = new SiteEntry.Builder();
                builder.withCategory(category);
                scrapeRowTitle(builder, siteElement);
                scrapeRowContent(builder, siteElement);
                SiteEntry siteEntry = builder.toSiteEntry();

                addSite(sites, siteEntry);
            }
        }
    }

    public void scrapeByAudience(Set<SiteEntry> sites, Document doc) {
        Element root = rootElement(doc);

        // each child of the root node is the category div
        for (Element categoryElement : root.select("> div")) {
            HashSet<Integer> grades = null;

            try{
                grades = scrapeGradeLevel(categoryElement);
            } catch (Exception e) {
                // skip elements that don't match this pattern
                continue;
            }

            Elements categoryChildren = categoryElement.select("> div:not(div.listingHeader)");

            for (Element siteElement : categoryChildren) {
                SiteEntry.Builder builder = new SiteEntry.Builder();
                builder.withTargetGrades((HashSet<Integer>) grades.clone());
                scrapeRowTitle(builder, siteElement);
                scrapeRowContent(builder, siteElement);
                SiteEntry siteEntry = builder.toSiteEntry();

                addSite(sites, siteEntry);
            }
        }
    }

    /**
     * Find the given site from the given set of sites.
     */
    public SiteEntry find(Set<SiteEntry> sites, SiteEntry site) {
        for (SiteEntry s : sites) {
            // equality is based on the activity URL
            if (s.equals(site)) {
                return s;
            }
        }

        return null;
    }

    public void addSite(Set<SiteEntry> sites, SiteEntry newEntry) {
        if (sites.contains(newEntry)) {
            SiteEntry site = find(sites, newEntry);
            addCategories(site, newEntry);
            addGrades(site, newEntry);
        } else {
            sites.add(newEntry);
        }
    }

    /**
     * Add the categories from the new entry to the
     */
    public void addCategories(SiteEntry oldEntry, SiteEntry newEntry) {
        Set<String> oldCategories = oldEntry.getCategories();
        Set<String> newCategories = newEntry.getCategories();

        if (oldCategories == null) {
            oldEntry.setCategories(newCategories);
            return;
        }

        if (newCategories != null) {
            oldCategories.addAll(newCategories);
        }
    }

    /**
     * Add the grades from the new entry to the
     */
    public void addGrades(SiteEntry oldEntry, SiteEntry newEntry) {
        Set<Integer> oldGrades = oldEntry.getTargetGrades();
        Set<Integer> newGrades = newEntry.getTargetGrades();

        if (oldGrades == null) {
            oldEntry.setTargetGrades(newGrades);
            return;
        }

        if (newGrades != null) {
            oldGrades.addAll(newGrades);
        }
    }

    protected int parseCategoryNumber(Node categoryNode) throws Exception {
        String id = categoryNode.attr("id");
        Matcher m = categoryIdPattern.matcher(id);

        if (!m.matches()) {
            throw new Exception("node id doesn't match pattern");
        }

        String catNumStr = m.group(CATEGORY_ID_GROUP_NUM);
        Integer catNum = new Integer(catNumStr);

        return catNum;
    }

    /**
     * Scrape the title out of the categoryNode.
     * @throws Exception
     */
    protected String scrapeCategory(Node categoryNode) throws Exception {
        Integer catNum = parseCategoryNumber(categoryNode);
        String category = CATEGORIES.get(catNum);

        if (category == null) {
            throw new Exception("unknown category number");
        }

        return category;
    }

    /**
     * Scrape the grade level out of a category node
     * @throws Exception
     */
    protected HashSet<Integer> scrapeGradeLevel(Node categoryNode) throws Exception {
        Integer catNum = parseCategoryNumber(categoryNode);
        HashSet<Integer> gradeLevel = (HashSet<Integer>) GRADE_LEVELS.get(catNum);

        if (gradeLevel == null) {
            throw new Exception("unknown category number");
        }

        return (HashSet<Integer>) gradeLevel.clone();
    }

    /**
     * Scrapes the title, activity URL, and image URL
     */
    protected SiteEntry.Builder scrapeRowTitle(SiteEntry.Builder builder,
            Element siteElement) {
        Element rowTitleElt = siteElement.select("div.rowTitle").first();

        // get the title
        Element anchorElt = rowTitleElt.select("> a").first();
        String title = anchorElt.text();
        builder.withTitle(title);

        // get the activity URL
        String activityUrl = anchorElt.attr("href");
        builder.withActivityUrl(activityUrl);

        // get the image URL
        Element imageDiv = rowTitleElt.select("> div").first();
        String styleStr = imageDiv.attr("style");

        Matcher m = relativeImagePathPattern.matcher(styleStr);
        m.find();
        String relPath = m.group(1);
        String imagePath = DOMAIN + relPath;
        builder.withIconImageUrl(imagePath);

        return builder;
    }

    /**
     * Scrape the description
     */
    protected SiteEntry.Builder scrapeRowContent(SiteEntry.Builder builder,
            Element siteElement) {
        Element descriptionDiv = siteElement.select("div.rowContent > div").first();
        String description = descriptionDiv.text();
        return builder.withDescription(description);
    }
}
