package by.beltam.practice.service;

import by.beltam.practice.dto.AuthResponse;
import by.beltam.practice.dto.UserRequest;
import by.beltam.practice.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AuthService {

    User register(UserRequest request);
    void promoteToAdmin(UUID userId);
    AuthResponse login(UserRequest request);
}