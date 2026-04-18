package edu.icet.ecom.security;

import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void build_ShouldMapAllFields() {
        UserEntity entity = new UserEntity();
        entity.setId(10L);
        entity.setName("Tester");
        entity.setEmail("test@test.com");
        entity.setPassword("hash");
        entity.setRole(Role.ADMIN);
        entity.setIsActive(true);

        UserDetailsImpl userDetails = UserDetailsImpl.build(entity);

        assertEquals(10L, userDetails.getId());
        assertEquals("Tester", userDetails.getName());
        assertEquals("test@test.com", userDetails.getUsername());
        assertTrue(userDetails.isEnabled());
        
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void booleanMethods_ShouldReturnDefaults() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "N", "E", "P", Role.USER, true);
        
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }
}
