package com.swifttech.dto.request;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class RegenerateOtpRequest {

    private String email;

}
