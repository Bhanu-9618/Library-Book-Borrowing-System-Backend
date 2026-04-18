package edu.icet.ecom.security;

import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_WhenExists_ShouldReturnUserDetails() {
        UserEntity user = new UserEntity();
        user.setEmail("test@test.com");
        user.setPassword("pass");
        user.setIsActive(true);
        user.setRole(edu.icet.ecom.model.enums.Role.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_WhenNotExists_ShouldThrowException() {
        when(userRepository.findByEmail("none@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("none@test.com")
        );
    }
}
