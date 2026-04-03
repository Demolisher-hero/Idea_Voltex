package org.example.ideavoltex.repository;

import org.example.ideavoltex.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findBySenderId(String senderId);
}