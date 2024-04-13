package com.ecommerce.authservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "login")
@Data
@Getter
@Setter
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private LocalDateTime logginAttemp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
