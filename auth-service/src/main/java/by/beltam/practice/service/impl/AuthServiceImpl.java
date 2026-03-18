package by.beltam.practice.service.impl;

import by.beltam.practice.common.exception.ConflictException;
import by.beltam.practice.common.exception.NotFoundException;
import by.beltam.practice.dto.AuthResponse;
import by.beltam.practice.dto.UserRequest;
import by.beltam.practice.entity.User;
import by.beltam.practice.entity.UserRole;
import by.beltam.practice.exception.WrongCredentialsException;
import by.beltam.practice.repository.UserRepository;
import by.beltam.practice.security.JwtProvider;
import by.beltam.practice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public User register(UserRequest request) {
        if (userRepository.existsByName(request.username())) {
            throw new ConflictException("user already exist");
        }

        User user = new User();
        user.setName(request.username());

        String hashedPassword = passwordEncoder.encode(request.password());
        user.setPassword(hashedPassword);

        user.setRole(UserRole.USER);
        user.setTokenVersion(1L);

        return userRepository.save(user);
    }

    @Transactional
    public void promoteToAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new NotFoundException("user"));

        user.setRole(UserRole.ADMIN);
        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }

    public AuthResponse login(UserRequest request) {
        User user = userRepository.getUserByName(request.username())
                .orElseThrow(() ->new NotFoundException("user"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new WrongCredentialsException();
        }

        String token = jwtProvider.generateToken(user);

        return new AuthResponse(token, "Bearer", user.getId());
    }
}