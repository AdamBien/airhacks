package airhacks.zsmith.episodicmemory.entity;

public enum MemoryType {

    user,
    feedback,
    project,
    reference;

    public static MemoryType fromString(String text) {
        if (text == null) {
            return null;
        }
        return valueOf(text.toLowerCase());
    }
}
