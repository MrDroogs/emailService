package com.swifttech.dto.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class ResetPassword {
    private Long id;
    private String otp;
    private String email;
    private String newPassword;
    private String confirmPassword;

}
