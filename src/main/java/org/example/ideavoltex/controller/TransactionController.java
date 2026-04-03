package org.example.ideavoltex.controller;

import org.example.ideavoltex.model.Transaction;
import org.example.ideavoltex.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    // 1. Send Money - Now using @RequestBody for better security
    @PostMapping("/send")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction request) {
        try {
            // Using the fields from the incoming JSON body
            Transaction result = service.sendMoney(
                    request.getSenderId(),
                    request.getReceiverId(),
                    request.getEncryptedAmount() // This is the plaintext 'amount' from the UI/request
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. View History - To check your MongoDB Atlas data
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }
}