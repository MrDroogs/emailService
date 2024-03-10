package com.swifttech.service;

import com.swifttech.model.User;

public interface EmailService {
     void sendResetEmail(String to);

    User findByEmail(String email);

    void save(User user);


}
