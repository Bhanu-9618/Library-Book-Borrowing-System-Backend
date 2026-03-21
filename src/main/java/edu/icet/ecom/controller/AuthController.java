package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.LoginDto;
import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.security.JwtUtils;
import edu.icet.ecom.security.UserDetailsImpl;
import edu.icet.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edu.icet.ecom.model.enums.Role;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String registerUser(@RequestBody UserDto userDto) {
        userDto.setRole(Role.USER);
        userService.save(userDto);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("id", userDetails.getId());
        response.put("email", userDetails.getEmail());
        response.put("role", userDetails.getRole());

        return ResponseEntity.ok(response);
    }
}