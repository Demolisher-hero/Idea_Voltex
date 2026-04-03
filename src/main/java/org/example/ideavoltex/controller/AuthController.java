package org.example.ideavoltex.controller;

import org.example.ideavoltex.crypto.BlindIndexer;
import org.example.ideavoltex.model.User;
import org.example.ideavoltex.repository.UserRepository; // Added Import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // Added Import
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller // This identifies the class as a web controller
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Injects your MongoDB repository

    @PostMapping("/login")
    public String handleLogin(@RequestParam String userId, @RequestParam String password) {
        // 1. Generate the blind index (Deterministic Hashing)
        String searchIndex = BlindIndexer.generateBlindIndex(userId);

        // 2. Search MongoDB for the blinded ID
        Optional<User> userOpt = userRepository.findByBlindIndex(searchIndex);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 3. Verify the Argon2 password hash (Non-deterministic)
            // We convert the password String to a char array for Argon2 security
            if (BlindIndexer.verifyPassword(user.getPasswordHash(), password.toCharArray())) {
                return "redirect:/dashboard"; // Redirects to your dashboard.html
            }
        }

        return "login?error=true"; // Returns to login with an error flag
    }

    @PostMapping("/register")
    @ResponseBody // This tells Spring to send a message back instead of redirecting
    public String handleRegistration(@RequestParam String userId, @RequestParam String password) {
        String blindedId = BlindIndexer.generateBlindIndex(userId);
        String hashedPw = BlindIndexer.hashPassword(password.toCharArray());

        User newUser = new User();
        newUser.setBlindIndex(blindedId);
        newUser.setPasswordHash(hashedPw);

        userRepository.save(newUser);


        return "Success! Blind Index " + blindedId + " saved to Mumbai Cluster.";
    }
}