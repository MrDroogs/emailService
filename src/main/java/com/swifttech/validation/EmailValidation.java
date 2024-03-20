package com.swifttech.validation;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class EmailValidation {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

        public static boolean isValid(String email) {
            String[] parts = email.split("@");
            if (parts.length != 2) {
                return false;
            }

            String domain = parts[1];
            try {
                InetAddress address = InetAddress.getByName(domain);
                return true;
            } catch (UnknownHostException e) {
                return false;
            }
        }
    }

