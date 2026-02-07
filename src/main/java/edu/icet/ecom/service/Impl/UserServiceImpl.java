package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    @Override
    public void save(UserDto user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(mapper.map(user, UserEntity.class));
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
                currentEntity.setEmail(user.getEmail());
                currentEntity.setPhone(user.getPhone());
                currentEntity.setAddress(user.getAddress());

                userRepository.save(currentEntity);
            }
        }
    }
    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> searchUsers(String term) {
        return userRepository.searchByTerm(term).stream()
                .map(entity -> mapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }
}