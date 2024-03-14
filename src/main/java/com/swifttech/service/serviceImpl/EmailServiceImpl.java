package com.swifttech.service.serviceImpl;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ResetPasswordRequest;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.model.User;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import com.swifttech.util.EmailUtil;
import com.swifttech.util.EncryptDecrypt;
import com.swifttech.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private  final OtpUtil otpUtil;
    private  final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final EncryptDecrypt encryptDecrypt;




    public String register(RegisterDto registerDto) {

        User user = new User();

        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepo.save(user);


        String otp = otpUtil.generateOtp();
        String encryptedOtp = EncryptDecrypt.encrypt(otp);
        user.setOtp(encryptedOtp);
        userRepo.save(user);

        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp, please try again");
        }

        return "User registration successful";
    }
    public String verifyAccount(VerifyAccountRequest verifyAccountRequest) {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            User user = userRepo.findByEmail(verifyAccountRequest.getEmail());
            logger.debug("Fetched user: {}", user);
            if (user == null) {
                return "User not found";
            }
            String decryptedOtp = EncryptDecrypt.decrypt(user.getOtp());
            logger.debug("Decrypted OTP: {}", decryptedOtp);
            if (decryptedOtp.equals(verifyAccountRequest.getOtp())&& Duration.between(user.getOtpGeneratedTime(),
                    LocalDateTime.now()).getSeconds() < (5 * 60)) {
                user.setActive(true);
                userRepo.save(user);
                return "OTP verified, you can login";
            }
        } catch (Exception e) {
            logger.error("Error while verifying the account", e);
        }
        return "Please regenerate OTP and try again";
    }





    public String regenerateOtp(RegenerateOtpRequest regenerateOtpRequest) {
        User user = userRepo.findByEmail(regenerateOtpRequest.getEmail());
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(regenerateOtpRequest.getEmail(),otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String encryptedOtp = EncryptDecrypt.encrypt(otp);
        user.setOtp(encryptedOtp);
        userRepo.save(user);
        return "Email sent... please reset";
    }



    public String login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail());
        if (!loginDto.getPassword().equals(user.getPassword())) {
            return "Password is incorrect";
        } else if (!user.isActive()) {
            return "your account is not verified";
        }
        return "Login successful";
    }


}





