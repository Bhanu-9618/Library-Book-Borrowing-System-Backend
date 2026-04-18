package edu.icet.ecom.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils = new JwtUtils();

    @Test
    void generateAndValidateToken_ShouldWork() {
        // Set up the secret and expiration via Reflection because they are @Value fields
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKeyForTestingPurposesThatIsLongEnough");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);

        String email = "test@user.com";

        // Generate
        String token = jwtUtils.generateToken(email);
        
        // Assert
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals(email, jwtUtils.getUsernameFromJwtToken(token));
    }
    
    @Test
    void validateJwtToken_WhenInvalid_ShouldReturnFalse() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKeyForTestingPurposesThatIsLongEnough");
        
        assertFalse(jwtUtils.validateJwtToken("invalid.token.here"));
    }
}
