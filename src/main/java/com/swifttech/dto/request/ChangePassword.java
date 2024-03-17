package com.swifttech.dto.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class ChangePassword {
    private String email;
     private String password;
    private String newPassword;
    private String confirmPassword;

}
