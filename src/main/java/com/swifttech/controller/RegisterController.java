package com.swifttech.controller;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.*;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import com.swifttech.service.PasswordService;
import com.swifttech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor

public class RegisterController {
    private final UserService userService;
    private final PasswordService passwordService;
    private final EmailService emailService;

    private final UserRepo userRepo;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>(userService.register(registerDto), HttpStatus.OK);
    }

    @PutMapping("/verifyaccount")
    public ResponseEntity<String> verifyAccount(@RequestBody VerifyAccountRequest verifyAccountRequest) {
        return new ResponseEntity<>(userService.verifyAccount(verifyAccountRequest), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestBody RegenerateOtpRequest regenerateOtpRequest) {
        return new ResponseEntity<>(emailService.regenerateOtp(regenerateOtpRequest), HttpStatus.OK);
    }
    @PutMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }
    @PutMapping("/requestReset")
    public ResponseEntity<String>requestReset(@RequestBody ResetPasswordRequest passwordRequest){
        return new ResponseEntity<>(emailService.requestPasswordReset(passwordRequest),HttpStatus.OK);
    }
    @PutMapping("/password-reset")
    public ResponseEntity<String>resetPassword(@RequestBody ResetPassword resetPassword){
        return new ResponseEntity<>(passwordService.resetPassword(resetPassword),HttpStatus.OK);
    }
    @PutMapping("/changePassword")
    public ResponseEntity<String>changePassword(@RequestBody ChangePassword changePassword){
        return new ResponseEntity<>(passwordService.changePassword(changePassword),HttpStatus.OK);
    }


}
