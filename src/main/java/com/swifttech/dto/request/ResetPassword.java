package com.swifttech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {
    private String otp;
    private String email;
    private String newPassword;
    private String confirmPassword;

}
