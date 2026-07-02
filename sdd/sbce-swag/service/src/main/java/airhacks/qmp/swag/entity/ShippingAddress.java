package airhacks.qmp.swag.entity;

public record ShippingAddress(String firstName, String lastName, String street, String postalCode, String city, String country) {

    public boolean complete() {
        return present(this.firstName) && present(this.lastName) && present(this.street)
                && present(this.postalCode) && present(this.city) && present(this.country);
    }

    static boolean present(String value) {
        return value != null && !value.isBlank();
    }
}
