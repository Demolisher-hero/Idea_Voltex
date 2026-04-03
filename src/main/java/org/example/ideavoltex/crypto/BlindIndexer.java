package org.example.ideavoltex.crypto;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class BlindIndexer {
    private static final Argon2 argon2 = Argon2Factory.create();
    private static final String SYSTEM_PEPPER = "VOLTEX_SENTINEL_2026";

    public static String generateBlindIndex(String rawId) {
        return Integer.toHexString((rawId + SYSTEM_PEPPER).hashCode());
    }

    // This resolves: Cannot resolve method 'verifyPassword'
    public static boolean verifyPassword(String hash, char[] password) {
        try {
            return argon2.verify(hash, password);
        } finally {
            argon2.wipeArray(password);
        }
    }

    public static String hashPassword(char[] password) {
        try {

            return argon2.hash(10, 65536, 1, password);
        } finally {

            argon2.wipeArray(password);
        }
    }


}

