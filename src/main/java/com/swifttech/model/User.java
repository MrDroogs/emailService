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
@Table(name = "test_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Status status;
    private int failedAttempts;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime blockStartTime;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime blockEndTime;



}
