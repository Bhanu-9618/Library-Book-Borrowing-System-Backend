package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.model.enums.Role;
import edu.icet.ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void initAdmin_WhenNotExists_ShouldCreateAdmin() {
        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_pass");

        userService.initAdmin();

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void save_ShouldEncryptPasswordAndSave() {
        UserDto dto = new UserDto();
        dto.setEmail("test@test.com");
        dto.setPassword("raw_password");

        UserEntity entity = new UserEntity();
        
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(mapper.map(dto, UserEntity.class)).thenReturn(entity);
        when(passwordEncoder.encode("raw_password")).thenReturn("encrypted_password");

        userService.save(dto);

        assertEquals("encrypted_password", entity.getPassword());
        assertTrue(entity.getIsActive());
        verify(userRepository, times(1)).save(entity);
    }

    @Test
    void updateUser_ShouldOnlyUpdatePasswordIfProvided() {
        UserDto updateDto = new UserDto();
        updateDto.setId(1L);
        updateDto.setEmail("user@test.com");
        updateDto.setPassword(""); // Empty password should NOT trigger encryption

        UserEntity existing = new UserEntity();
        existing.setEmail("user@test.com");
        existing.setPassword("old_encrypted_pass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);

        userService.updateUser(updateDto);

        assertEquals("old_encrypted_pass", existing.getPassword()); // Should not change
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void deleteUser_ShouldPerformSoftDelete() {
        UserEntity user = new UserEntity();
        user.setIsActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertFalse(user.getIsActive()); // Soft delete check
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllDetails_ShouldReturnPaginatedMap() {
        Page<UserEntity> page = new PageImpl<>(Collections.singletonList(new UserEntity()));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.map(any(), eq(UserDto.class))).thenReturn(new UserDto());

        Map<String, Object> response = userService.getAllDetails(0, 10);

        assertNotNull(response);
        assertEquals(1, ((java.util.List)response.get("users")).size());
    }
}
