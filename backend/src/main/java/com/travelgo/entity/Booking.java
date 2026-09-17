package com.travelgo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name="bookings", indexes=@Index(name="idx_bookings_user", columnList="user_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Booking {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="user_id") private User user;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="package_id") private TravelPackage travelPackage;
    @Column(nullable=false, length=40) private String roomType;
    @Column(nullable=false) private LocalDate travelDate;
    @Column(nullable=false) private Integer guests;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status = Status.PENDING;
    private Instant createdAt; private Instant updatedAt;
    @PrePersist void created(){createdAt=Instant.now(); updatedAt=createdAt;}
    @PreUpdate void updated(){updatedAt=Instant.now();}
    public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }
}
