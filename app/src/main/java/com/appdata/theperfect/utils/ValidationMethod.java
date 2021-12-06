package com.appdata.theperfect.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationMethod {

    private static Matcher m;
    private static String emailExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    private static Pattern emailPattern = Pattern.compile(emailExpression, Pattern.CASE_INSENSITIVE);
    private static String passwordExpression = "((?=.*\\d)(?=.*[A-Z])(?=.*[0-9]).{8,15})";
    private static Pattern passwordPattern = Pattern.compile(passwordExpression);

    public static boolean emailValidation(String s) {
        if (s == null) {
            return false;
        } else {
            m = emailPattern.matcher(s);
            return m.matches();
        }
    }

    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static boolean passwordValidation(String s) {
        if (s == null) {
            return false;
        } else {
            m = passwordPattern.matcher(s);
            return m.matches();
        }
    }

    public static boolean emailValidation2(String s) {
        m = emailPattern.matcher(s);
        return m.matches();
    }
}
