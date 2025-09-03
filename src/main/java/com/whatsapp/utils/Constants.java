package com.whatsapp.utils;

import lombok.Data;

@Data
public class Constants {
    public static final String EMAIL_REG_EXP = "[a-zA-Z0-9.+_-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}";
    public static final String PHONE_REG_EXP = "^[0-9]+(\\.[0-9]+)?$";
    public static final String NAME_REG_EXP = "^(?! )[A-Za-z ]*(?<! )$";
}
