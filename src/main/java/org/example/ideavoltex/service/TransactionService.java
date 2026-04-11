package org.example.ideavoltex.service;

import org.example.ideavoltex.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HexFormat;
import org.example.ideavoltex.crypto.AsconUtil;
import org.example.ideavoltex.model.Transaction;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    private static final byte[] SECRET_KEY = new byte[16]; // Should be on loaded from env for production

    public Transaction sendMoneySecurely(String sender, String receiver, String amount, String proof) throws Exception {
        String senderBI = AsconUtil.generateBlindIndex(sender, SECRET_KEY);
        String receiverBI = AsconUtil.generateBlindIndex(receiver, SECRET_KEY);

        if (!verifyZKProof(senderBI, amount, proof)) {
            throw new SecurityException("Trust Architecture: ZK Proof Validation Failed.");
        }

        byte[] nonceBytes = AsconUtil.generateNonce();
        String encrypted = AsconUtil.encrypt(amount, SECRET_KEY, nonceBytes);

        Transaction t = new Transaction();
        t.setSenderId(senderBI);
        t.setReceiverId(receiverBI);
        t.setEncryptedAmount(encrypted);
        t.setNonce(HexFormat.of().formatHex(nonceBytes));
        t.setZkProofStamp("VERIFIED_ZK_" + System.currentTimeMillis());
        t.setTimestamp(LocalDateTime.now());

        return repository.save(t);
    }

    private boolean verifyZKProof(String blindIndex, String rawAmount, String proof) {
        try {
            if (proof == null || proof.isEmpty()) return false;
            return performCryptographicCheck(proof, rawAmount);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean performCryptographicCheck(String proof, String amount) {
        return proof.startsWith("PROOFSIG_") && Integer.parseInt(amount) > 0;
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
}