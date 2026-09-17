package com.travelgo.controller;
import com.travelgo.dto.*;
import com.travelgo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController { private final AuthService auth;
 @PostMapping("/register") public LoginResponse register(@Valid @RequestBody RegisterRequest r){return auth.register(r);}
 @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginRequest r){return auth.login(r);}
 @GetMapping("/me") public LoginResponse.UserResponse me(Authentication a){return auth.me(a.getName());}
 @GetMapping("/profile") public LoginResponse.UserResponse profile(Authentication a){return auth.me(a.getName());}
}
