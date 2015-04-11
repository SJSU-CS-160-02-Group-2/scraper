import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class SiteEntry {
    private static final String AUTHOR = "Shodor Education Foundation, Inc.";
    private static final String CONTENT_TYPE = "interactive lesson";

    private String title;
    private String description;
    private Set<String> categories;
    private Set<Integer> targetGrades;
    private String iconImageUrl;
    private String activityUrl;
    private Set<String> contributors;
    private String isoTime;

    public SiteEntry(Builder builder) {
        this.title = builder.getTitle();
        this.description = builder.getDescription();
        this.categories = builder.getCategories();
        this.targetGrades = builder.getTargetGrades();
        this.iconImageUrl = builder.getIconImageUrl();
        this.activityUrl = builder.getActivityUrl();
        this.contributors = builder.getContributors();

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        this.isoTime = df.format(new Date());
    }

    @Override
    public String toString() {
        String catsString = stripBrackets(categories.toString());
        String gradesString = stripBrackets(targetGrades.toString());

        return String.format(
            "%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n%s",
            title, description, activityUrl, iconImageUrl, catsString, gradesString,
            AUTHOR, CONTENT_TYPE, isoTime
        );
    }

    private String stripBrackets(String s) {
        if (s.length() < 3) {
            return s;
        }

        return s.substring(1, s.length()-1);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the categories
     */
    public Set<String> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    /**
     * @return the targetGrades
     */
    public Set<Integer> getTargetGrades() {
        return targetGrades;
    }

    /**
     * @param targetGrades the targetGrades to set
     */
    public void setTargetGrades(Set<Integer> targetGrades) {
        this.targetGrades = targetGrades;
    }

    /**
     * @return the iconImageUrl
     */
    public String getIconImageUrl() {
        return iconImageUrl;
    }

    /**
     * @return the activityUrl
     */
    public String getActivityUrl() {
        return activityUrl;
    }

    /**
     * @return the contributors
     */
    public Set<String> getContributors() {
        return contributors;
    }

    // Base the hashCode and equality on the activity URL so that duplicates
    // get weeded out in HashSets
    @Override
    public int hashCode() {
        return activityUrl.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return this == null;
        }

        if (!(obj instanceof SiteEntry)) {
            return false;
        }

        SiteEntry other = (SiteEntry) obj;
        return this.activityUrl.equals(other.getActivityUrl());
    }

    public static class Builder {
        private String title;
        private String description;
        private Set<String> categories;
        private Set<Integer> targetGrades;
        private String iconImageUrl;
        private String activityUrl;
        private Set<String> contributors;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withCategory(String category) {
            if (this.categories == null) {
                this.categories = new HashSet<String>();
            }

            this.categories.add(category);
            return this;
        }

        public Builder withCategories(Set<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder withTargetGrades(Set<Integer> targetGrades) {
            this.targetGrades = targetGrades;
            return this;
        }

        public Builder withIconImageUrl(String iconImageUrl) {
            this.iconImageUrl = iconImageUrl;
            return this;
        }

        public Builder withActivityUrl(String activityUrl) {
            this.activityUrl = activityUrl;
            return this;
        }

        public Builder withContributors(Set<String> contributors) {
            this.contributors = contributors;
            return this;
        }

        public SiteEntry toSiteEntry() {
            return new SiteEntry(this);
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the categories
         */
        public Set<String> getCategories() {
            return categories;
        }

        /**
         * @return the targetGrades
         */
        public Set<Integer> getTargetGrades() {
            return targetGrades;
        }

        /**
         * @return the iconImageUrl
         */
        public String getIconImageUrl() {
            return iconImageUrl;
        }

        /**
         * @return the activityUrl
         */
        public String getActivityUrl() {
            return activityUrl;
        }

        /**
         * @return the contributors
         */
        public Set<String> getContributors() {
            return contributors;
        }
    }
}
