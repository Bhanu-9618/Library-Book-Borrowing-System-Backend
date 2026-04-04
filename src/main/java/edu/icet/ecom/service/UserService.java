package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    void save(UserDto user);
    Map<String, Object> getAllDetails(int page, int size);
    void updateUser(UserDto user);
    void deleteUser(Long id);
    Map<String, Object> searchUsers(String term, int page, int size);
    UserDto getUserById(Long id);
    long getTotalCount();
}