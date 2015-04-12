import java.util.Set;
import java.io.*;

import org.jsoup.nodes.Document;

public class Main {
    public static void main(String[] args) throws Exception {
        ShodorScraper scraper = new ShodorScraper();
        Document bySubjectDoc = scraper.bySubject();
        Document byAudienceDoc = scraper.byAudience();
        Set<SiteEntry> sites = scraper.scrape(bySubjectDoc, byAudienceDoc);
        PrintWriter printWriter = new PrintWriter ("data.txt");


        for (SiteEntry site : sites) {
             printWriter.print(site.toString());
             printWriter.print("\n");

        }
        printWriter.close();
        System.out.printf("Scraped %d entries\n", sites.size());
    }
}
