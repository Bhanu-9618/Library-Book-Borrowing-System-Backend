package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public void save(UserDto user){
        UserEntity userEntity = new UserEntity(
                user.getUserid(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getMembershipdate()
        );
        userRepository.save(userEntity);
    }

    public List<UserDto> getAllDetails() {

        List<UserEntity> userEntities =  userRepository.findAll();
        List<UserDto> users = new ArrayList<>();

        for (UserEntity userEntity : userEntities){
            users.add( new UserDto(
                            userEntity.getUserid(),
                            userEntity.getName(),
                            userEntity.getEmail(),
                            userEntity.getPhone(),
                            userEntity.getAddress(),
                            userEntity.getMembershipdate()
                    )
            );
        }
        return users;
    }
}
