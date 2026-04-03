package org.example.ideavoltex.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Indexed
    private String blindIndex;

    // This resolves: Cannot resolve method 'getPasswordHash'
    private String passwordHash;
}