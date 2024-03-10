package com.swifttech.service.serviceImpl;

import com.swifttech.model.User;
import com.swifttech.repo.OtpRepo;
import com.swifttech.repo.UserRepo;
import com.swifttech.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
//    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30L;
    private final JavaMailSender javaMailSender;
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserRepo userRepo, OtpRepo otpRepo) {
        this.javaMailSender = javaMailSender;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
    }

    @Override
    public void sendResetEmail(String to) {
        String resetOtp ="012365";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Password Reset");
        mailMessage.setText("To reset your password, click the link below:\n\n"
                + "http://localhost/" + resetOtp);

        javaMailSender.send(mailMessage);
    }


    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public void save(User user) {
     userRepo.save(user);
    }




//    public String resetPassword(String otp, String password) {
//        Optional<User> userOptional = Optional
//                .ofNullable(userRepo.findByEmail(email));
//
//        if (!userOptional.isPresent()) {
//            return "Invalid token.";
//        }
//
//        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();
//
//        if (isTokenExpired(tokenCreationDate)) {
//            return "Token expired.";
//
//        }
//
//        User user = userOptional.get();
//
//        user.setPassword(password);
//        user.setToken(null);
//        user.setTokenCreationDate(null);
//
//        userRepository.save(user);
//
//        return "Your password successfully updated.";
//    }
//    }

}

