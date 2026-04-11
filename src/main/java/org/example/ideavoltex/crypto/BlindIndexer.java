package org.example.ideavoltex.crypto;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Provides deterministic indexing and secure password management.
 * * This class handles:
 * 1. Blind Indexing: Allowing data lookups without decrypting PII.
 * 2. Argon2 Hashing: Secure, memory-hard password storage to resist GPU cracking.
 * This is the base of all
 */
public class BlindIndexer {

    // Argon2 instance for password hashing and verification
    private static final Argon2 argon2 = Argon2Factory.create();

    // System-wide salt (pepper) to increase entropy of deterministic hashes
    private static final String SYSTEM_PEPPER = "VOLTEX_SENTINEL_2026";

    /**
     * Generates a deterministic, non-reversible hash of a unique identifier.
     * This "Blind Index" allows the database to locate records via a search key
     * while the actual identifier remains encrypted and unreadable.
     */
    public static String generateBlindIndex(String rawId) {
        // Concatenates ID with pepper to ensure hashes are unique to this system
        return Integer.toHexString((rawId + SYSTEM_PEPPER).hashCode());
    }

    /**
     * Validates a plaintext password against a stored Argon2 hash.
     * Uses a 'finally' block to wipe the sensitive char array from memory immediately.
     */
    public static boolean verifyPassword(String hash, char[] password) {
        try {
            return argon2.verify(hash, password);
        } finally {
            // Security: Clear the memory buffer to prevent heap dumps from exposing credentials
            argon2.wipeArray(password);
        }
    }

    /**
     * Creates a secure Argon2 hash of a user's password.
     * Configuration: 10 iterations, 64MB memory usage, 1 parallel thread.
     */
    public static String hashPassword(char[] password) {
        try {
            // High-cost parameters selected to deter brute-force and rainbow table attacks
            return argon2.hash(10, 65536, 1, password);
        } finally {
            // Security: Securely wipe the plaintext password array
            argon2.wipeArray(password);
        }
    }
}