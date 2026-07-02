import airhacks.zsmith.episodicmemory.entity.Episode;
import airhacks.zsmith.episodicmemory.entity.MemoryType;

void main() {
    // Episode.of(content) defaults timestamp and null type
    var episode = Episode.of("test content");
    assert "test content".equals(episode.content()) : "content mismatch";
    assert episode.timestamp() != null : "timestamp should default";
    assert episode.type() == null : "type should be null";

    // Episode.of(content, type) sets type
    var typed = Episode.of("feedback note", MemoryType.feedback);
    assert MemoryType.feedback == typed.type() : "type should be feedback";

    // hasType returns true for matching type
    assert typed.hasType(MemoryType.feedback) : "hasType should return true for matching type";

    // hasType returns false for non-matching type
    assert !typed.hasType(MemoryType.project) : "hasType should return false for non-matching type";

    // hasType with null type on episode
    assert !episode.hasType(MemoryType.user) : "hasType should return false when episode type is null";

    // blank content throws
    try {
        Episode.of("   ");
        assert false : "should have thrown for blank content";
    } catch (IllegalArgumentException e) {
        // expected
    }

    // JSON round-trip
    var original = Episode.of("round trip test", MemoryType.reference);
    var json = original.toJSON();
    var restored = Episode.fromJSON(json);
    assert original.content().equals(restored.content()) : "content mismatch after round-trip";
    assert original.timestamp().equals(restored.timestamp()) : "timestamp mismatch after round-trip";
    assert original.type() == restored.type() : "type mismatch after round-trip";
}
