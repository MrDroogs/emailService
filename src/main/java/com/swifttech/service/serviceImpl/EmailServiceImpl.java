package com.swifttech.service.serviceImpl;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
//    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30L;
    private final JavaMailSender javaMailSender;
    private  final OtpUtil otpUtil;
    private  final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final EncryptDecrypt encryptDecrypt;




public String register(RegisterDto registerDto) {
    String otp = otpUtil.generateOtp();
    String encryptedOtp=EncryptDecrypt.encrypt(otp);

    try {
        emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
    } catch (MessagingException e) {
        throw new RuntimeException("Unable to send otp please try again");
    }
    User user = new User();
    user.setName(registerDto.getName());
    user.setEmail(registerDto.getEmail());
    user.setPassword(registerDto.getPassword());
    user.setOtp(encryptedOtp);
    user.setOtpGeneratedTime(LocalDate.from(LocalDateTime.now()));
    userRepo.save(user);
    return "User registration successful";
}
public String verifyAccount(String email, String otp) {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    try {
        User user = userRepo.findByEmail(email);
        logger.debug("Fetched user: {}", user);
        if (user == null) {
            return "User not found";
        }

        if (user != null && user.getOtp()
                .equals(otp)) {
            user.setActive(true);
            userRepo.save(user);
            return "OTP verified, you can login";
        }
    } catch (Exception e) {
        logger.error("Error while verifying the account", e);
    }
    return "Please regenerate Otp and try agian";
}



    public String regenerateOtp(String email) {
        User user = userRepo.findByEmail(email);
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        userRepo.save(user);
        return "Email sent... please reset";
    }

    @Override
    public String resetPassword(String email) {
        User user=userRepo.findByEmail(email);

        if (user ==null) {
                   return "User not found";
               }
        String otp=otpUtil.generateOtp();
        String encryptedOtp=EncryptDecrypt.encrypt(otp);
        user.setOtp(encryptedOtp);
        userRepo.save(user);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText("Your OTP for password reset is: " + otp);
        javaMailSender.send(message);
        return "password reset successfull";

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


