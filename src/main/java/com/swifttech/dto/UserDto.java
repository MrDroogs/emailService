package com.swifttech.dto;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter

public class UserDto {
    private String name;
    private String email;
    private String otp;
    private String password;
    private String newPassword;
    private boolean active;
    private LocalDate otpGeneratedTime;
}
