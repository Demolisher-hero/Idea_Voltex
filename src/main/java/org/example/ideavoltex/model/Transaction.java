package org.example.ideavoltex.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Data model for the 'transactions' collection in MongoDB.
 * Represents a secure ledger entry utilizing Encrypted-at-Rest data
 * and Zero-Knowledge Proof (ZKP) verification markers which will helpful in the future.
 */
@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    // Searchable blind indexes of the participating users
    private String senderId;
    private String receiverId;

    /**
     * The transaction value encrypted using the Ascon-128 algorithm.
     * Stored as a Hex-encoded string to ensure database-level privacy.
     */
    private String encryptedAmount;

    /**
     * Unique 16-byte initialization vector (IV) used during the Ascon
     * encryption process to ensure ciphertext uniqueness for identical amounts.
     */
    private String nonce;

    /**
     * ISO-8601 formatted timestamp of when the transaction was finalized.
     */
    private LocalDateTime timestamp;

    /**
     * Cryptographic proof stamp verifying the validity of the transaction
     * without revealing private inputs.
     * Required for PS-401 regulatory compliance and audit trails.
     */
    private String zkProofStamp;
}