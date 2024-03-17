package com.swifttech.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OtpDto {
    private String otp;

    private String email;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime otpGeneratedTime;

}
