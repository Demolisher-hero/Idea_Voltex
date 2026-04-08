package org.example.ideavoltex.controller;

import org.example.ideavoltex.model.Transaction;
import org.example.ideavoltex.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Controller managing secure transaction workflows and dashboard data visualization.
 * Supports both standard MVC view resolution and RESTful API interactions.
 */
@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    /**
     * View resolver for the transaction dashboard.
     * Fetches historical transaction data and populates the UI model for Thymeleaf rendering.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Transaction> transactions = service.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "dashboard";
    }

    /**
     * API endpoint for executing secure transfers.
     * Processes transaction requests using Zero-Knowledge Proofs (ZKP) and Ascon-encrypted data.
     * * @param request Map containing sender, receiver, amount, and cryptographic proof.
     * @return ResponseEntity with the transaction record or security-related error status.
     */
    @PostMapping("/api/send")
    @ResponseBody
    public ResponseEntity<?> send(@RequestBody Map<String, String> request) {
        try {
            // Invokes the core service layer to validate proofs and finalize the secure ledger entry
            Transaction result = service.sendMoneySecurely(
                    request.get("sender"),
                    request.get("receiver"),
                    request.get("amount"),
                    request.get("proof")
            );
            return ResponseEntity.ok(result);
        } catch (SecurityException e) {
            // Returns 403 Forbidden if ZKP validation or cryptographic integrity check fails
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}