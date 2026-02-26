package airhacks.coverage;

/**
 * Opinionated classification of code coverage percentages into qualitative tiers.
 * Constants are ordered by ascending threshold — each tier represents the minimum
 * coverage percentage at which that assessment applies.
 */
public enum CoverageAssessment {

    KAMIKAZE(0),
    REASONABLE(20),
    CONTRACTUAL(79),
    EXAGGERATOR(100);

    int coverage;

     CoverageAssessment() {
        this.coverage = 0;
    }

     CoverageAssessment(int coverage) {
        this.coverage = coverage;
    }

     int coverage() {
        return coverage;
    }

    /**
     * Maps a coverage percentage to its tier by scanning thresholds from highest
     * to lowest and returning the first tier the value qualifies for.
     * Values below the lowest threshold (0) default to {@link #KAMIKAZE}.
     *
     * @param coverage percentage value (0–100); values outside this range are
     *        not rejected but may produce misleading classifications
     */
    static CoverageAssessment from(int coverage) {
        var values = values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (coverage >= values[i].coverage) {
                return values[i];
            }
        }
        return KAMIKAZE;
    }

     
}
