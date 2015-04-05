import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private static final HashMap<Integer, String> categories;

    static {
        categories = new HashMap<>();
        categories.put(0, "algebra");
        categories.put(1, "calculus");
        categories.put(2, "discrete");
        categories.put(3, "fractions");
        categories.put(4, "geometry");
        categories.put(5, "graphs");
        categories.put(6, "modeling");
        categories.put(7, "number and operations");
        categories.put(8, "probability");
        categories.put(9, "statistics");
        categories.put(10, "trigonometry");
        categories.put(11, "other");
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

    public Set<SiteEntry> scrape(Document doc) throws Exception {
        Set<SiteEntry> sites = new HashSet<>();
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

                SiteEntry siteEntry = builder.toSiteEntry();
                log.info(siteEntry.toString());
            }
        }

        return sites;
    }

    /**
     * Scrape the title out of the categoryNode.
     * @throws Exception
     */
    protected String scrapeCategory(Node categoryNode) throws Exception {
        String id = categoryNode.attr("id");
        Matcher m = categoryIdPattern.matcher(id);

        if (!m.matches()) {
            throw new Exception("node id doesn't match pattern");
        }

        String catNumStr = m.group(CATEGORY_ID_GROUP_NUM);
        Integer catNum = new Integer(catNumStr);

        String category = categories.get(catNum);

        if (category == null) {
            throw new Exception("unknown category number");
        }

        return category;
    }

    /**
     * Scrapes all data from the div.rowTitle element of the given
     * site node.
     */
    protected SiteEntry.Builder scrapeRowTitle(SiteEntry.Builder builder,
            Element siteElement) {
        //log.info(siteElt.outerHtml());
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
        //log.info(styleStr);

        Matcher m = relativeImagePathPattern.matcher(styleStr);
        m.find();
        String relPath = m.group(1);
        String imagePath = DOMAIN + relPath;
        builder.withIconImageUrl(imagePath);

        return builder;
    }
}
