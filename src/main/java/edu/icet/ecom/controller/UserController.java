package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.service.UserService;
import edu.icet.ecom.util.StandardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<StandardResponse> save(@RequestBody UserDto user){
        userService.save(user);
        return new ResponseEntity<>(
                new StandardResponse(201, "User Saved Successfully", user),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> getDetails(){
        List<UserDto> allUsers = userService.getAllDetails();
        return new ResponseEntity<>(
                new StandardResponse(200, "Success", allUsers),
                HttpStatus.OK
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> updateUser(@RequestBody UserDto user){
        userService.updateUser(user);
        return new ResponseEntity<>(
                new StandardResponse(200, "User Updated Successfully", null),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(
                new StandardResponse(200, "User Deleted Successfully", null),
                HttpStatus.OK
        );
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StandardResponse> searchUserById(@PathVariable Long id){
        UserDto user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(
                    new StandardResponse(200, "Success", user),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new StandardResponse(404, "User Not Found", null),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}