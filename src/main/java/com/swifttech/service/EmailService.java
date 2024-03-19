package com.swifttech.service;

import com.swifttech.dto.UserDto;
import com.swifttech.dto.request.BulkMailRequest;
import com.swifttech.dto.request.RegenerateOtpRequest;
import com.swifttech.dto.request.ResetPasswordRequest;

import java.util.List;

public interface EmailService {

    public String regenerateOtp(RegenerateOtpRequest regenerateOtpRequest);

    public String requestPasswordReset(ResetPasswordRequest passwordRequest);

//    public String sendBulkEmail(BulkMailRequest bulkMailRequest);
//
//    List<String> getAllEmails();
}
