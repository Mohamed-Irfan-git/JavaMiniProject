package utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    // Keep secret in one place
    private static final String JWT_SECRET = "a9!B3vK7#hJ2pXz8qL0mT5wRf6Y1uS4d";

    // Validate token
    public boolean validateToken(String token) {
        try {
            getAllClaims(token); // will throw if invalid
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extract username
    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }

    // Extract role
    public String getRoleFromToken(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    // Central method to parse token
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}