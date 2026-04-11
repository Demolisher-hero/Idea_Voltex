package org.example.ideavoltex.controller;

import jakarta.servlet.http.HttpSession;
import org.example.ideavoltex.crypto.AsconUtil;
import org.example.ideavoltex.crypto.BlindIndexer;
import org.example.ideavoltex.model.User;
import org.example.ideavoltex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

/**
 * Handles user authentication, registration, and dashboard access.
 * Implements searchable encryption via Blind Indexing and lightweight
 * authenticated encryption using the Ascon128v11 cipher.
 */
@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    /**
     * Authenticates user by generating a deterministic blind index of the ID
     * to perform a database lookup without decrypting entire columns.
     */
    @PostMapping("/login")
    public String handleLogin(@RequestParam String userId,
                              @RequestParam String password,
                              HttpSession session) {

        // Generate a non-reversible hash of the ID for database searching
        String searchIndex = BlindIndexer.generateBlindIndex(userId);
        Optional<User> userOpt = userRepository.findByBlindIndex(searchIndex);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Verify provided password against the Argon2/BCrypt hash
            if (BlindIndexer.verifyPassword(user.getPasswordHash(), password.toCharArray())) {
                session.setAttribute("loggedInUserEmail", user.getEmail());
                return "redirect:/dashboard";
            }
        }
        return "redirect:/login?error=true";
    }

    /**
     * Retrieves and decrypts user profile data for display.
     * Uses Ascon decryption to convert ciphertext from the database into plaintext for the UI.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        String email = (String) session.getAttribute("loggedInUserEmail");

        if (email == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                // Perform decryption of sensitive fields using Ascon algorithm
                user.setName(AsconUtil.decrypt(user.getName()));
                user.setEmail(AsconUtil.decrypt(user.getEmail()));
            } catch (Exception e) {
                // Log decryption failures which may indicate key mismatch or data corruption
                System.err.println("Cryptographic error during profile decryption: " + e.getMessage());
            }

            model.addAttribute("user", user);
            return "dashboard";
        }

        return "redirect:/login";
    }

    /**
     * Registers a new user by encrypting PII (Personally Identifiable Information)
     * and generating a searchable blind index for the email address.
     */
    @PostMapping("/signup")
    public String handleRegistration(@RequestParam String name,
                                     @RequestParam String email,
                                     @RequestParam String password) {
        try {
            // Encrypt data before persistence (Encryption-at-Rest)
            String encryptedEmail = AsconUtil.encrypt(email);
            String encryptedName = AsconUtil.encrypt(name);

            // Create a deterministic index to allow future lookups on the encrypted email
            String blindedId = BlindIndexer.generateBlindIndex(email);

            User newUser = new User();
            newUser.setBlindIndex(blindedId);
            newUser.setEmail(encryptedEmail);
            newUser.setName(encryptedName);
            newUser.setPasswordHash(BlindIndexer.hashPassword(password.toCharArray()));

            userRepository.save(newUser);
            return "redirect:/login?registered=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/signup?error=encryption_failed";
        }
    }
}