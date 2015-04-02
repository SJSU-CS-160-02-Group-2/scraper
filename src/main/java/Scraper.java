import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Document;

/**
 * A web scraper. Downloads HTML documents and scrapes information from it.
 */
public interface Scraper {
    /**
     * Download the Document.
     *
     * @param url the URL to download
     */
    public Document download(String url) throws IOException;

    /**
     * Scrapes the site's info from the given Document.
     *
     * @return the list of site entries
     */
    public List<SiteEntry> scrape(Document doc);
}
