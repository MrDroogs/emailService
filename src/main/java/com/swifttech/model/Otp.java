package com.swifttech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String otp;
    private Long otpGenerated;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime otpGeneratedTime;
    private LocalDateTime otpExpiryTime;
}
