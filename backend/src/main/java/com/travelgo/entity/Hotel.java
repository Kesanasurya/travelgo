package com.travelgo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="hotels") @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Hotel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="destination_id") private Destination destination;
    @Column(columnDefinition="TEXT") private String description;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal pricePerNight;
    @Column(nullable=false, precision=2, scale=1) private BigDecimal rating;
    private String imageUrl; private String location;
    @Column(columnDefinition="TEXT") private String amenities;
    private Instant createdAt; private Instant updatedAt;
    @PrePersist void created(){createdAt=Instant.now(); updatedAt=createdAt;}
    @PreUpdate void updated(){updatedAt=Instant.now();}
}
