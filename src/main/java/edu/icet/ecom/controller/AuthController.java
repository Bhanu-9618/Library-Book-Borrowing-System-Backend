package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.LoginDto;
import edu.icet.ecom.model.dto.UserDto;
import edu.icet.ecom.security.JwtUtils;
import edu.icet.ecom.security.UserDetailsImpl;
import edu.icet.ecom.service.EmailService;
import edu.icet.ecom.service.UserService;
import edu.icet.ecom.util.StandardResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<StandardResponse> registerUser(@Valid @RequestBody UserDto userDto) {
        userDto.setRole(Role.USER);
        userService.save(userDto);

        String subject = "Welcome to Enterprise Book Borrowing System";
        String message = "Hello " + userDto.getName() + ",\n\n" +
                "Your account has been successfully created. Welcome to the Enterprise Book Borrowing System!\n" +
                "You can now log in and start borrowing your favorite books.\n\n" +
                "Best Regards,\n" +
                "Library Management Team";

        emailService.sendSimpleMessage(userDto.getEmail(), subject, message);

        return new ResponseEntity<>(
                new StandardResponse(201, "User registered successfully!", null),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails.getEmail());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", jwt);
            responseData.put("id", userDetails.getId());
            responseData.put("name", userDetails.getName());
            responseData.put("email", userDetails.getEmail());
            responseData.put("role", userDetails.getRole());

            return new ResponseEntity<>(
                    new StandardResponse(200, "Login Successful", responseData),
                    HttpStatus.OK
            );
        } catch (org.springframework.security.authentication.DisabledException e) {
            return new ResponseEntity<>(
                    new StandardResponse(403, "Account is disabled. Please contact admin.", null),
                    HttpStatus.FORBIDDEN
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            return new ResponseEntity<>(
                    new StandardResponse(401, "Invalid email or password", null),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}