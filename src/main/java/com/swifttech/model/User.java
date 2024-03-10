package com.swifttech.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


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
    @Column(unique = true)
    private String email;
    private String password;
    private String newPassword;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate resetOtpExpiration;

}
