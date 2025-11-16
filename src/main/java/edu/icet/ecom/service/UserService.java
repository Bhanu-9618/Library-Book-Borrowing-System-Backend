package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.UserDto;

import java.util.List;

public interface UserService {
    void save(UserDto user);
    List<UserDto> getAllDetails();
}
