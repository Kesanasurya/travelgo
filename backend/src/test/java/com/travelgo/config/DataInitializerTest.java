package com.travelgo.config;

import com.travelgo.entity.Destination;
import com.travelgo.entity.TravelPackage;
import com.travelgo.repository.DestinationRepository;
import com.travelgo.repository.HotelRepository;
import com.travelgo.repository.TravelPackageRepository;
import com.travelgo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {
    @Mock private UserRepository users;
    @Mock private DestinationRepository destinations;
    @Mock private TravelPackageRepository packages;
    @Mock private HotelRepository hotels;
    @Mock private PasswordEncoder encoder;

    @Test
    void seedsAllTravelPackagesWhenDatabaseIsEmpty() throws Exception {
        when(users.existsByEmailIgnoreCase("admin@travelgo.dev")).thenReturn(false);
        when(destinations.count()).thenReturn(0L);
        when(packages.findAll()).thenReturn(List.of());
        when(destinations.findAll()).thenReturn(sampleDestinations());
        when(users.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(destinations.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(packages.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DataInitializer initializer = new DataInitializer();
        initializer.seed(users, destinations, packages, hotels, encoder).run();

        verify(packages, times(4)).save(any(TravelPackage.class));
    }

    @Test
    void seedsPackagesMissingFromPartiallyPopulatedDatabase() throws Exception {
        when(users.existsByEmailIgnoreCase("admin@travelgo.dev")).thenReturn(true);
        when(destinations.count()).thenReturn(4L);
        when(destinations.findAll()).thenReturn(sampleDestinations());

        TravelPackage existingGoaPackage = new TravelPackage();
        existingGoaPackage.setName("Goa Beach Explorer");
        when(packages.findAll()).thenReturn(List.of(existingGoaPackage));
        when(packages.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DataInitializer initializer = new DataInitializer();
        initializer.seed(users, destinations, packages, hotels, encoder).run();

        verify(packages, times(3)).save(any(TravelPackage.class));
    }

    private List<Destination> sampleDestinations() {
        Destination goa = new Destination();
        goa.setId(1L); goa.setName("Goa");
        Destination dubai = new Destination();
        dubai.setId(2L); dubai.setName("Dubai");
        Destination bali = new Destination();
        bali.setId(3L); bali.setName("Bali");
        Destination maldives = new Destination();
        maldives.setId(4L); maldives.setName("Maldives");
        return List.of(goa, dubai, bali, maldives);
    }
}
