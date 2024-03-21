package com.swifttech.service.serviceImpl;

import com.swifttech.dto.request.BulkMailRequest;
import com.swifttech.model.User;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.BulkEmailService;
import com.swifttech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class BulkEmailServiceImpl implements BulkEmailService {
    private final JavaMailSender mailSender;
    private final UserRepo userRepo;
    private final UserService userService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust thread pool size as needed

    @Override
    public String requestBulkEmail(BulkMailRequest bulkMailRequest) {
        List<User> emails= userRepo.findAll();
        return null;
    }

    //
// try {
//        // 1. Retrieve Data
//        List<User> users = userService.findUsersByCriteria(bulkMailRequest.getCriteria());
//
//        // 2. Process Data
//        List<String> recipientEmails = new ArrayList<>();
//        for (User user : users) {
//            recipientEmails.add(user.getEmail());
//            // Additional processing if needed
//        }
//
//        // 3. Send Bulk Email
//        emailService.sendBulkEmail(recipientEmails, bulkMailRequest.getEmailContent());
//
//        return "Bulk email sent successfully.";
//    } catch (Exception e) {
//        // 4. Handle Errors and Exceptions
//        e.printStackTrace(); // Handle error logging appropriately
//        return "Failed to send bulk email: " + e.getMessage();
//  }
}
