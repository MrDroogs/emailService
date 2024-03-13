package com.swifttech.util;



public class Constant {

    public static final String key = "1234567812345678";
    public static final String initVector = "1234567812345678";
    public static final String algo = "AES/CBC/PKCS5PADDING";

    public static final Integer SUCCESS_STATUS_CODE = 100;
    public static final Integer FAILURE_STATUS_CODE = 500;
    public static final Integer OTP_LENGTH = 6;

    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

}
