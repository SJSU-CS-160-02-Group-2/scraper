import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * Scrapes data from a web site.
 *
 * @author dead10ck, @date 4/2/15 11:58 AM
 */
public class ShodorScraper extends BaseScraper {
    private static final String BASE_URI =
        "http://www.shodor.org/interactivate/activities";
    private static final String ROOT_SELECTOR = "div#listing0";

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

    protected static Element rootElement(Document doc) throws Exception {
        Elements elts = doc.select(ROOT_SELECTOR);

        if (elts.size() != 1) {
            throw new Exception(
                String.format("Selector found %d elements in root search",
                    elts.size()));
        }

        return elts.first();
    }
}
