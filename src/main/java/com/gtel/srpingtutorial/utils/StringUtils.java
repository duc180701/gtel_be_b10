package com.gtel.srpingtutorial.utils;

import com.gtel.srpingtutorial.exception.ApplicationException;

public class StringUtils {

    public static void validatePassword(String password) throws ApplicationException {

        if (org.apache.commons.lang3.StringUtils.isEmpty(password) || password.length() < 8) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "password is invalid");
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            throw new ApplicationException(ERROR_CODE.INVALID_PARAMETER , "password must contain at least one letter");
        }


    }
}
