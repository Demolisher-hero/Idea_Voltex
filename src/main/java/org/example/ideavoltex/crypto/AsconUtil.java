package org.example.ideavoltex.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility wrapper for the Ascon-128 AEAD implementation.
 * Provides high-level methods for string-based encryption, decryption with twists.
 * and searchable indexing (Blind Indexing).
 */
public class AsconUtil {

    private static final int KEY_SIZE = 16;
    private static final int NONCE_SIZE = 16;
    private static final int TAG_SIZE = 16;

    // Static cryptographic parameters for the prototype phase
    private static final byte[] MASTER_KEY = "SENTINEL_CORE_16".getBytes(StandardCharsets.UTF_8);
    private static final byte[] STATIC_NONCE = "FIXED_NONCE_16_B".getBytes(StandardCharsets.UTF_8);

    /**
     * Standard encryption entry point for the application.
     * Encrypts plaintext using the default master key and static nonce.
     */
    public static String encrypt(String plaintext) throws Exception {
        return encrypt(plaintext, MASTER_KEY, STATIC_NONCE);
    }

    /**
     * Standard decryption entry point for the application.
     * Converts Hex-encoded ciphertext back to plaintext after integrity verification.
     */
    public static String decrypt(String hexCiphertext) throws Exception {
        return decrypt(hexCiphertext, MASTER_KEY, STATIC_NONCE);
    }

    /**
     * Core encryption logic.
     * Bridges the application Strings with the byte-level Ascon AEAD implementation.
     * @return Hex-encoded string containing both Ciphertext and the 16-byte Authentication Tag.
     */
    public static String encrypt(String plaintext, byte[] key, byte[] nonce) throws Exception {
        byte[] m = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] c = new byte[m.length + TAG_SIZE];
        byte[] ad = new byte[0]; // Associated Data is empty for basic PII encryption

        // Execute Ascon-128 authenticated encryption
        int clen = Ascon128v12.crypto_aead_encrypt(c, 0, m, m.length, ad, ad.length, null, nonce, key);

        return HexFormat.of().formatHex(c);
    }

    /**
     * Core decryption logic with built-in integrity checking.
     * @throws SecurityException If the Ascon tag verification fails (indicating data tampering).
     */
    public static String decrypt(String hexCiphertext, byte[] key, byte[] nonce) throws Exception {
        byte[] c = HexFormat.of().parseHex(hexCiphertext);
        byte[] m = new byte[c.length - TAG_SIZE];
        byte[] ad = new byte[0];

        // Execute Ascon-128 decryption and tag verification
        int mlen = Ascon128v12.crypto_aead_decrypt(m, 0, null, c, c.length, ad, ad.length, nonce, key);

        // A return value of -1 indicates the authentication tag does not match the ciphertext
        if (mlen == -1) {
            throw new SecurityException("Tampering detected! Sentinel-DB integrity compromised.");
        }

        return new String(m, 0, mlen, StandardCharsets.UTF_8);
    }

    /**
     * Generates a deterministic HMAC-SHA256 hash to serve as a 'Blind Index'.
     * This allows for database lookups on encrypted fields without decrypting the data.
     */
    public static String generateBlindIndex(String input, byte[] key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return HexFormat.of().formatHex(sha256_HMAC.doFinal(input.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Securely generates a random 16-byte nonce for unique initialization vectors.
     */
    public static byte[] generateNonce() {
        byte[] nonce = new byte[NONCE_SIZE];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
}