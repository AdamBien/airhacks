package airhacks.qmp.fleet.entity;

public enum BikeStatus {
    AVAILABLE,
    ALLOCATED,
    WITHDRAWN;

    public String label() {
        return name().toLowerCase();
    }
}
