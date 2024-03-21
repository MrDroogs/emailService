package com.swifttech.service;

import com.swifttech.dto.request.BulkMailRequest;
import com.swifttech.dto.request.ResetPasswordRequest;

public interface BulkEmailService {
    public String requestBulkEmail(BulkMailRequest bulkMailRequest);

}
