package com.example.tj_music.db.mapper;

import org.apache.ibatis.annotations.*;
import com.example.tj_music.db.entity.User;

import java.util.List;
import java.util.Date;
@Mapper
public interface UserMapper {
    // insert user
    @Insert("insert into user(user_student_number, user_password, user_status, user_nickname, user_signature, user_profileImageFilename) values(#{userStudentNumber}, #{user_password}, #{user_status}, #{user_nickname}, #{user_signature}, #{user_profileImageFilename})")
    public void insertUser(String userStudentNumber, String userPassword, String userStatus, String userNickname, String userSignature, String userProfileImageFilename);

    // get all users
    @Select("select * from user where user_status = 'normal' or user_status = 'banned'")
    public List<User> getAllUsers();

    // select user by student number
    @Select("select * from user where user_student_number=#{userStudentNumber}")
    public User selectUserByStudentNumber(String userStudentNumber);

    // select id by student number
    @Select("select user_id from user where user_student_number=#{userStudentNumber}")
    public Integer selectIdByStudentNumber(String userStudentNumber);

    // select user by id
    @Select("select * from user where user_id=#{userId}")
    public User selectUserById(Integer userId);

    // update user status by id
    @Update("update user set user_status=#{userStatus} where user_id=#{userId}")
    public void updateUserStatusById(String userStatus, Integer userId);

    // update user password by StudentNumber
    @Update("update user set user_password=#{userPassword} where user_student_number=#{userStudentNumber}")
    public void updateUserPasswordByStudentNumber(String userPassword, String userStudentNumber);

    // update user verification code by StudentNumber
    @Update("update user set user_identifying_code=#{userVerificationCode} where user_student_number=#{userStudentNumber}")
    public void updateUserVerificationCodeByStudentNumber(String userVerificationCode, String userStudentNumber);

    // update user nickname by StudentNumber
    @Update("update user set user_nickname=#{userNickname} where user_student_number=#{userStudentNumber}")
    public void updateUserNicknameByStudentNumber(String userNickname, String userStudentNumber);

    // update user college by StudentNumber
    @Update("update user set user_college=#{userCollege} where user_student_number=#{userStudentNumber}")
    public void updateUserCollegeByStudentNumber(String userCollege, String userStudentNumber);

    // update user major by StudentNumber
    @Update("update user set user_major=#{userMajor} where user_student_number=#{userStudentNumber}")
    public void updateUserMajorByStudentNumber(String userMajor, String userStudentNumber);

    // update user area1 by StudentNumber
    @Update("update user set user_area1=#{userArea1} where user_student_number=#{userStudentNumber}")
    public void updateUserArea1ByStudentNumber(String userArea1, String userStudentNumber);

    // update user area2 by StudentNumber
    @Update("update user set user_area2=#{userArea2} where user_student_number=#{userStudentNumber}")
    public void updateUserArea2ByStudentNumber(String userArea2, String userStudentNumber);

    // update user birthday by StudentNumber
    @Update("update user set user_birthday=#{userBirthday} where user_student_number=#{userStudentNumber}")
    public void updateUserBirthdayByStudentNumber(Date userBirthday, String userStudentNumber);

    // update user gender by StudentNumber
    @Update("update user set user_gender=#{userGender} where user_student_number=#{userStudentNumber}")
    public void updateUserGenderByStudentNumber(String userGender, String userStudentNumber);

    // update user signature by StudentNumber
    @Update("update user set user_signature=#{userSignature} where user_student_number=#{userStudentNumber}")
    public void updateUserSignatureByStudentNumber(String userSignature, String userStudentNumber);

    // update user profile image file name by StudentNumber
    @Update("update user set user_profileImageFilename=#{userProfileImage} where user_student_number=#{userStudentNumber}")
    public void updateUserProfileImageByStudentNumber(String userProfileImage, String userStudentNumber);

    // update the user follow cnt by id
    @Update("update user set user_follow_cnt=#{userFollowCnt} where user_id=#{userId}")
    public void updateUserFollowCntById(Integer userFollowCnt, Integer userId);

    // update the user fans cnt by id
    @Update("update user set user_fans_cnt=#{userFansCnt} where user_id=#{userId}")
    public void updateUserFansCntById(Integer userFansCnt, Integer userId);

    // update the user post cnt by id
    @Update("update user set user_posts_cnt=#{userPostCnt} where user_id=#{userId}")
    public void updateUserPostsCntById(Integer userPostsCnt, Integer userId);

    // get the fans cnt by id
    @Select("select count(*) from user_follow where user_followed_id=#{userId}")
    public Integer selectFansCntById(Integer userId);

    // delete the user by id
    @Delete("delete from user where user_id=#{userId}")
    public void deleteUserById(Integer userId);

    // delete the user by student number
    @Delete("delete from user where user_student_number=#{userStudentNumber}")
    public void deleteUserByStudentNumber(String userStudentNumber);
}
