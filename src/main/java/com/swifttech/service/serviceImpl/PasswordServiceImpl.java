package com.swifttech.service.serviceImpl;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.request.ChangePassword;
import com.swifttech.dto.request.ResetPassword;
import com.swifttech.model.Otp;
import com.swifttech.model.Status;
import com.swifttech.model.User;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.PasswordService;
import com.swifttech.util.EmailUtil;
import com.swifttech.util.EncryptDecrypt;
import com.swifttech.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.swifttech.util.Constant.PASSWORD;

@Service
@RequiredArgsConstructor

public class PasswordServiceImpl implements PasswordService {

    private final JavaMailSender javaMailSender;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final EncryptDecrypt encryptDecrypt;

    @Override
    public String resetPassword(ResetPassword resetPassword) {
        try {
            User user = userRepo.findByEmail(resetPassword.getEmail());
            if (user == null) {
                return "User not found";
            }

            Otp otp = otpRepo.findByStatus(Status.RESET);
            if (otp == null) {
                return "OTP not found";
            }
            String decryptedOtp = EncryptDecrypt.decrypt(otp.getOtp());

            if (decryptedOtp == null) {
                return "OTP decryption failed";
            }

            if (LocalDateTime.now()
                    .isAfter(otp.getOtpExpiryTime())) {
                return "OTP expired. Please regenerate OTP and try again";
            }

            if (decryptedOtp.equals(resetPassword.getOtp())) {
                if (!isValidPassword(resetPassword.getNewPassword())) {
                    throw new RuntimeException("Invalid password");
                }
                if (!resetPassword.getNewPassword()
                        .equals(resetPassword.getConfirmPassword())) {
                    return "New password and confirm password do not match";
                }
                String encryptedPassword = EncryptDecrypt.encrypt(resetPassword.getNewPassword());
                user.setPassword(encryptedPassword);
                user.setStatus(Status.ACTIVE);
                userRepo.save(user);
                return "Password reset successful, you can now login";
            } else {
                return "OTP verification failed. Please enter the correct otp ";

            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return "An error occurred while resetting password";
        }
    }

    @Override
    public String changePassword(ChangePassword changePassword) {
        User user = userRepo.findByEmail(changePassword.getEmail());
        if (user == null) {
            return "User not found";
        }
        String newEncryptedPassword = EncryptDecrypt.encrypt(changePassword.getNewPassword());
        String oldEncryptedPassword = EncryptDecrypt.encrypt(changePassword.getPassword());
        if (newEncryptedPassword.equals(oldEncryptedPassword)) {
            return "New password cannot be the same as the old password";
        }
        user.setPassword(newEncryptedPassword);
        userRepo.save(user);

        return "Password updated successfully";
    }



    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
