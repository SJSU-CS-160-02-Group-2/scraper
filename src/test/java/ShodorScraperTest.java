import java.io.IOException;

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
        bySubjectDoc = scraper.bySubject();
        //logger.info(String.format("Document text:\n%s\n", bySubjectDoc.outerHtml()));
        assertNotNull(bySubjectDoc);
        assertTrue(0 < bySubjectDoc.childNodes().size());

        byAudienceDoc = scraper.byAudience();
        //logger.info(String.format("Document text:\n%s\n", byAudienceDoc.outerHtml()));
        assertNotNull(byAudienceDoc);
        assertTrue(0 < byAudienceDoc.childNodes().size());
    }

    @Test
    public void testRootElement() {
        Element rootElt = ShodorScraper.rootElement(bySubjectDoc);
        assertNotNull(rootElt);
    }

    @Test
    public void testScrapeCategory() throws Exception {
        Element rootElt = ShodorScraper.rootElement(bySubjectDoc);
        assertNotNull(rootElt);

        Node catNode = rootElt.childNodes().get(0);
        assertNotNull(catNode);
        String category = scraper.scrapeCategory(catNode);
        assertEquals("algebra", category);

        catNode = rootElt.childNodes().get(1);
        assertNotNull(catNode);
        category = scraper.scrapeCategory(catNode);
        assertEquals("calculus", category);
    }
}
