import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zsmith.configuration.control.ZCfg;
import airhacks.zsmith.episodicmemory.boundary.EpisodicMemoryStore;
import airhacks.zsmith.episodicmemory.entity.Episode;
import airhacks.zsmith.episodicmemory.entity.MemoryType;

void main() throws Exception {
    ZCfg.loadBaseConfig("zsmith-test-" + ProcessHandle.current().pid());

    var tempFile = Files.createTempFile("episodic-test", ".json");
    Files.deleteIfExists(tempFile);

    var store = new EpisodicMemoryStore(tempFile);

    // store episodes of different types
    store.store(Episode.of("user pref", MemoryType.user));
    store.store(Episode.of("project note", MemoryType.project));
    store.store(Episode.of("another user pref", MemoryType.user));
    store.store(Episode.of("feedback item", MemoryType.feedback));

    // allEpisodes returns all
    assert store.allEpisodes().size() == 4 : "expected 4 episodes, got: " + store.allEpisodes().size();

    // byType filters correctly
    var userEpisodes = store.byType(MemoryType.user);
    assert userEpisodes.size() == 2 : "expected 2 user episodes, got: " + userEpisodes.size();
    assert userEpisodes.stream().allMatch(e -> e.type() == MemoryType.user) : "all should be user type";

    var projectEpisodes = store.byType(MemoryType.project);
    assert projectEpisodes.size() == 1 : "expected 1 project episode, got: " + projectEpisodes.size();

    // recent(n) returns last n
    var recent2 = store.recent(2);
    assert recent2.size() == 2 : "expected 2 recent, got: " + recent2.size();
    assert "feedback item".equals(recent2.getLast().content()) : "last recent should be feedback item";

    // recent(0) returns empty
    assert store.recent(0).isEmpty() : "recent(0) should be empty";

    // catalog formats stored episodes
    var catalog = store.catalog();
    assert catalog.contains("## Recalled Memories") : "catalog should contain header, got: " + catalog;
    assert catalog.contains("user pref") : "catalog should contain user pref";
    assert catalog.contains("project note") : "catalog should contain project note";
    assert catalog.contains("feedback item") : "catalog should contain feedback item";
    for (var line : catalog.lines().toList()) {
        if (line.startsWith("- ")) {
            assert line.matches("^- \\[\\d{4}-\\d{2}-\\d{2} \\w+\\] .+") : "bullet line malformed: " + line;
        }
    }

    // per-type cap respected; total cap respected
    for (int i = 0; i < 30; i++) {
        store.store(Episode.of("bulk " + i, MemoryType.feedback));
    }
    var capped = store.catalog(5, 20);
    var feedbackLines = capped.lines().filter(l -> l.contains("] bulk ")).count();
    assert feedbackLines <= 5 : "expected ≤ 5 bulk feedback lines, got: " + feedbackLines;
    var bulletCount = capped.lines().filter(l -> l.startsWith("- ")).count();
    assert bulletCount <= 20 : "expected ≤ 20 total bullets, got: " + bulletCount;

    // zeroed caps disable injection
    assert "".equals(store.catalog(0, 20)) : "catalog(0, 20) should be empty";
    assert "".equals(store.catalog(5, 0)) : "catalog(5, 0) should be empty";

    // clear removes all; catalog becomes empty
    store.clear();
    assert store.allEpisodes().isEmpty() : "should be empty after clear";
    assert !Files.exists(tempFile) : "file should be deleted after clear";
    assert "".equals(store.catalog()) : "catalog of empty store should be empty";
}
