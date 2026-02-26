package airhacks.qmp.auth.control;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PasswordService {

    static final int ITERATIONS = 65536;
    static final int KEY_LENGTH = 256;
    static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    final SecureRandom random = new SecureRandom();

    public String generateSalt() {
        var salt = new byte[16];
        this.random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hash(String password, String salt) {
        try {
            var spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
            var factory = SecretKeyFactory.getInstance(ALGORITHM);
            var hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    public boolean verify(String password, String hash, String salt) {
        return hash(password, salt).equals(hash);
    }
}
