package com.swifttech.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class OtpUtil {
    public static String generateOtp(){
        Random random=new SecureRandom();
        int otp =100000+ random.nextInt(900000);
        return String.valueOf(otp);
    }


}
