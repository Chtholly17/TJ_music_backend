package com.example.tj_music.controller;

import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.object.*;
import com.example.tj_music.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/user/account")
public class AccountController {

    // log
    private static Logger log = Logger.getLogger("UserController.class");
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserMapper userMapper;

    /**
     * select user by user id
     * @param userId
     * @return
     */
    @GetMapping("/selectUserById")
    public Result selectUserById(@RequestParam("userId") Integer userId) {
        return Result.success(userMapper.selectUserById(userId));
    }

    /**
     * get all users
     * code:1 represents getting all users succeeded.
     * @return Result
     */
    @GetMapping("/getAllUsers")
    public Result getAllUsers() {
        return accountService.getAllUsers();
    }

    /**
     * login
     * code:0 represents user does not exist.
     * code:1 represents login succeeded.
     * code:2 represents login failed. The password is incorrect.
     * code:3 represents login failed. The user is banned.
     * @param loginInfo
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginInfo loginInfo) {
        return accountService.loginCheck(loginInfo.getUserNumber(), loginInfo.getPassword());
    }

    /**
     * logout
     * code:2 represents logout failed. The account does not exist.
     * code:1 represents logout succeeded.
     * code:0 represents logout failed.
     * logout
     * @param userNumber
     * @return Result
     */
    @PostMapping("/logout")
    public Result logout(@RequestParam("userNumber") String userNumber) {
        return accountService.logout(userNumber);
    }

    /**
     * register send verification code.
     * code:1 represents sending verification code succeeded.
     * code:0 represents register failed. The account has been existed.
     * @param userNumber
     * @return Result
     */
    @PostMapping("/register")
    public Result registerSendVerificationCode(@RequestParam("userNumber") String userNumber) throws MessagingException {
        return accountService.registerSendVerificationCode(userNumber);
    }

    /**
     * register check verification code.
     * code:2 represents register failed. The password is not the same.
     * code:1 represents register succeeded.
     * code:0 represents register failed. The verification code is wrong.
     *
     * @param verificationCodeInfo
     * @return Result
     */
    @PostMapping("/registerCheck")
    public Result registerCheckVerificationCode(@RequestBody VerificationCodeInfo verificationCodeInfo) {
        return accountService.registerCheckVerificationCode(
                verificationCodeInfo.getUserNumber(),
                verificationCodeInfo.getVerificationCode(),
                verificationCodeInfo.getPassword(),
                verificationCodeInfo.getCheckPassword()
        );
    }

    /**
     * forget password send verification code.
     * code:1 represents sending verification code successfully.
     * code:0 represents sending password failed. The account does not exist.
     * @param userNumber
     * @return Result
     */
    @PostMapping("/forgetPassword")
    public Result forgetPasswordSendVerificationCode(@RequestParam("userNumber") String userNumber) throws MessagingException {
        return accountService.forgetPasswordSendVerificationCode(userNumber);
    }

    /**
     * forget password check verification code.
     * code:3 represents checking verification code failed. The account does not exist.
     * code:2 represents checking verification code failed. The password is not the same.
     * code:1 represents checking verification code successfully.
     * code:0 represents the verification code is wrong.
     * @param verificationCodeInfo
     * @return Result
     */
    @PostMapping("/forgetPasswordCheck")
    public Result forgetPasswordCheckVerificationCode(@RequestBody VerificationCodeInfo verificationCodeInfo) {
        return accountService.forgetPasswordCheckVerificationCode(
                verificationCodeInfo.getUserNumber(),
                verificationCodeInfo.getVerificationCode(),
                verificationCodeInfo.getPassword(),
                verificationCodeInfo.getCheckPassword()
        );
    }

    /**
     * update password.
     * code:2 represents updating password failed. The account is banned.
     * code:1 represents updating password successfully.
     * code:0 represents updating password failed. The account does not exist.
     * @param userInfo
     * @return Result
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UserLoginInfo userInfo) {
        return accountService.updatePassword(userInfo.getUserNumber(), userInfo.getPassword());
    }

    /**
     * appeal account.
     * code:3 represents appealing account failed. The account does not exist.
     * code:2 represents appealing account succeeded.
     * code:1 represents appealing account failed. The account has been appealed.
     * code:0 represents appealing account failed. The account is valid.
     * @param userAppealInfo
     * @return Result
     */
    @PostMapping("/appeal")
    public Result appealForAccount(@RequestBody UserAppealInfo userAppealInfo) {
        return accountService.appealAccount(userAppealInfo.getUserNumber(), userAppealInfo.getAppealContent());
    }

    /**
     * update user status by user id.
     * code:1 represents updating user status successfully.
     * code:0 represents updating user status failed. The account does not exist.
     * @param userStatusInfo
     * @return Result
     */
    @PostMapping("/updateUserStatus")
    public Result updateUserStatus(@RequestBody UserStatusInfo userStatusInfo) {
        return accountService.updateUserStatusByUserId(userStatusInfo.getUserStudentNumber(), userStatusInfo.getStatus());
    }

    /**
     * 通过学号查找用户
     * code 0: 用户不存在
     * code 1: 成功
     * @param userStudentNumber 学号
     * @return 用户信息
     */
    @GetMapping("/getUserByStudentNumber")
    public Result getUserByStudentNumber(@RequestParam("userStudentNumber") String userStudentNumber) {
        return accountService.getUserByStudentNumber(userStudentNumber);
    }
}