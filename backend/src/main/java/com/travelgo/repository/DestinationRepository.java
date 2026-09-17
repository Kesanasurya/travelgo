package com.travelgo.repository;
import com.travelgo.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DestinationRepository extends JpaRepository<Destination, Long> { }
