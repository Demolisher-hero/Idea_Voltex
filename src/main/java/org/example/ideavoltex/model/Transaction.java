package org.example.ideavoltex.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private String encryptedAmount; // The Hex result from Ascon
    private String nonce;           // Store as Hex to retrieve later
    private LocalDateTime timestamp;
}