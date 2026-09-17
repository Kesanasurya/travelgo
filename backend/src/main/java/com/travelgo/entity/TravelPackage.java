package com.travelgo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="travel_packages") @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TravelPackage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="destination_id") private Destination destination;
    @Column(columnDefinition="TEXT") private String description;
    @Column(nullable=false) private String duration;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal price;
    private String imageUrl;
    @Column(columnDefinition="TEXT") private String includedServices;
    private Instant createdAt; private Instant updatedAt;
    @PrePersist void created(){createdAt=Instant.now(); updatedAt=createdAt;}
    @PreUpdate void updated(){updatedAt=Instant.now();}
}
