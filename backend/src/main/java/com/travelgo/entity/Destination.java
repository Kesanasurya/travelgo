package com.travelgo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="destinations") @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Destination {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private String country;
    @Column(columnDefinition="TEXT") private String description;
    private String imageUrl;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal startingPrice;
    private Instant createdAt; private Instant updatedAt;
    @PrePersist void created(){createdAt=Instant.now(); updatedAt=createdAt;}
    @PreUpdate void updated(){updatedAt=Instant.now();}
}
