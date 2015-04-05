import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/*
 * Scraper tests.
 *
 * @author dead10ck, @date 4/2/15 11:58 AM
 */
public class ShodorScraperTest {
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(ShodorScraperTest.class);
    private static ShodorScraper scraper = new ShodorScraper();
    private static Document bySubjectDoc;
    private static Document byAudienceDoc;

    @BeforeClass
    public static void setUp() throws IOException {
        ClassLoader loader = ShodorScraperTest.class.getClassLoader();
        File bySubjectFile = new File(loader.getResource("bySubject.htm").getFile());
        File byAudienceFile = new File(loader.getResource("byAudience.htm").getFile());

        bySubjectDoc = Jsoup.parse(bySubjectFile, "UTF-8", "http://www.shodor.org/");
        assertNotNull(bySubjectDoc);
        assertTrue(0 < bySubjectDoc.childNodes().size());

        byAudienceDoc = Jsoup.parse(byAudienceFile, "UTF-8", "http://www.shodor.org/");
        assertNotNull(byAudienceDoc);
        assertTrue(0 < byAudienceDoc.childNodes().size());
    }

    public Element getRootElt() {
        Element rootElt = ShodorScraper.rootElement(bySubjectDoc);
        assertNotNull(rootElt);
        return rootElt;
    }

    @Test
    public void testRootElement() {
        getRootElt();
    }

    @Test
    public void testScrapeCategory() throws Exception {
        Element rootElt = getRootElt();

        Node catNode = rootElt.childNodes().get(0);
        assertNotNull(catNode);
        String category = scraper.scrapeCategory(catNode);
        assertEquals("algebra", category);

        catNode = rootElt.childNodes().get(1);
        assertNotNull(catNode);
        category = scraper.scrapeCategory(catNode);
        assertEquals("calculus", category);
    }

    @Test
    public void testScrapeRowTitle() {
        Element rootElt = getRootElt();
        Element algebraCategoryElt = rootElt.select("> div").first();
        //logger.info(algebraCategoryNode.outerHtml());
        Element firstAlgebraNode = algebraCategoryElt.select("> div:not(div.listingHeader)").first();
        //logger.info(firstAlgebraNode.outerHtml());
        SiteEntry.Builder builder = new SiteEntry.Builder();

        scraper.scrapeRowTitle(builder, firstAlgebraNode);
        assertEquals("3D Transmographer", builder.getTitle());
        assertEquals("http://www.shodor.org/interactivate/activities/3DTransmographer/",
                builder.getActivityUrl());
        assertEquals("http://www.shodor.org/media/N/j/Z/mNDk4YjUzYmRhMjhlODE2MDA0NzA2NjExNGIxNzQ.png",
                builder.getIconImageUrl());
    }

    @Test
    public void testScrapeRowContent() {
        Element rootElt = getRootElt();
        Element algebraCategoryElt = rootElt.select("> div").first();
        //logger.info(algebraCategoryNode.outerHtml());
        Element firstAlgebraNode = algebraCategoryElt.select("> div:not(div.listingHeader)").first();
        //logger.info(firstAlgebraNode.outerHtml());
        SiteEntry.Builder builder = new SiteEntry.Builder();

        scraper.scrapeRowContent(builder, firstAlgebraNode);
        String desc = "Build your own polygon and transform it in the Cartesian" +
            " coordinate system. Experiment with reflections across any line," +
            " revolving around any line (which yields a 3-D image), rotations" +
            " about any point, and translations in any direction.";
        assertEquals(desc, builder.getDescription());
    }

    @Test
    public void testScrape() throws Exception {
        Set<SiteEntry> sites = scraper.scrape(bySubjectDoc, byAudienceDoc);
        Iterator<SiteEntry> siteIter = sites.iterator();

        // find the first site
        SiteEntry site = null;
        while (siteIter.hasNext()) {
            site = siteIter.next();
            if (site.getTitle().equals("3D Transmographer")) {
                break;
            }
        }

        if (site == null || !site.getTitle().equals("3D Transmographer")) {
            fail();
        }

        logger.info(String.format("%s", site.getCategories()));

        Set<String> correctCategories = new HashSet<String>();
        correctCategories.add("calculus");
        correctCategories.add("graphs");
        correctCategories.add("geometry");
        correctCategories.add("algebra");

        assertEquals(correctCategories, site.getCategories());

        logger.info(String.format("%s", site.getTargetGrades()));
    }
}
