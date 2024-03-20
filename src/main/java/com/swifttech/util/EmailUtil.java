package com.swifttech.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;
    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("alertremittancev2@swifttech.com.np");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Account");
        String messageBody = String.format(
                "<div><p>Your account has been created successfully. Please verify your account using the OTP: <strong>%s</strong></p></div>",
                otp);
        mimeMessageHelper.setText(messageBody, true);

        mailSender.send(mimeMessage);
    }

}


