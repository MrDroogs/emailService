package com.swifttech.controller;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import com.swifttech.service.serviceImpl.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor

public class RegisterController {
    private final EmailService emailService;
    private final PasswordService passwordService;
    private final UserRepo userRepo;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(emailService.register(registerDto), HttpStatus.OK);
    }

    @PutMapping("/verifyaccount")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyAccountRequest verifyAccountRequest) {
        return new ResponseEntity<>(emailService.verifyAccount(verifyAccountRequest), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody RegenerateOtpRequest regenerateOtpRequest) {
        return new ResponseEntity<>(emailService.regenerateOtp(regenerateOtpRequest), HttpStatus.OK);
    }
    @PutMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(emailService.login(loginDto), HttpStatus.OK);
    }

}
