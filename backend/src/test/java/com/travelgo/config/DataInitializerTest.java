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
        when(packages.count()).thenReturn(0L);

        Destination goa = new Destination();
        goa.setId(1L); goa.setName("Goa");
        goa.setCountry("India");
        goa.setStartingPrice(new BigDecimal("12999"));

        Destination dubai = new Destination();
        dubai.setId(2L);
        dubai.setName("Dubai");
        dubai.setCountry("UAE");
        dubai.setStartingPrice(new BigDecimal("35999"));

        Destination bali = new Destination();
        bali.setId(3L);
        bali.setName("Bali");
        bali.setCountry("Indonesia");
        bali.setStartingPrice(new BigDecimal("28999"));

        Destination maldives = new Destination();
        maldives.setId(4L);
        maldives.setName("Maldives");
        maldives.setCountry("Maldives");
        maldives.setStartingPrice(new BigDecimal("45999"));

        when(destinations.findAll()).thenReturn(List.of(goa, dubai, bali, maldives));
        when(users.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(destinations.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(packages.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DataInitializer initializer = new DataInitializer();
        initializer.seed(users, destinations, packages, hotels, encoder).run();

        verify(packages, times(4)).save(any(TravelPackage.class));
    }
}
