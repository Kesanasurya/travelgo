package com.travelgo.config;

import com.travelgo.entity.*;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Demo data for presentations: Indian-named customers, an extra admin, more hotels and sample bookings.
 * Runs after {@link DataInitializer}, never creates duplicates, and is switched off with SEED_DEMO_DATA=false.
 */
@Component
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner, Ordered {
    public static final String DEMO_PASSWORD = "Demo@123";
    public static final String DEMO_ADMIN_EMAIL = "lakshmi.rao@travelgo.dev";
    public static final String DEMO_ADMIN_PASSWORD = "Admin@123";

    private final UserRepository users;
    private final DestinationRepository destinations;
    private final TravelPackageRepository packages;
    private final HotelRepository hotels;
    private final BookingRepository bookings;
    private final PasswordEncoder encoder;

    @Value("${app.seed-demo-data:true}")
    private boolean enabled = true;

    record DemoUser(String name, String email) { }

    static final List<DemoUser> CUSTOMERS = List.of(
        new DemoUser("Aarav Sharma", "aarav.sharma@travelgo.dev"),
        new DemoUser("Priya Reddy", "priya.reddy@travelgo.dev"),
        new DemoUser("Rohan Verma", "rohan.verma@travelgo.dev"),
        new DemoUser("Ananya Iyer", "ananya.iyer@travelgo.dev"),
        new DemoUser("Karthik Nair", "karthik.nair@travelgo.dev"),
        new DemoUser("Sneha Patel", "sneha.patel@travelgo.dev"));

    /** email, package name, room type, days from today, guests, status */
    record DemoBooking(String email, String packageName, String roomType, int daysFromToday, int guests,
                       Booking.Status status) { }

    static final List<DemoBooking> BOOKINGS = List.of(
        new DemoBooking("aarav.sharma@travelgo.dev", "Goa Beach Explorer", "Deluxe", 21, 2, Booking.Status.CONFIRMED),
        new DemoBooking("aarav.sharma@travelgo.dev", "Dubai Luxury Escape", "Suite", -40, 2, Booking.Status.COMPLETED),
        new DemoBooking("priya.reddy@travelgo.dev", "Bali Island Adventure", "Standard", 35, 3, Booking.Status.CONFIRMED),
        new DemoBooking("priya.reddy@travelgo.dev", "Maldives Island Retreat", "Suite", 60, 2, Booking.Status.PENDING),
        new DemoBooking("rohan.verma@travelgo.dev", "Goa Beach Explorer", "Standard", 14, 4, Booking.Status.CONFIRMED),
        new DemoBooking("rohan.verma@travelgo.dev", "Bali Island Adventure", "Deluxe", 28, 2, Booking.Status.CANCELLED),
        new DemoBooking("ananya.iyer@travelgo.dev", "Maldives Island Retreat", "Deluxe", 45, 2, Booking.Status.CONFIRMED),
        new DemoBooking("karthik.nair@travelgo.dev", "Dubai Luxury Escape", "Deluxe", 30, 1, Booking.Status.PENDING),
        new DemoBooking("karthik.nair@travelgo.dev", "Goa Beach Explorer", "Standard", -75, 5, Booking.Status.COMPLETED),
        new DemoBooking("sneha.patel@travelgo.dev", "Bali Island Adventure", "Suite", 50, 2, Booking.Status.CONFIRMED));

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE; // after DataInitializer (ordered first)
    }

    @Override
    public void run(String... args) {
        if (!enabled) return;
        seedUsers();
        seedHotels();
        seedBookings();
    }

    void seedUsers() {
        if (!users.existsByEmailIgnoreCase(DEMO_ADMIN_EMAIL)) {
            users.save(new User(null, "Lakshmi Rao", DEMO_ADMIN_EMAIL, encoder.encode(DEMO_ADMIN_PASSWORD),
                User.Role.ADMIN, null));
        }
        for (DemoUser customer : CUSTOMERS) {
            if (!users.existsByEmailIgnoreCase(customer.email())) {
                users.save(new User(null, customer.name(), customer.email(), encoder.encode(DEMO_PASSWORD),
                    User.Role.USER, null));
            }
        }
    }

    void seedHotels() {
        Map<String, Destination> byName = destinations.findAll().stream()
            .collect(Collectors.toMap(d -> d.getName().toLowerCase(Locale.ROOT), Function.identity(), (a, b) -> a));
        Set<String> existing = hotels.findAll().stream()
            .map(h -> h.getName().toLowerCase(Locale.ROOT)).collect(Collectors.toSet());
        String[][] data = {
            {"Desert Pearl Resort", "Dubai", "Resort on the edge of the desert with dune safaris on request", "8600", "4.6",
                "Al Marmoom, Dubai", "Pool, desert safari desk, free Wi-Fi, breakfast",
                "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=900&q=80"},
            {"Coral Lagoon Villas", "Maldives", "Overwater villas above a quiet coral lagoon", "18500", "4.9",
                "South Male Atoll", "Private deck, snorkelling gear, spa, all meals",
                "https://images.unsplash.com/photo-1573843981267-be1999ff37cd?auto=format&fit=crop&w=900&q=80"},
            {"Seine Riverside Hotel", "Paris", "Boutique hotel a short walk from the river and museums", "14200", "4.5",
                "Saint-Germain, Paris", "Free Wi-Fi, breakfast, concierge, city view",
                "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=900&q=80"},
            {"Thames View Suites", "London", "Modern suites with views over the Thames", "16800", "4.6",
                "South Bank, London", "Gym, free Wi-Fi, breakfast, 24h reception",
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?auto=format&fit=crop&w=900&q=80"}
        };
        for (String[] h : data) {
            Destination destination = byName.get(h[1].toLowerCase(Locale.ROOT));
            if (destination == null || !existing.add(h[0].toLowerCase(Locale.ROOT))) continue;
            hotels.save(new Hotel(null, h[0], destination, h[2], new BigDecimal(h[3]), new BigDecimal(h[4]),
                h[7], h[5], h[6], null, null));
        }
    }

    void seedBookings() {
        Map<String, TravelPackage> packagesByName = packages.findAll().stream()
            .collect(Collectors.toMap(p -> p.getName().toLowerCase(Locale.ROOT), Function.identity(), (a, b) -> a));
        Set<String> customersWithBookings = new HashSet<>();
        for (DemoUser customer : CUSTOMERS) {
            users.findByEmailIgnoreCase(customer.email()).ifPresent(user -> {
                if (!bookings.findByUserIdOrderByCreatedAtDesc(user.getId()).isEmpty()) {
                    customersWithBookings.add(customer.email());
                }
            });
        }
        for (DemoBooking demo : BOOKINGS) {
            if (customersWithBookings.contains(demo.email())) continue; // seed each customer's bookings only once
            TravelPackage travelPackage = packagesByName.get(demo.packageName().toLowerCase(Locale.ROOT));
            Optional<User> user = users.findByEmailIgnoreCase(demo.email());
            if (travelPackage == null || user.isEmpty()) continue;
            Booking booking = new Booking();
            booking.setUser(user.get());
            booking.setTravelPackage(travelPackage);
            booking.setRoomType(demo.roomType());
            booking.setTravelDate(LocalDate.now().plusDays(demo.daysFromToday()));
            booking.setGuests(demo.guests());
            booking.setTotalAmount(travelPackage.getPrice().multiply(BigDecimal.valueOf(demo.guests())));
            booking.setStatus(demo.status());
            bookings.save(booking);
        }
    }
}
