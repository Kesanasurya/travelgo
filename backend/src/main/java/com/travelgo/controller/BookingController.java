package com.travelgo.controller;

import com.travelgo.dto.BookingRequest;
import com.travelgo.entity.Booking;
import com.travelgo.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public Booking create(Authentication authentication, @Valid @RequestBody BookingRequest request) {
        return service.create(authentication.getName(), request);
    }

    @GetMapping("/my")
    public List<Booking> mine(Authentication authentication) {
        return service.mine(authentication.getName());
    }

    @GetMapping("/{id}")
    public Booking one(Authentication authentication, @PathVariable Long id) {
        return service.oneForUser(id, authentication.getName());
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(Authentication authentication, @PathVariable Long id) {
        return service.cancel(id, authentication.getName());
    }
}
