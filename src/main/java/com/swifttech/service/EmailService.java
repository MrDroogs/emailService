package com.swifttech.service;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.RegisterDto;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ResetPasswordRequest;
import com.swifttech.dto.request.VerifyAccountRequest;

public interface EmailService {

    public String register(RegisterDto registerDto);
    public String login(LoginDto loginDto);

    public String verifyAccount(VerifyAccountRequest verifyAccountRequest);
    public String regenerateOtp(RegenerateOtpRequest regenerateOtpRequest);

}
