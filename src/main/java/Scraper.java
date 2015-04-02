import java.io.IOException;

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
}
