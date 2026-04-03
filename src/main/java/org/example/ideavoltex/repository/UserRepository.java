package org.example.ideavoltex.repository;

import org.example.ideavoltex.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// This resolves: Cannot resolve symbol 'UserRepository'
public interface UserRepository extends MongoRepository<User, String> {

    // This resolves: Cannot resolve method 'findByBlindIndex(String)'
    Optional<User> findByBlindIndex(String blindIndex);
}