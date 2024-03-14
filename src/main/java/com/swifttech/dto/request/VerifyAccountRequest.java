package com.swifttech.dto.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class VerifyAccountRequest {
    private String email;
    private String otp;

}
