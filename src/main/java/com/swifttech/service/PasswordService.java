package com.swifttech.service;

import com.swifttech.dto.LoginDto;
import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ChangePassword;

public interface PasswordService {
    String resetPassword(ResetPassword resetPassword);
     String changePassword(ChangePassword changePassword);

     String loginAttempts(LoginDto loginDto);

}
