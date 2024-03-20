package com.swifttech.service;

import com.swifttech.dto.request.BulkMailRequest;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPasswordRequest;

public interface EmailService {

    public String regenerateOtp(RegenerateOtpRequest regenerateOtpRequest);

    public String requestPasswordReset(ResetPasswordRequest passwordRequest);




}
