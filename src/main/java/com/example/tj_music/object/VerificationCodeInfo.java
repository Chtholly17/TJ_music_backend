package com.example.tj_music.object;


import lombok.Data;

@Data
public class VerificationCodeInfo {
    String userNumber;
    String password;
    String verificationCode;
    String checkPassword;
}
