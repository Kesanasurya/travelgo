package com.travelgo.config;
import com.travelgo.entity.*;
import com.travelgo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;
@Configuration @RequiredArgsConstructor
public class DataInitializer {
 @Bean CommandLineRunner seed(UserRepository users,DestinationRepository destinations,TravelPackageRepository packages,HotelRepository hotels,PasswordEncoder encoder){return args->{if(!users.existsByEmailIgnoreCase("admin@travelgo.dev")){users.save(new User(null,"TravelGo Admin","admin@travelgo.dev",encoder.encode("Admin@123"),User.Role.ADMIN,null));} if(destinations.count()==0){String[][] data={{"Goa","India","Sunny beaches and vibrant coastal experiences","12999"},{"Dubai","UAE","Luxury shopping, desert adventures and iconic landmarks","35999"},{"Bali","Indonesia","Temples, rice terraces and peaceful island escapes","28999"},{"Maldives","Maldives","Crystal-clear lagoons and private island stays","45999"},{"Paris","France","Art, culture, cuisine and iconic landmarks","54999"},{"London","United Kingdom","Historic streets and world-class museums","59999"}};for(String[] d:data)destinations.save(new Destination(null,d[0],d[1],d[2],null,new BigDecimal(d[3]),null,null));}if(packages.count()==0){var goa=destinations.findAll().get(0);packages.save(new TravelPackage(null,"Goa Beach Explorer",goa,"Beaches, sightseeing and local experiences","4 Days / 3 Nights",new BigDecimal("15000"),null,"Breakfast, transfers, guided sightseeing",null,null));}};}
}
