package edu.icet.ecom.service.Impl;

import edu.icet.ecom.model.dto.LoginDto;
import edu.icet.ecom.model.entity.AdminEntity;
import edu.icet.ecom.repository.AdminRepository;
import edu.icet.ecom.service.LoginService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    public boolean authenticate(LoginDto loginDto) {
        return adminRepository.findByEmailAndPassword(
                loginDto.getEmail(),
                loginDto.getPassword()
        ).isPresent();
    }
    @PostConstruct
    public void initAdmin() {
        if (adminRepository.count() == 0) {
            AdminEntity admin = new AdminEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin123");
            adminRepository.save(admin);
        }
    }
}