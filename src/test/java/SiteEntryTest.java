import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class SiteEntryTest {
    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(SiteEntryTest.class);

    public SiteEntry dummySiteEntry() {
        SiteEntry.Builder builder = new SiteEntry.Builder();

        String title = "3D Transmographer";
        String description = "Build your own polygon and transform it in the" +
            " Cartesian coordinate system. Experiment with reflections" +
            " across any line, revolving around any line (which yields a" +
            " 3-D image), rotations about any point, and translations in" +
            " any direction.";


        HashSet<String> categories = new HashSet<>();
        categories.add("Algebra");
        categories.add("Trigonometry");

        HashSet<Integer> targetGrades = new HashSet<>();
        targetGrades.add(3);
        targetGrades.add(4);
        targetGrades.add(5);

        String iconImageUrl = "http://www.shodor.org/media/N/j/Z/mNDk4YjUzYmRhMjhlODE2MDA0NzA2NjExNGIxNzQ.png";
        String activityUrl = "http://www.shodor.org/interactivate/activities/3DTransmographer/";

        builder
            .withTitle(title)
            .withDescription(description)
            .withCategories(categories)
            .withTargetGrades(targetGrades)
            .withIconImageUrl(iconImageUrl)
            .withActivityUrl(activityUrl)
            .withContributors(new HashSet<String>())
            ;

        return builder.toSiteEntry();
    }

    @Test
    public void testBuilder() {
        String title = "3D Transmographer";
        String description = "Build your own polygon and transform it in the" +
            " Cartesian coordinate system. Experiment with reflections" +
            " across any line, revolving around any line (which yields a" +
            " 3-D image), rotations about any point, and translations in" +
            " any direction.";


        HashSet<String> categories = new HashSet<>();
        categories.add("Algebra");
        categories.add("Trigonometry");

        HashSet<Integer> targetGrades = new HashSet<>();
        targetGrades.add(3);
        targetGrades.add(4);
        targetGrades.add(5);

        String iconImageUrl = "http://www.shodor.org/media/N/j/Z/mNDk4YjUzYmRhMjhlODE2MDA0NzA2NjExNGIxNzQ.png";
        String activityUrl = "http://www.shodor.org/interactivate/activities/3DTransmographer/";

        SiteEntry entry = dummySiteEntry();

        assertEquals(entry.getTitle(), title);
        assertEquals(entry.getDescription(), description);
        assertEquals(entry.getCategories(), categories);
        assertEquals(entry.getTargetGrades(), targetGrades);
        assertEquals(entry.getIconImageUrl(), iconImageUrl);
        assertEquals(entry.getActivityUrl(), activityUrl);
    }

    @Test
    /**
     * Test if multiple SiteEntries being added to a set results
     * in duplicated entries.
     */
    public void testMultipleEntriesInSet() {
        Set<SiteEntry> entrySet = new HashSet<>();
        SiteEntry entry1 = dummySiteEntry();
        //logger.info(String.format("%s: %d", entry1.getTitle(), entry1.hashCode()));
        SiteEntry entry2 = dummySiteEntry();
        //logger.info(String.format("%s: %d", entry2.getTitle(), entry2.hashCode()));

        entrySet.add(entry1);
        entrySet.add(entry2);

        assertTrue(entrySet.size() == 1);
    }
}
