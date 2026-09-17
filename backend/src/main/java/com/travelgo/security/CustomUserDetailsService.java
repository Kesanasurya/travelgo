package com.travelgo.security;
import com.travelgo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { var user=users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("User not found")); return User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build(); }
}
