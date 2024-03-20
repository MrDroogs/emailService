package com.swifttech.service;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.VerifyAccountRequest;
import com.swifttech.model.User;

public interface UserService {

    public String register(RegisterDto registerDto);
    public String login(LoginDto loginDto);

    public String verifyAccount(VerifyAccountRequest verifyAccountRequest);



}
