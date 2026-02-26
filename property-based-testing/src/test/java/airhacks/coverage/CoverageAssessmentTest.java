package airhacks.coverage;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;

import static airhacks.coverage.CoverageAssessment.*;
import static org.assertj.core.api.Assertions.assertThat;

class CoverageAssessmentTest {

    @Property
    void coverageZeroToNineteenIsKamikaze(@ForAll @IntRange(min = 0, max = 19) int coverage) {
        assertThat(from(coverage)).isEqualTo(KAMIKAZE);
    }

    @Property
    void coverageTwentyToSeventyEightIsReasonable(@ForAll @IntRange(min = 20, max = 78) int coverage) {
        assertThat(from(coverage)).isEqualTo(REASONABLE);
    }

    @Property
    void coverageSeventyNineToNinetyNineIsContractual(@ForAll @IntRange(min = 79, max = 99) int coverage) {
        assertThat(from(coverage)).isEqualTo(CONTRACTUAL);
    }

    @Property
    void coverageOneHundredIsExaggerator(@ForAll @IntRange(min = 100, max = 100) int coverage) {
        assertThat(from(coverage)).isEqualTo(EXAGGERATOR);
    }

    @Property
    void negativeValuesAreKamikaze(@ForAll @IntRange(min = Integer.MIN_VALUE, max = -1) int coverage) {
        assertThat(from(coverage)).isEqualTo(KAMIKAZE);
    }

    @Property
    void valuesAboveOneHundredAreExaggerator(@ForAll @IntRange(min = 101, max = Integer.MAX_VALUE) int coverage) {
        assertThat(from(coverage)).isEqualTo(EXAGGERATOR);
    }
}
