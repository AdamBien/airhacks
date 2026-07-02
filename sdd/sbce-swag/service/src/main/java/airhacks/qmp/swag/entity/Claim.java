package airhacks.qmp.swag.entity;

import java.util.regex.Pattern;

public record Claim(String email, String tShirtSize, String socksSize, ShippingAddress shippingAddress) {

    static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public boolean complete() {
        return present(this.email) && present(this.tShirtSize) && present(this.socksSize)
                && this.shippingAddress != null && this.shippingAddress.complete();
    }

    public boolean emailValid() {
        return this.email != null && EMAIL.matcher(this.email).matches();
    }

    static boolean present(String value) {
        return value != null && !value.isBlank();
    }
}
