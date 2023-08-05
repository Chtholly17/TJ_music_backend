package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Image;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.object.UserInfo;
import com.example.tj_music.object.UserLoginInfo;
import com.example.tj_music.service.InformationService;
import com.example.tj_music.utils.ImageUtils;
import com.example.tj_music.object.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@RestController
@RequestMapping("/user/info")
public class InformationController {
    // user service
    @Autowired // auto-inject
    private InformationService informationService;

    /**
     * 获取用户信息
     * 用这个API可以获取用户信息
     * @param userStudentNumber
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("userStudentNumber") String userStudentNumber) {
        User user = informationService.getInformationByStudentNumber(userStudentNumber);
        if (user == null) {
            return Result.fail("user not found");
        }
        return Result.success(user);
    }

    /**
     * 获取用户头像
     * @param userStudentNumber
     * @return
     */
    @GetMapping("/getUserImage")
    public Result getUserImage(@RequestParam("userStudentNumber") String userStudentNumber) {
        User user = informationService.getInformationByStudentNumber(userStudentNumber);
        if (user == null) {
            return Result.fail("user not found");
        }
        System.out.println("user id is " + user.getUserId());
        return Result.success(user.getUserProfileImageFilename());
    }

    /**
     * 修改用户信息
     * 用这个API可以修改用户信息
     * @param userInfo 用户信息
     * @return
     */
    @PostMapping("/updateUserinfo")
    public Result updateUserinfo(@RequestBody UserInfo userInfo) throws ParseException {
        User user = informationService.getInformationByStudentNumber(userInfo.getUserStudentNumber());
        if (user == null) {
            return Result.fail("user not found");
        }
//        System.out.println(new_birthday);
        informationService.updateUserNickName(userInfo.getUserNickName(), userInfo.getUserStudentNumber());
        informationService.updateUserCollege(userInfo.getUserCollege(), userInfo.getUserStudentNumber());
        informationService.updateUserMajor(userInfo.getUserMajor(), userInfo.getUserStudentNumber());
        informationService.updateUserArea1(userInfo.getUserArea1(), userInfo.getUserStudentNumber());
        informationService.updateUserArea2(userInfo.getUserArea2(), userInfo.getUserStudentNumber());
        if (!Objects.equals(userInfo.getUserBirthday(), "")) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(userInfo.getUserBirthday());
            informationService.updateUserBirthday(date, userInfo.getUserStudentNumber());
        }
        informationService.updateUserGender(userInfo.getUserGender(), userInfo.getUserStudentNumber());
        informationService.updateUserSignature(userInfo.getUserSignature(), userInfo.getUserStudentNumber());
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
        // print out the userStudentNumber and file
        String userStudentNumber = image.getUserStudentNumber();
        MultipartFile file = image.getFile();
        User user = informationService.getInformationByStudentNumber(userStudentNumber);
        if (user == null) {
            return Result.fail("user not found");
        }
        try{
            String url = ImageUtils.getInstance().upload(file, userStudentNumber, "avatar");
            informationService.updateUserProfileImage(url, userStudentNumber);
            return Result.success("upload success");
        } catch (IOException e) {
            return Result.fail("upload failed");
        }
    }

    /**
     * 修改用户密码
     * 用这个API可以修改用户密码
     * @param userLoginInfo
     * @return: list of objects: [user_id, user_name, user_signature, user_profile_image]
     */
    @PostMapping("/updateUserPassword")
    public Result updateUserPassword(@RequestBody UserLoginInfo userLoginInfo) {
        User user = informationService.getInformationByStudentNumber(userLoginInfo.getUserNumber());
        if (user == null) {
            return Result.fail("user not found");
        }
        informationService.updateUserPassword(userLoginInfo.getPassword(), userLoginInfo.getUserNumber());
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