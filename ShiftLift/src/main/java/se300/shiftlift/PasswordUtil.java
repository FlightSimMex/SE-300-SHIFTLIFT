package se300.shiftlift;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

final class PasswordUtil {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private PasswordUtil() {}

    static String hash(String raw) {
        if (raw == null) return null;
        // If already a BCrypt hash, return as-is
        if (isBcryptHash(raw)) return raw;
        return ENCODER.encode(raw);
    }

    static boolean matches(String raw, String hashed) {
        if (raw == null || hashed == null) return false;
        if (isBcryptHash(hashed)) {
            return ENCODER.matches(raw, hashed);
        }
        // Fallback: if legacy plaintext stored, allow direct comparison once
        return raw.equals(hashed);
    }

    static boolean isBcryptHash(String value) {
        if (value == null) return false;
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
