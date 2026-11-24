package com.travelgo.config;

import com.travelgo.entity.*;
import com.travelgo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DemoDataSeederTest {
    @Mock UserRepository users;
    @Mock DestinationRepository destinations;
    @Mock TravelPackageRepository packages;
    @Mock HotelRepository hotels;
    @Mock BookingRepository bookings;
    @Mock PasswordEncoder encoder;

    DemoDataSeeder seeder;
    final Map<String, User> savedUsers = new HashMap<>();

    @BeforeEach
    void setUp() {
        seeder = new DemoDataSeeder(users, destinations, packages, hotels, bookings, encoder);
        when(encoder.encode(anyString())).thenAnswer(i -> "hashed:" + i.getArgument(0));
        when(users.existsByEmailIgnoreCase(anyString()))
            .thenAnswer(i -> savedUsers.containsKey(((String) i.getArgument(0)).toLowerCase()));
        when(users.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId((long) savedUsers.size() + 10);
            savedUsers.put(u.getEmail().toLowerCase(), u);
            return u;
        });
        when(users.findByEmailIgnoreCase(anyString()))
            .thenAnswer(i -> Optional.ofNullable(savedUsers.get(((String) i.getArgument(0)).toLowerCase())));
        when(destinations.findAll()).thenReturn(destinations("Goa", "Dubai", "Bali", "Maldives", "Paris", "London"));
        when(packages.findAll()).thenReturn(List.of(
            pkg("Goa Beach Explorer", "15000"), pkg("Dubai Luxury Escape", "42000"),
            pkg("Bali Island Adventure", "35000"), pkg("Maldives Island Retreat", "52000")));
        when(hotels.findAll()).thenReturn(List.of());
        when(bookings.findByUserIdOrderByCreatedAtDesc(anyLong())).thenReturn(List.of());
    }

    @Test
    void createsIndianDemoCustomersAndAnAdmin() {
        seeder.run();

        assertThat(savedUsers.values()).extracting(User::getName)
            .contains("Aarav Sharma", "Priya Reddy", "Rohan Verma", "Ananya Iyer", "Karthik Nair", "Sneha Patel",
                "Lakshmi Rao");
        User admin = savedUsers.get(DemoDataSeeder.DEMO_ADMIN_EMAIL);
        assertThat(admin.getRole()).isEqualTo(User.Role.ADMIN);
        assertThat(admin.getPassword()).isEqualTo("hashed:" + DemoDataSeeder.DEMO_ADMIN_PASSWORD);
        assertThat(savedUsers.get("priya.reddy@travelgo.dev").getRole()).isEqualTo(User.Role.USER);
    }

    @Test
    void createsHotelsForTheOtherDestinations() {
        seeder.run();

        ArgumentCaptor<Hotel> saved = ArgumentCaptor.forClass(Hotel.class);
        verify(hotels, times(4)).save(saved.capture());
        assertThat(saved.getAllValues()).extracting(h -> h.getDestination().getName())
            .containsExactly("Dubai", "Maldives", "Paris", "London");
    }

    @Test
    void createsBookingsWithPricesDatesAndStatuses() {
        seeder.run();

        ArgumentCaptor<Booking> saved = ArgumentCaptor.forClass(Booking.class);
        verify(bookings, times(DemoDataSeeder.BOOKINGS.size())).save(saved.capture());
        Booking first = saved.getAllValues().get(0); // Aarav, Goa, 2 guests, 21 days ahead
        assertThat(first.getUser().getName()).isEqualTo("Aarav Sharma");
        assertThat(first.getTotalAmount()).isEqualByComparingTo("30000");
        assertThat(first.getTravelDate()).isEqualTo(LocalDate.now().plusDays(21));
        assertThat(saved.getAllValues()).extracting(Booking::getStatus)
            .contains(Booking.Status.CONFIRMED, Booking.Status.PENDING, Booking.Status.CANCELLED, Booking.Status.COMPLETED);
        assertThat(first.customerName()).isEqualTo("Aarav Sharma");
    }

    @Test
    void secondRunCreatesNothingNew() {
        Set<String> hotelNames = new HashSet<>();
        when(hotels.save(any(Hotel.class))).thenAnswer(i -> { hotelNames.add(((Hotel) i.getArgument(0)).getName()); return i.getArgument(0); });
        seeder.run();
        int usersAfterFirst = savedUsers.size();

        when(hotels.findAll()).thenAnswer(i -> hotelNames.stream().map(n -> { Hotel h = new Hotel(); h.setName(n); return h; }).toList());
        when(bookings.findByUserIdOrderByCreatedAtDesc(anyLong())).thenReturn(List.of(new Booking()));
        clearInvocations(hotels, bookings);
        seeder.run();

        assertThat(savedUsers).hasSize(usersAfterFirst);
        verify(hotels, never()).save(any());
        verify(bookings, never()).save(any());
    }

    @Test
    void doesNothingWhenSwitchedOff() {
        ReflectionTestUtils.setField(seeder, "enabled", false);
        seeder.run();
        verifyNoInteractions(users, hotels, bookings);
    }

    private static List<Destination> destinations(String... names) {
        List<Destination> list = new ArrayList<>();
        long id = 1;
        for (String n : names) { Destination d = new Destination(); d.setId(id++); d.setName(n); list.add(d); }
        return list;
    }

    private static TravelPackage pkg(String name, String price) {
        TravelPackage p = new TravelPackage();
        p.setName(name);
        p.setPrice(new BigDecimal(price));
        return p;
    }
}
