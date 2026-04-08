package org.example.ideavoltex.repository;

import org.example.ideavoltex.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

/**
 * Repository interface for User entity persistence.
 * Leverages MongoDB for storing encrypted user profiles and supporting
 * privacy-preserving search patterns.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Executes a lookup using the deterministic Blind Index.
     * This is the primary method for authentication, allowing the system to
     * identify a user record without decrypting email addresses during the search.
     * * @param blindIndex The non-reversible hash generated from the user's ID/Email.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByBlindIndex(String blindIndex);

    /**
     * Retrieves a user based on their encrypted email ciphertext.
     * Typically used for session-based lookups where the exact ciphertext
     * stored in the session is used as a unique identifier.
     * * @param email The Ascon-encrypted email string.
     * @return An Optional containing the User if the ciphertext matches.
     */
    Optional<User> findByEmail(String email);
}