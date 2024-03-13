package com.swifttech.service;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.model.User;

public interface EmailService {

    public String register(RegisterDto registerDto);



    public String login(LoginDto loginDto);

//    public String sendResetEmail(String email,String otp);



    public String verifyAccount(String email,String otp);
    public String regenerateOtp(String email);

     public String resetPassword(String email);

}
