package com.airhacks;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class StreamsTest {

    @Test
    public void simple() {
        List<String> uuids = Stream.generate(this::next).
                map(u -> u.toString()).
                limit(10).
                collect(Collectors.toList());
        uuids.forEach(System.out::println);
    }

    @Test
    public void mapToBetrieb() {
        double averagePowerOfAllMachines = Stream.generate(this::next).
                map(u -> u.toString()).
                filter(this::withLeadingZero).
                limit(10).
                map(this::find).
                map(b -> b.getMaschine()).
                mapToInt(m -> m.getPs()).
                average().
                orElse(-1);
        System.out.println("averagePowerOfAllMachines = " + averagePowerOfAllMachines);
    }

    @Test
    public void stats() {
        LongSummaryStatistics stats = Stream.generate(this::next).
                map(u -> u.toString()).
                filter(this::withLeadingZero).
                limit(10).
                map(this::find).
                map(Betrieb::getMaschine).
                collect(Collectors.summarizingLong(Maschine::getPs));
        System.out.println("stats = " + stats);
    }

    @Test

    public void machines() {
        List<Maschine> collect = Stream.generate(this::next).
                map(u -> u.toString()).
                filter(this::withLeadingZero).
                limit(10).
                map(this::find).
                map(b -> b.getMaschine()).
                collect(Collectors.toList());
        System.out.println("collect = " + collect);
    }

    public boolean withLeadingZero(String input) {
        return input.startsWith("0");
    }

    public Betrieb find(String id) {
        return new Betrieb(id);
    }

    UUID next() {
        return UUID.randomUUID();
    }

}
