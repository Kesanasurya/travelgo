package com.travelgo.service;
import com.travelgo.dto.BookingRequest;
import com.travelgo.entity.*;
import com.travelgo.exception.ResourceNotFoundException;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service @RequiredArgsConstructor
public class BookingService {
 private final BookingRepository bookings; private final UserRepository users; private final TravelPackageRepository packages;
 public Booking create(String email,BookingRequest r){var u=users.findByEmailIgnoreCase(email).orElseThrow();var p=packages.findById(r.packageId()).orElseThrow(()->new ResourceNotFoundException("Package not found"));var b=new Booking();b.setUser(u);b.setTravelPackage(p);b.setRoomType(r.roomType());b.setTravelDate(r.travelDate());b.setGuests(r.guests());b.setTotalAmount(p.getPrice().multiply(java.math.BigDecimal.valueOf(r.guests())));b.setStatus(Booking.Status.CONFIRMED);return bookings.save(b);}
 public List<Booking> mine(String email){return bookings.findByUserIdOrderByCreatedAtDesc(users.findByEmailIgnoreCase(email).orElseThrow().getId());}
 public Booking oneForUser(Long id,String email){var b=bookings.findById(id).orElseThrow(()->new ResourceNotFoundException("Booking not found"));if(!b.getUser().getEmail().equalsIgnoreCase(email))throw new org.springframework.security.access.AccessDeniedException("Booking does not belong to user");return b;}
 public Booking cancel(Long id,String email){var b=oneForUser(id,email);if(b.getStatus()==Booking.Status.COMPLETED)throw new IllegalStateException("Completed bookings cannot be cancelled");b.setStatus(Booking.Status.CANCELLED);return bookings.save(b);}
 public List<Booking> all(){return bookings.findAll();}
}
