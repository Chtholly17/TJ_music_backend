package com.example.tj_music.object;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class UserInfo {
    String userStudentNumber;
    String userNickName;
    String userCollege;
    String userMajor;
    String userArea1;
    String userArea2;
    String userBirthday;
    String userGender;
    String userSignature;
}
