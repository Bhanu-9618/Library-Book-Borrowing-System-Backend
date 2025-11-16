package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    public UserDto save(@RequestBody UserDto user){
        userService.save(user);
        return user;
    }

    @GetMapping("/all")
    public List<UserDto> getDetails(){
        return userService.getAllDetails();
    }
}
