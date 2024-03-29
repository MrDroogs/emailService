package com.swifttech.dto.request;

import com.swifttech.model.Status;
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
    private Status status;
    private String otp;

}
