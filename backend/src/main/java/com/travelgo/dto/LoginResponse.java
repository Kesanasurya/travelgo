package com.travelgo.dto;
import com.travelgo.entity.User;
public record LoginResponse(String token, UserResponse user) { public record UserResponse(Long id, String name, String email, User.Role role) { public static UserResponse from(User user){ return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole()); } } }
