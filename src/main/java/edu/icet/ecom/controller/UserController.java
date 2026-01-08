package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
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

    @PutMapping("/update")
    public void update(@RequestBody UserDto user){
        userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/search/{term}")
    public List<UserDto> search(@PathVariable String term) {
        return userService.searchUsers(term);
    }
}