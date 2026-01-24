package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.LoginDto;

public interface LoginService {
    boolean authenticate(LoginDto loginDto);
}
