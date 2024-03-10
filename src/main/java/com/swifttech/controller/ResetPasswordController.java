package com.swifttech.controller;

import com.swifttech.model.User;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import com.swifttech.util.OtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class ResetPasswordController {

    private final EmailService emailService;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;

    public ResetPasswordController(EmailService emailService, UserRepo userRepo, OtpRepo otpRepo) {
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
    }

    @PostMapping("/requestReset")
    public ResponseEntity<String> requestResetPassword(@RequestBody String email) {
        User user = emailService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
        String otp= OtpUtil.generateOtp();
         emailService.sendResetEmail(user.getEmail());
        return ResponseEntity.ok("Password reset instructions sent to your email");
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestBody String email, @RequestBody String providedOtp) {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return "User not found";
        }


        if (providedOtp.equals(user.(otpRepo))) {

            user.setOtp(null);
            userRepo.save(user);

            return "OTP verification successful!";
        } else {
            return "Invalid OTP";
        }
    }

    @PostMapping("/resetpassword")
    public String resetPassword(@RequestBody String email,@RequestBody String newPassword){
        User user= userRepo.findByEmail(email);
        if (user==null){
            return "User not found";
        }
        user.setPassword(newPassword);
        userRepo.save(user);
        return "password reset successful!";
    }
}


