package airhacks.contacts.contacts.entity;

public enum ContactType {
    BUSINESS, PRIVATE;

    /**
     * @param value the JSON representation ("business" or "private")
     * @return the matching type, or null for a missing or unknown value —
     *         rejected downstream by {@link Contact#isValid()}
     */
    public static ContactType fromJSON(String value) {
        return switch (value) {
            case "business" -> BUSINESS;
            case "private" -> PRIVATE;
            default -> null;
        };
    }

    public String toJSON() {
        return this.name().toLowerCase();
    }
}
