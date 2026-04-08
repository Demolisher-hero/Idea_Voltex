package org.example.ideavoltex.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Data model for the 'users' collection in MongoDB.
 * Implements a "Zero-Knowledge" storage pattern where sensitive
 * identifiable data is stored in encrypted form.
 */
@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    /**
     * User's full name, stored as an Ascon-128 encrypted Hex string.
     * Must be decrypted at the service layer before being sent to the UI.
     */
    private String name;

    /**
     * Encrypted email address.
     * Unique index ensures no duplicate accounts exist while maintaining
     * ciphertext privacy at the database level.
     */
    @Indexed(unique = true)
    private String email;

    /**
     * A deterministic hash (Blind Index) of the user's email/ID.
     * Used for high-speed O(1) database lookups without requiring
     * the decryption of the 'email' field.
     */
    @Indexed
    private String blindIndex;

    /**
     * One-way cryptographic hash of the user's password.
     * Generated using the Argon2 memory-hard function to prevent
     * brute-force and dictionary attacks.
     */
    private String passwordHash;
}