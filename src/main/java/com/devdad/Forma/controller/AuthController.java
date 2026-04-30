package com.devdad.Forma.controller;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.dto.user.LoginResponse;
import com.devdad.Forma.model.dto.user.UserLoginResponse;
import com.devdad.Forma.model.dto.user.UserRegisterResponse;
import com.devdad.Forma.model.dto.user.UserResponse;
import com.devdad.Forma.model.dto.user.UserUpdateResponse;
import com.devdad.Forma.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthUser() {
        return ResponseEntity.ok(authService.getAuthenticatedUser());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegisterResponse request,
            HttpServletResponse response) {
        User user = authService.registerUser(request, response);
        return new ResponseEntity<>(toUserResponse(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginResponse userLoginResponse, HttpServletResponse response) {
        LoginResponse loginResponseDto = authService.validateAndAuthenticateUserForLogin(userLoginResponse,
                response);
        System.out.println("Login Response: " + loginResponseDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserUpdateResponse userDetails) {
        return ResponseEntity.ok(authService.updateUserProfile(userDetails));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getProfilePicture());
    }
}
