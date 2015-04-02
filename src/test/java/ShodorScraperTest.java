import java.io.IOException;

import org.jsoup.nodes.Document;
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
        bySubjectDoc = scraper.download(ShodorScraper.Endpoint.BY_SUBJECT);
        //logger.info(String.format("Document text:\n%s\n", bySubjectDoc.outerHtml()));
        assertNotNull(bySubjectDoc);
        assertTrue(0 < bySubjectDoc.childNodes().size());

        byAudienceDoc = scraper.download(ShodorScraper.Endpoint.BY_AUDIENCE);
        //logger.info(String.format("Document text:\n%s\n", byAudienceDoc.outerHtml()));
        assertNotNull(byAudienceDoc);
        assertTrue(0 < byAudienceDoc.childNodes().size());
    }

    @Test
    public void testDownloadWebDocument() throws IOException {

    }
}
