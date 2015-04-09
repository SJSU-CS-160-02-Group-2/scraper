import java.util.Set;

import org.jsoup.nodes.Document;

public class Main {
    public static void main(String[] args) throws Exception {
        ShodorScraper scraper = new ShodorScraper();
        Document bySubjectDoc = scraper.bySubject();
        Document byAudienceDoc = scraper.byAudience();
        Set<SiteEntry> sites = scraper.scrape(bySubjectDoc, byAudienceDoc);

        for (SiteEntry site : sites) {
            System.out.printf("%s\n\n", site.toString());
        }

        System.out.printf("Scraped %d entries\n", sites.size());
    }
}
