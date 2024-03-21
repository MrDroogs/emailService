package com.swifttech.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BulkEmailUtil {
     private  final JavaMailSender mailSender;
    public void sendBulk(String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("alertremittancev2@swifttech.com.np");
        mimeMessageHelper.setBcc();
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Test Bulk enail");
        String messageBody = String.format(
                "<div><p>This mail is for testing  <strong>%s</strong></p></div>");
        mimeMessageHelper.setText(messageBody, true);

        mailSender.send(mimeMessage);
    }

}
