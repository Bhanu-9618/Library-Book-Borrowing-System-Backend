package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.LoginDto;
import edu.icet.ecom.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public boolean login(@RequestBody LoginDto loginDto) {
        return loginService.authenticate(loginDto);
    }
}