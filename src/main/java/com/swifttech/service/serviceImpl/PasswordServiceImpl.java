package com.swifttech.service.serviceImpl;

import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ResetPasswordRequest;
import com.swifttech.model.User;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.util.EmailUtil;
import com.swifttech.util.EncryptDecrypt;
import com.swifttech.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor

public class PasswordServiceImpl implements PasswordService{
    private final JavaMailSender javaMailSender;
    private  final OtpUtil otpUtil;
    private  final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final EncryptDecrypt encryptDecrypt;

    public String requestPasswordReset(ResetPasswordRequest passwordRequest) {
        User user = userRepo.findByEmail(passwordRequest.getEmail());
        if (user == null) {
            return "User not found";
        }
        String code = OtpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(passwordRequest.getEmail(), code);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String encryptedOtp = EncryptDecrypt.encrypt(code);
        user.setOtp(encryptedOtp);
        userRepo.save(user);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom("alertremittancev2@swifttech.com.np");
        message.setSubject("Password Reset");
        message.setText("Your OTP for password reset is: " + code);
        javaMailSender.send(message);
        return "Password reset email sent successfully!";
    }

    @Override
    public String resetPassword(ResetPassword resetPassword) {
        try {
            User user = userRepo.findByEmail(resetPassword.getEmail());
            if (user == null) {
                return "User not found";
            }
            String decryptedOtp = EncryptDecrypt.decrypt(user.getOtp());
            if (decryptedOtp.equals(resetPassword.getOtp()) &&
                    Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (5 * 60)) {

                if (!resetPassword.getNewPassword().equals(resetPassword.getConfirmPassword())) {
                    return "New password and confirm password do not match";
                }
                user.setPassword(resetPassword.getNewPassword());
                user.setActive(true);
                userRepo.save(user);
                return "Password reset successful, you can now login";
            } else {
                return "OTP verification failed or OTP expired. Please regenerate OTP and try again";
            }
        } catch (Exception e) {
            return "An error occurred while resetting password";
        }
    }
}
