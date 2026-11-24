package com.travelgo.config;
import com.travelgo.entity.*;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration @RequiredArgsConstructor
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository users, DestinationRepository destinations, TravelPackageRepository packages, HotelRepository hotels, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByEmailIgnoreCase("admin@travelgo.dev")) {
                users.save(new User(null, "TravelGo Admin", "admin@travelgo.dev", encoder.encode("Admin@123"), User.Role.ADMIN, null));
            }

            if (destinations.count() == 0) {
                String[][] data = {
                        {"Goa", "India", "Sunny beaches and vibrant coastal experiences", "12999"},
                        {"Dubai", "UAE", "Luxury shopping, desert adventures and iconic landmarks", "35999"},
                        {"Bali", "Indonesia", "Temples, rice terraces and peaceful island escapes", "28999"},
                        {"Maldives", "Maldives", "Crystal-clear lagoons and private island stays", "45999"},
                        {"Paris", "France", "Art, culture, cuisine and iconic landmarks", "54999"},
                        {"London", "United Kingdom", "Historic streets and world-class museums", "59999"}
                };
                for (String[] d : data) {
                    destinations.save(new Destination(null, d[0], d[1], d[2], null, new BigDecimal(d[3]), null, null));
                }
            }

                List<Destination> allDestinations = destinations.findAll();
                Set<String> existingPackages = packages.findAll().stream()
                    .map(travelPackage -> travelPackage.getName().toLowerCase(Locale.ROOT))
                    .collect(Collectors.toSet());

                seedPackage(packages, existingPackages, "Goa Beach Explorer", findDestination(allDestinations, "Goa"),
                    "Beaches, sightseeing and local experiences", "4 Days / 3 Nights", "15000",
                    "Breakfast, transfers, guided sightseeing");
                seedPackage(packages, existingPackages, "Dubai Luxury Escape", findDestination(allDestinations, "Dubai"),
                    "Luxury city getaway with desert and skyline experiences", "5 Days / 4 Nights", "42000",
                    "Luxury hotel, city tour, desert safari");
                seedPackage(packages, existingPackages, "Bali Island Adventure", findDestination(allDestinations, "Bali"),
                    "Island discovery with culture, beaches and nature", "6 Days / 5 Nights", "35000",
                    "Breakfast, temple tour, island transfer");
                seedPackage(packages, existingPackages, "Maldives Island Retreat", findDestination(allDestinations, "Maldives"),
                    "Private island retreat with beaches and spa comfort", "5 Days / 4 Nights", "52000",
                    "Airport transfer, breakfast, island tour");
        };
    }

    private static Destination findDestination(List<Destination> destinations, String name) {
        return destinations.stream()
            .filter(destination -> name.equalsIgnoreCase(destination.getName()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing destination for package seed: " + name));
    }

    private static void seedPackage(TravelPackageRepository packages, Set<String> existingNames,
                                    String name, Destination destination, String description, String duration,
                                    String price, String includedServices) {
        if (existingNames.add(name.toLowerCase(Locale.ROOT))) {
            packages.save(new TravelPackage(null, name, destination, description, duration,
                new BigDecimal(price), null, includedServices, null, null));
        }
    }
}

