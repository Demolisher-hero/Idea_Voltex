package org.example.ideavoltex.repository;

import org.example.ideavoltex.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Data Access Object (DAO) for Transaction entities.
 * Facilitates interaction with the MongoDB 'transactions' collection,
 * leveraging Spring Data MongoDB for automated query generation.
 */
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    /**
     * Retrieves all transaction records associated with a specific sender.
     * * @param senderId The deterministic Blind Index of the sender's identifier.
     * @return A list of Transaction objects where the senderId matches the index.
     * * Note: This method performs lookups on the 'senderId' index, which allows for
     *  efficient retrieval without exposing the sender's actual PII.
     */
    List<Transaction> findBySenderId(String senderId);
}