import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class BaseScraper implements Scraper {
    String baseUri;

    public BaseScraper(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Downloads the document.
     *
     * @param endpoint the endpoint of the baseUri to download from
     * @return the downloaded Document
     */
    public Document download(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
