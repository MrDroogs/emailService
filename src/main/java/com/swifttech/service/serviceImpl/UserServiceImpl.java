package com.swifttech.service.serviceImpl;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.model.Otp;
import com.swifttech.model.User;
import com.swifttech.model.Status;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.UserService;
import com.swifttech.util.EmailUtil;
import com.swifttech.util.EncryptDecrypt;
import com.swifttech.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.swifttech.util.Constant.PASSWORD;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JavaMailSender javaMailSender;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;

    public String register(RegisterDto registerDto) {

        User user = new User();
        Otp otp = new Otp();

        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        if (!isValidPassword(registerDto.getPassword())) {
            throw new RuntimeException("invalid password ");
        }
        String encryptPassword = EncryptDecrypt.encrypt(registerDto.getPassword());
        user.setPassword(encryptPassword);


        LocalDateTime otpGeneratedTime = LocalDateTime.now();
        LocalDateTime otpExpiryTime = otpGeneratedTime.plusMinutes(5);
        otp.setOtpGeneratedTime(otpGeneratedTime);
        otp.setOtpExpiryTime(otpExpiryTime);
        otp.setStatus(Status.REGISTER);

        userRepo.save(user);
        otpRepo.save(otp);

        String generateOtp = otpUtil.generateOtp();
        String encryptedOtp = EncryptDecrypt.encrypt(generateOtp);
        otp.setOtp(encryptedOtp);
        otpRepo.save(otp);

        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), generateOtp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp, please try again");
        }

        return "User registration successful";
    }


    public String verifyAccount(VerifyAccountRequest verifyAccountRequest) {
        try {
            User user = userRepo.findByEmail(verifyAccountRequest.getEmail());

            if (user == null) {
                return "User not found";
            }

            Otp otp = otpRepo.findById(user.getId())
                    .orElseThrow();

            String decryptedOtp = EncryptDecrypt.decrypt(otp.getOtp());
            LocalDateTime currentTime = LocalDateTime.now();

            if (currentTime.isBefore(otp.getOtpExpiryTime())) {
                System.out.println("OTP is valid.");

                if (decryptedOtp != null && decryptedOtp.equals(verifyAccountRequest.getOtp())) {
                    user.setActive(true);
                    userRepo.save(user);
                    return "OTP verified, you can login";
                } else {
                    return "Invalid OTP";
                }
            } else {
                System.out.println("OTP has expired.");
                return "OTP has expired, please regenerate OTP and try again";
            }
        } catch (Exception e) {

            return "Error occurred while verifying OTP";
        }
    }


    public String login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail());
        String decryptedPassword = EncryptDecrypt.decrypt(loginDto.getPassword());
        if ((decryptedPassword != null &&
                decryptedPassword.equals(user.getPassword()))) {
            return "Password is incorrect";
        } else if (!user.isActive()) {
            return "your account is not verified";
        }
        return "Login successful";
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}




