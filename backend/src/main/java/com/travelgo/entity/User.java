package com.travelgo.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="users", indexes=@Index(name="idx_users_email", columnList="email", unique=true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
    @JsonIgnore @Column(nullable=false) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.USER;
    @Column(nullable=false, updatable=false) private Instant createdAt;
    @PrePersist void created() { createdAt = Instant.now(); }
    public enum Role { USER, ADMIN }
}
