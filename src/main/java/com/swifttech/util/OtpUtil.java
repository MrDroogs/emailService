package com.swifttech.util;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class OtpUtil {
    public static String generateOtp(){
        Random random=new SecureRandom();
        int otp =100000+ random.nextInt(900000);
        return String.valueOf(otp);
    }


}
