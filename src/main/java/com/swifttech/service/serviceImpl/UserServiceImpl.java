package com.swifttech.service.serviceImpl;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.model.Otp;
import com.swifttech.model.Status;
import com.swifttech.model.User;
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
import java.util.Optional;
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
        otp.setUser(user);
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

            if (user.getStatus() == Status.BLOCKED) {
                LocalDateTime currentTime = LocalDateTime.now();
                LocalDateTime blockEndTime = user.getBlockEndTime();

                if (currentTime.isBefore(blockEndTime)) {
                    return "Your account is blocked. Please try again later.";
                } else {
                    user.setStatus(Status.ACTIVE);
                    user.setFailedAttempts(0);
                    user.setBlockStartTime(null);
                    user.setBlockEndTime(null);
                    userRepo.save(user);
                }
            }

            Optional<Otp> otpOptional = otpRepo.findTopByUserIdAndStatusOrderByIdDesc(user.getId(), Status.REGISTER);
            if (otpOptional.isEmpty()) {
                return "No OTP record found for this user";
            }
            Otp otp = otpOptional.get();
            String decryptedOtp = EncryptDecrypt.decrypt(otp.getOtp());
            LocalDateTime currentTime = LocalDateTime.now();

            if (currentTime.isBefore(otp.getOtpExpiryTime())) {
                if (decryptedOtp != null && decryptedOtp.equals(verifyAccountRequest.getOtp())) {
                   user.setActive(true);
                    otp.setFailedAttempts(0);
                    userRepo.save(user);
                    return "OTP verified, you can login";
                } else {
                    int otpAttempts = otp.getFailedAttempts() + 1;
                    otp.setFailedAttempts(otpAttempts);
                    userRepo.save(user);

                    if (otpAttempts >= 3) {
                        LocalDateTime blockStartTime = LocalDateTime.now();
                        LocalDateTime blockEndTime = blockStartTime.plusMinutes(5);
                        user.setStatus(Status.BLOCKED);
                        user.setBlockStartTime(blockStartTime);
                        user.setBlockEndTime(blockEndTime);
                        userRepo.save(user);
                        return "Maximum OTP verification attempts reached. Your account is now locked. Please try again after 5 minutes";
                    } else {
                        return "Invalid OTP. You have " + (3 - otpAttempts) + " attempts remaining.";
                    }
                }
            } else {
                return "OTP has expired, please regenerate OTP and try again";
            }
        } catch (Exception e) {
            return "Error occurred while verifying OTP";
        }
    }

    public String login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail());

        if (user == null) {
            return "User not found";
        }

        if (!user.isActive()) {
            return "Your account is not verified";
        }

        if (user.getStatus() == Status.BLOCKED) {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime blockEndTime = user.getBlockEndTime();

            if (currentTime.isBefore(blockEndTime)) {
                return "Your account is blocked. Please try again later.";
            } else {

                user.setStatus(Status.ACTIVE);
                user.setFailedAttempts(0);
                user.setBlockStartTime(null);
                user.setBlockEndTime(null);
                userRepo.save(user);
            }
        }

        String decryptedPassword = EncryptDecrypt.decrypt(user.getPassword());
        if (decryptedPassword != null && decryptedPassword.equals(loginDto.getPassword())) {

            user.setFailedAttempts(0);
            userRepo.save(user);
            return "Login successful";
        } else {

            int passwordAttempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(passwordAttempts);
            userRepo.save(user);

            if (passwordAttempts >= 3) {

                LocalDateTime blockStartTime = LocalDateTime.now();
                LocalDateTime blockEndTime = blockStartTime.plusMinutes(5);
                user.setStatus(Status.BLOCKED);
                user.setBlockStartTime(blockStartTime);
                user.setBlockEndTime(blockEndTime);
                userRepo.save(user);
                return "Maximum login attempts reached. Your account is now locked. Please try again after 5 minutes";
            } else {
                return "Password is incorrect. You have " + (3 - passwordAttempts) + " attempts remaining.";
            }
        }
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}





