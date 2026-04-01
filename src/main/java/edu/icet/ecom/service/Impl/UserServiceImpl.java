package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.icet.ecom.model.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            UserEntity admin = new UserEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Admin");
            admin.setPhone("0000000000");
            admin.setAddress("System");
            admin.setMembershipdate(LocalDate.now());
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            userRepository.save(admin);
        }
    }

    @Override
    public void save(UserDto user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            UserEntity userEntity = mapper.map(user, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setIsActive(true); // Default to active
            if (user.getRole() == null) {
                userEntity.setRole(Role.USER);
            } else {
                userEntity.setRole(user.getRole());
            }
            userRepository.save(userEntity);
        }
    }

    @Override
    public List<UserDto> getAllDetails() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> users = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            users.add(mapper.map(userEntity, UserDto.class));
        }
        return users;
    }

    @Transactional
    @Override
    public void updateUser(UserDto user) {
        Optional<UserEntity> existingUser = userRepository.findById(user.getId());

        if (existingUser.isPresent()) {
            UserEntity currentEntity = existingUser.get();

            boolean emailExistsInOthers = userRepository.existsByEmail(user.getEmail())
                    && !currentEntity.getEmail().equals(user.getEmail());

            if (!emailExistsInOthers) {
                currentEntity.setName(user.getName());
                currentEntity.setEmail(user.getEmail());
                currentEntity.setPhone(user.getPhone());
                currentEntity.setAddress(user.getAddress());
                currentEntity.setMembershipdate(user.getMembershipdate());
                currentEntity.setRole(user.getRole());
                
                if (user.getIsActive() != null) {
                    currentEntity.setIsActive(user.getIsActive());
                }

                // Only update password if a new one is provided (it's not blank or the default 'unknown')
                if (user.getPassword() != null && !user.getPassword().isBlank() && !user.getPassword().equals("********")) {
                    currentEntity.setPassword(passwordEncoder.encode(user.getPassword()));
                }

                userRepository.save(currentEntity);
            }
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserEntity entity = user.get();
            entity.setIsActive(false); // Soft Delete
            userRepository.save(entity);
        }
    }

    @Override
    public List<UserDto> searchUsers(String term) {
        return userRepository.searchByTerm(term).stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(entity -> mapper.map(entity, UserDto.class))
                .orElse(null);
    }

    @Override
    public long getTotalCount() {
        return userRepository.count();
    }
}