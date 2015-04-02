import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.*;

public class SiteEntryTest {
    @Test
    public void testBuilder() {
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

        SiteEntry entry = builder.toSiteEntry();

        assertEquals(entry.getTitle(), title);
        assertEquals(entry.getDescription(), description);
        assertEquals(entry.getCategories(), categories);
        assertEquals(entry.getTargetGrades(), targetGrades);
        assertEquals(entry.getIconImageUrl(), iconImageUrl);
        assertEquals(entry.getActivityUrl(), activityUrl);
    }
}
