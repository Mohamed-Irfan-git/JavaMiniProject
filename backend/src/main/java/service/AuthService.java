package service;

import dao.UserDAO;
import model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Authenticate user and return JWT token
    public AuthResult authenticate(String username, String password) {
        User user = userDAO.findByUsernameAndPassword(username, password);
        if (user != null) {
            String token = generateToken(user);  // token generated here
            return new AuthResult(true, user.getUsername(), user.getRole(), "Login successful", token);
        }
        return new AuthResult(false, null, null, "Invalid username or password", null);
    }

    // Generate JWT token (private, only called inside authenticate)
    private String generateToken(User user) {


        String jwtSecret = "a9!B3vK7#hJ2pXz8qL0mT5wRf6Y1uS4d";
        long jwtExpirationMs = 24 * 60 * 60 * 1000;
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // AuthResult wrapper class
    public static class AuthResult {
        private final boolean success;
        private final String username;
        private final String role;
        private final String message;
        private final String token;

        public AuthResult(boolean success, String username, String role, String message, String token) {
            this.success = success;
            this.username = username;
            this.role = role;
            this.message = message;
            this.token = token;
        }

        public boolean isSuccess() { return success; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getMessage() { return message; }
        public String getToken() { return token; }
    }
}