package com.travelgo.service;

import com.travelgo.dto.*;
import com.travelgo.entity.User;
import com.travelgo.repository.UserRepository;
import com.travelgo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt; private final AuthenticationManager authenticationManager;
    public LoginResponse register(RegisterRequest request){ if(users.existsByEmailIgnoreCase(request.email())) throw new IllegalArgumentException("Email is already registered"); User user=new User(null,request.name(),request.email().toLowerCase(),encoder.encode(request.password()), User.Role.USER,null); users.save(user); return new LoginResponse(jwt.generate(user), LoginResponse.UserResponse.from(user)); }
    public LoginResponse login(LoginRequest request){ authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password())); User user=users.findByEmailIgnoreCase(request.email()).orElseThrow(); return new LoginResponse(jwt.generate(user),LoginResponse.UserResponse.from(user)); }
    public LoginResponse.UserResponse me(String email){ return LoginResponse.UserResponse.from(users.findByEmailIgnoreCase(email).orElseThrow()); }
}
