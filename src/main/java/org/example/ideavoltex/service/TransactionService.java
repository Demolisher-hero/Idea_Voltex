package org.example.ideavoltex.service;

import org.example.ideavoltex.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HexFormat; // Native Java 17+ replacement
import org.example.ideavoltex.crypto.AsconUtil;
import org.example.ideavoltex.model.Transaction;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    // 16-byte key (System Architect Note: Use a secure random key or env variable in production)
    private static final byte[] SECRET_KEY = new byte[16];

    public Transaction sendMoney(String sender, String receiver, String amount) throws Exception {
        // 1. Generate Nonce via your AsconUtil
        byte[] nonceBytes = AsconUtil.generateNonce();

        // 2. Encrypt the plaintext amount using ASCON-128 engine
        String encrypted = AsconUtil.encrypt(amount, SECRET_KEY, nonceBytes);

        // 3. Create and populate the Secure Transaction Object
        Transaction t = new Transaction();
        t.setSenderId(sender);
        t.setReceiverId(receiver);
        t.setEncryptedAmount(encrypted);

        // 4. Store the nonce using native HexFormat (No Bouncy Castle needed)
        t.setNonce(HexFormat.of().formatHex(nonceBytes));
        t.setTimestamp(LocalDateTime.now());

        // 5. Persist to Mumbai MongoDB Cluster
        return repository.save(t);
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
}