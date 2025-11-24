package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    ModelMapper mapper = new ModelMapper();

    @Autowired
    UserRepository userRepository;

    public void save(UserDto user){
        userRepository.save(mapper.map(user , UserEntity.class));
    }

    public List<UserDto> getAllDetails() {

        List<UserEntity> userEntities =  userRepository.findAll();
        List<UserDto> users = new ArrayList<>();

        for (UserEntity userEntity : userEntities){
            users.add(mapper.map(userEntity ,UserDto.class));
        }
        return users;
    }
}
