import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
 * Scrapes data from a web site.
 *
 * @author dead10ck, @date 4/2/15 11:58 AM
 */
public class Scraper {
    private static final String BASE_URI =
        "http://www.shodor.org/interactivate/activities/";

    public enum Endpoint {
        BY_SUBJECT("bySubject/"),
        BY_AUDIENCE("byAudience/");

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

    public static Document getDocument(Endpoint ep) throws IOException {
        String url = BASE_URI + ep.toString();
        return Jsoup.connect(url).get();
    }
}
