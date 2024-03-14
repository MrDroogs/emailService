package com.swifttech.controller;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.repo.UserRepo;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ResetPasswordRequest;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.service.EmailService;
import com.swifttech.service.serviceImpl.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reset")
@RequiredArgsConstructor

public class ResetPasswordController {

    private final EmailService emailService;
    private final PasswordService passwordService;
    private final UserRepo userRepo;

    @PutMapping("/requestReset")
    public ResponseEntity<String>requestReset(@RequestBody ResetPasswordRequest passwordRequest){
        return new ResponseEntity<>(passwordService.requestPasswordReset(passwordRequest),HttpStatus.OK);
    }
    @PutMapping("/password-reset")
    public ResponseEntity<String>resetPassword(@RequestBody ResetPassword resetPassword){
        return new ResponseEntity<>(passwordService.resetPassword(resetPassword),HttpStatus.OK);
    }

}



