package com.swifttech.controller;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.repo.UserRepo;
import com.swifttech.request.RegenerateOtpRequest;
import com.swifttech.request.VerifyAccountRequest;
import com.swifttech.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor

public class ResetPasswordController {

    private final EmailService emailService;
    private final UserRepo userRepo;



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(emailService.register(registerDto), HttpStatus.OK);
    }

    @PutMapping("/verifyaccount")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyAccountRequest request) {
        String email= request.getEmail();
        String otp= request.getOtp();
        return new ResponseEntity<>(emailService.verifyAccount(email,otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody RegenerateOtpRequest regenerateOtpRequest) {
       String email= regenerateOtpRequest.getEmail();
        return new ResponseEntity<>(emailService.regenerateOtp(email), HttpStatus.OK);
    }
    @PutMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(emailService.login(loginDto), HttpStatus.OK);
    }
    @PutMapping("/passwordreset")
    public ResponseEntity<String>resetPassword(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(emailService.resetPassword(String.valueOf(registerDto)),HttpStatus.OK);
    }

}



