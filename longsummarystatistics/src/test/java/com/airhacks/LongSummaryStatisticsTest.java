package com.airhacks;

import java.util.ArrayList;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class LongSummaryStatisticsTest {

    private ArrayList<MethodCall> calls;

    @Before
    public void provideTestData() {
        this.calls = new ArrayList<>();
        calls.add(new MethodCall("save", 90));
        calls.add(new MethodCall("find", 10));
        calls.add(new MethodCall("delete", 2));

    }

    @Test
    public void computeStatistics() {
        LongSummaryStatistics statistics = this.calls.stream().
                collect(Collectors.summarizingLong(MethodCall::getDuration));
        assertThat(statistics.getCount(), is(3l));
        assertThat(statistics.getMin(), is(2l));
        assertThat(statistics.getMax(), is(90l));
        assertThat(statistics.getAverage(), is(34d));
        assertThat(statistics.getSum(), is(102l));
    }

}
