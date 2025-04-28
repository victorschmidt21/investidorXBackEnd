package com.java.Invista.Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private String email;

    public Email(String email) {
        this.email = email;
        validateEmail();
    }

    public String validateEmail(){
        String expression = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new RuntimeException("Email inválido: " + email);
        }
        return email;
    }

    public String getEmail() {
        return email;
    }

}
