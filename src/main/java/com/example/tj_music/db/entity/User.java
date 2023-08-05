package com.example.tj_music.db.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author rkr
 * @date 2021/4/18
 * @description: user entity
 * user_id
 * create_time
 * userStudentNumber
 * user_nickname
 * user_password
 * user_signature
 * user_status
 * user_profileImageFilename
 * user_birthday
 * user_area1
 * user_area2
 * user_gender
 * user_college
 * user_major
 */
@Data
public class User {
    private Integer userId;
    private Date createTime;
    private String userStudentNumber;
    private String userNickname;
    private String userPassword;
    private String userSignature;
    private String userStatus;
    private String userProfileImageFilename;
    private Date userBirthday;
    private String userArea1;
    private String userArea2;
    private String userGender;
    private String userCollege;
    private String userMajor;
    private Integer userFollowCnt;
    private Integer userPostsCnt;
    private Integer userFansCnt;
    private String userIdentifyingCode;
}