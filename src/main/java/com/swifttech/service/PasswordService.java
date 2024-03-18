package com.swifttech.service;

import com.swifttech.dto.request.ChangePassword;
import com.swifttech.dto.request.ResetPassword;

public interface PasswordService {
    String resetPassword(ResetPassword resetPassword);
     String changePassword(ChangePassword changePassword);



}
