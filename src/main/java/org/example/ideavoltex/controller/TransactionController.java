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

@Controller
@RequestMapping("/api/transactions") // Base path for everything in this controller
public class TransactionController {

    @Autowired
    private TransactionService service;

    // URL: http://localhost:8080/api/transactions/dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Transaction> transactions = service.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "dashboard";
    }

    // URL: http://localhost:8080/api/transactions/send
    @PostMapping("/send") // This now correctly appends to the base path
    @ResponseBody
    public ResponseEntity<?> send(@RequestBody Map<String, String> request) {
        try {
            Transaction result = service.sendMoneySecurely(
                    request.get("sender"),
                    request.get("receiver"),
                    request.get("amount"),
                    request.get("proof")
            );
            return ResponseEntity.ok(result);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            // Keep an eye on the console for MongoDB SSL errors here
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}