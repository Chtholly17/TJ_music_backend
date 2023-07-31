package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Image;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.service.informationService;
import com.example.tj_music.utils.ImageUtils;
import com.example.tj_music.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;


@RestController // @RestController = @Controller + @ResponseBody (return json)
public class informationController {
    // user service
    @Autowired // auto-inject
    private informationService informationService;

    // @Autowired
    // private ImageUtils imageUtils;

    /**
     * 获取用户信息
     * 用这个API可以获取用户信息
     * @param user_student_number
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("user_student_number") String user_student_number) {
        User user = informationService.getInformationByStudentNumber(user_student_number);
        if (user == null) {
            return Result.fail("user not found");
        }
        return Result.success(user);
    }

    /**
     * 获取用户头像
     * @param user_student_number
     * @return
     */
    @GetMapping("/getUserImage")
    public Result getUserImage(@RequestParam("user_student_number") String user_student_number) {
        User user = informationService.getInformationByStudentNumber(user_student_number);
        if (user == null) {
            return Result.fail("user not found");
        }
        System.out.println("user id is " + user.getUserId());
        return Result.success(user.getUserProfileImageFilename());
    }

    /**
     * 修改用户信息
     * 用这个API可以修改用户信息
     * @param user_student_number
     * @param new_nickname
     * @param new_college
     * @param new_major
     * @param new_area1
     * @param new_area2
     * @param new_birthday
     * @param new_gender
     * @param new_signature
     * @return
     */
    @PostMapping("/updateUserinfo")
    public Result updateUserinfo(@RequestParam("user_student_number") String user_student_number,
                                 @RequestParam("new_nickname") String new_nickname,
                                 @RequestParam("new_college") String new_college,
                                 @RequestParam("new_major") String new_major,
                                 @RequestParam("new_area1") String new_area1,
                                 @RequestParam("new_area2") String new_area2,
                                 @RequestParam("new_birthday")String new_birthday,
                                 @RequestParam("new_gender") String new_gender,
                                 @RequestParam("new_signature") String new_signature) throws ParseException {
        User user = informationService.getInformationByStudentNumber(user_student_number);
        // print out the user_student_number and new_nickname
//        System.out.println("get the user_student_number: " + user_student_number);
//        System.out.println("get the new_nickname: " + new_nickname);
//        System.out.println("get the birthday: " + new_birthday);
        if (user == null) {
            return Result.fail("user not found");
        }
//        System.out.println(new_birthday);
        informationService.updateUserNickName(new_nickname, user_student_number);
        informationService.updateUserCollege(new_college, user_student_number);
        informationService.updateUserMajor(new_major, user_student_number);
        informationService.updateUserArea1(new_area1, user_student_number);
        informationService.updateUserArea2(new_area2, user_student_number);
        if (!Objects.equals(new_birthday, "")) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(new_birthday);
            informationService.updateUserBirthday(date, user_student_number);
        }
        informationService.updateUserGender(new_gender, user_student_number);
        informationService.updateUserSignature(new_signature, user_student_number);
        return Result.success();
    }


    /**
     * 修改用户头像
     * 用这个API可以修改用户头像
     * @param image
     * @return
     */
    @PostMapping("/updateUserImage")
    public Result updateUserImage(Image image) {
        // print out the user_student_number and file
        System.out.println(image.getUser_student_number());
        System.out.println(image.getFile());
        String user_student_number = image.getUser_student_number();
        MultipartFile file = image.getFile();
        User user = informationService.getInformationByStudentNumber(user_student_number);
        if (user == null) {
            return Result.fail("user not found");
        }
        try{
            String url = ImageUtils.getInstance().upload(file, user_student_number, "avatar");
            informationService.updateUserProfileImage(url, user_student_number);
            return Result.success("upload success");
        } catch (IOException e) {
            return Result.fail("upload failed");
        }
    }

    /**
     * 修改用户密码
     * 用这个API可以修改用户密码
     * @param user_student_number: 用户的学号
     * @param new_password: 用户的新密码
     * @return: list of objects: [user_id, user_name, user_signature, user_profile_image]
     */
    @PostMapping("/updateUserPassword")
    public Result updateUserPassword(@RequestParam("user_student_number") String user_student_number,
                                     @RequestParam("new_password") String new_password) {
        User user = informationService.getInformationByStudentNumber(user_student_number);
        if (user == null) {
            return Result.fail("user not found");
        }
        informationService.updateUserPassword(new_password, user_student_number);
        return Result.success();
    }

    /**
     * get user information.
     * code:1 represents getting user information successfully.
     * code:0 represents getting user information failed. The account does not exist.
     * @param userNumber
     * @return Result, data is a dictionary consist two keys: 'user' and 'workList'
     */
    @GetMapping("/UserProfile")
    public Result getUserProfile(@RequestParam("userNumber") String userNumber) {
        return informationService.getUserInformation(userNumber);
    }
}