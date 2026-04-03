package org.example.ideavoltex.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat; // Native replacement

public class AsconUtil {

    private static final int KEY_SIZE = 16;
    private static final int NONCE_SIZE = 16;
    private static final int TAG_SIZE = 16;

    public static String encrypt(String plaintext, byte[] key, byte[] nonce) throws Exception {
        byte[] m = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] c = new byte[m.length + TAG_SIZE];

        byte[] ad = new byte[0];

        int clen = Ascon128v12.crypto_aead_encrypt(c, 0, m, m.length, ad, ad.length, null, nonce, key);

        // Code Shift: Static call replaced by HexFormat instance
        return HexFormat.of().formatHex(c);
    }

    public static String decrypt(String hexCiphertext, byte[] key, byte[] nonce) throws Exception {
        // Code Shift: Static call replaced by HexFormat instance
        byte[] c = HexFormat.of().parseHex(hexCiphertext);

        byte[] m = new byte[c.length - TAG_SIZE];
        byte[] ad = new byte[0];

        int mlen = Ascon128v12.crypto_aead_decrypt(m, 0, null, c, c.length, ad, ad.length, nonce, key);

        if (mlen == -1) {
            throw new SecurityException("Tampering detected! Decryption failed.");
        }

        return new String(m, 0, mlen, StandardCharsets.UTF_8);
    }

    public static byte[] generateNonce() {
        byte[] nonce = new byte[NONCE_SIZE];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
}