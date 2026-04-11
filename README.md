# IDEA_VOLTEX: Privacy-Preserving Digital Transaction System

A high-performance, zero-trust security architecture designed for encrypted storage and secure data handling.

## 🛡️ Security Pillars
- **Ascon-128 Standard:** Lightweight authenticated encryption (AEAD) for high-speed, secure data processing.
- **Deterministic Blind Indexing:** Decouples user identity from stored data, ensuring privacy even in the event of a database breach.
- **Argon2 Integration:** Memory-hard hashing function utilized for secure authentication and resistance against GPU-based attacks.

## 🚀 Tech Stack
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.4.3
- **Database:** MongoDB Atlas (Encrypted at Rest)
- **Security:** Integrated OWASP Dependency Scanning & Zero-Knowledge Proof (ZKP) logic.

## 🛰️ Architecture Overview
Designed with modularity for future integration with the **VLEO Sentinel Core (HD-STDR)** for real-time disaster surveillance and secure edge AI processing.

## 🛠️ Setup & Installation
1. Clone the repository.
2. Configure `application.properties` with your MongoDB Atlas URI.
3. Run `./mvnw spring-boot:run`.
