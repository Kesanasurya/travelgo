package com.travelgo.controller;

import com.travelgo.entity.Booking;
import com.travelgo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminBookingController {
    private final BookingService service;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> all() {
        return service.all();
    }
}
