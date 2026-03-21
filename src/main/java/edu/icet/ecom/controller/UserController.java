package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto save(@RequestBody UserDto user){
        userService.save(user);
        return user;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getDetails(){
        return userService.getAllDetails();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@RequestBody UserDto user){
        userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}