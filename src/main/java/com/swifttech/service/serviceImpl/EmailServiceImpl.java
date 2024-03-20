package com.swifttech.service.serviceImpl;

import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPasswordRequest;
import com.swifttech.model.Otp;
import com.swifttech.model.Status;
import com.swifttech.model.User;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import com.swifttech.util.EmailUtil;
import com.swifttech.util.EncryptDecrypt;
import com.swifttech.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final EncryptDecrypt encryptDecrypt;

    public String regenerateOtp(RegenerateOtpRequest regenerateOtpRequest) {
        User user = userRepo.findByEmail(regenerateOtpRequest.getEmail());

        String otp = otpUtil.generateOtp();
        Otp code = new Otp();
        LocalDateTime otpGeneratedTime = LocalDateTime.now();
        LocalDateTime otpExpiryTime = otpGeneratedTime.plusMinutes(5);
        code.setUser(user);
        code.setOtpGeneratedTime(otpGeneratedTime);
        code.setOtpExpiryTime(otpExpiryTime);
        try {
            emailUtil.sendOtpEmail(regenerateOtpRequest.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        String encryptedOtp = EncryptDecrypt.encrypt(otp);
        code.setOtp(encryptedOtp);
        code.setStatus(Status.REGISTER);
//        code.setStatus(Status.RESET);
        otpRepo.save(code);
        userRepo.save(user);
        return "Email sent... please reset";
    }

    public String requestPasswordReset(ResetPasswordRequest passwordRequest) {
        User user = userRepo.findByEmail(passwordRequest.getEmail());

        if (user == null) {
            return "User not found";
        }

        String generatedOtp = OtpUtil.generateOtp();

        String encryptedOtp = EncryptDecrypt.encrypt(generatedOtp);

        Otp otp = new Otp();
        LocalDateTime otpGeneratedTime = LocalDateTime.now();
        LocalDateTime otpExpiryTime = otpGeneratedTime.plusMinutes(5);
        otp.setOtpGeneratedTime(otpGeneratedTime);
        otp.setOtpExpiryTime(otpExpiryTime);
        otp.setStatus(Status.RESET);
        otp.setOtp(encryptedOtp);
        otp.setUser(user);
        otpRepo.save(otp);
        userRepo.save(user);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("alertremittancev2@swifttech.com.np");
            mimeMessageHelper.setTo(passwordRequest.getEmail());
            mimeMessageHelper.setSubject("Password Reset");
            String messageBody = String.format("<div><p>Your OTP for password reset is: <strong>%s</strong></p></div>",
                    generatedOtp);
            mimeMessageHelper.setText(messageBody, true);

            javaMailSender.send(mimeMessage);

            return "Password reset email sent successfully!";
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP, please try again");
        }
    }



}



