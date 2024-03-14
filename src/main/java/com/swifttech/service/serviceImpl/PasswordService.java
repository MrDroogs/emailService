package com.swifttech.service.serviceImpl;

import com.swifttech.dto.request.ResetPassword;
import com.swifttech.dto.request.ResetPasswordRequest;

public interface PasswordService {
    public String requestPasswordReset(ResetPasswordRequest passwordRequest);
    String resetPassword(ResetPassword resetPassword);

}
