package com.example.tj_music.service;

import com.example.tj_music.db.entity.User;
import com.example.tj_music.db.mapper.AppealMapper;
import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.object.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.Authenticator;
import javax.mail.internet.MimeMessage;


@Service
public class AccountService {
    @Autowired
    private AppealMapper appealMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * get all users
     * code:1 represents getting all users succeeded.
     * @return Result
     */
    public Result getAllUsers() {
        return new Result(1, "Getting all users succeeded.", userMapper.getAllUsers());
    }

    /**
     * logout.
     * code:2 represents logout failed. The user is not online.
     * code:1 represents logout succeeded.
     * code:0 represents logout failed. The user does not exist.
     * @param userNumber
     * @return Result
     */
    public Result logout(String userNumber) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned"))
            return new Result(0, "Logout failed. The user does not exist.", null);
        if (Objects.equals(user.getUserStatus(), "online")) {
            userMapper.updateUserStatusById("normal", user.getUserId());
            return new Result(1, "Logout succeeded.", null);
        }
        else
            return new Result(2, "Logout failed. The user is not online.", null);
    }

    /**
     * login check.
     * code:0 represents user does not exist.
     * code:1 represents login succeeded.
     * code:2 represents login failed. The password is incorrect.
     * code:3 represents login failed. The user is banned.
     * @param userNumber
     * @param password
     * @return Result
     */
    public Result loginCheck(String userNumber, String password) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "deleted"))
            return new Result(0, "Login failed. The user does not exist.", null);
        else if (Objects.equals(user.getUserStatus(), "banned"))
            return new Result(3, "Login failed. The user is banned.", null);
        else if (Objects.equals(user.getUserPassword(), password)) {
            return new Result(1, "Login succeeded.", null);
        }
        else
            return new Result(2, "Login failed. The password is incorrect.", null);
    }

    /**
     * send verification code.
     * code:1 represents the verification code sent successfully.
     * @param userNumber
     * @return Result
     */
    public Result sendVerificationCode(String userNumber) throws MessagingException, AddressException {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        Random random = new Random();
        String verificationCode = String.valueOf(random.nextInt(89999) + 10000);
        if (user == null)
            userMapper.insertUser(userNumber, "null", "unsigned", userNumber, "这个人很神秘，没有留下签名", "http://120.46.60.40:8888/admin/images/test_avatar.jpg");

        userMapper.updateUserVerificationCodeByStudentNumber(verificationCode, userNumber);

        String email = userNumber + "@tongji.edu.cn";
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "SMTP");//发送邮件协议
        properties.setProperty("mail.host", "smtp.qq.com");//发送邮件的服务器地址
        properties.setProperty("mail.smtp.auth", "true");//需要验证

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("982017264@qq.com", "xhamshqextmpbbcd");
            }
        };

        Session session = Session.getInstance(properties, auth);
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("982017264@qq.com"));//设置发送人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));//设置接收人
        message.setSubject("邮箱验证");//设置邮件主题
        message.setText("你的验证码为：" + verificationCode + "。请注意，验证码有效时间为2分钟！！！");//设置邮件内容
        Transport.send(message);

        return new Result(1, "send verification code succeeded", null);
    }

    /**
     * register send verification code.
     * code:1 represents sending verification code succeeded.
     * code:0 represents register failed. The account has been existed.
     * @param userNumber
     * @return Result
     */
    public Result registerSendVerificationCode(String userNumber) throws MessagingException {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "deleted"))
            return sendVerificationCode(userNumber);
        else
            return new Result(0, "Register failed. The account has been existed", null);
    }

    /**
     * register check verification code.
     * code:2 represents register failed. The password is not the same.
     * code:1 represents register succeeded.
     * code:0 represents register failed. The verification code is wrong.
     * @param userNumber
     * @param password
     * @param checkPassword
     * @param verificationCode
     * @return Result
     */
    public Result registerCheckVerificationCode(String userNumber, String password, String verificationCode, String checkPassword) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (Objects.equals(verificationCode, user.getUserIdentifyingCode())) {
            if (!Objects.equals(password, checkPassword))
                return new Result(2, "Register failed. The password is not the same", null);
            userMapper.updateUserStatusById("normal", user.getUserId());
            userMapper.updateUserPasswordByStudentNumber(password, userNumber);
            // we set the userNumber as the default nickname
            userMapper.updateUserNicknameByStudentNumber(userNumber, userNumber);

            return new Result(1, "Register succeeded", null);

        } else
            return new Result(0, "Register failed. The verification code is wrong", null);
    }

    /**
     * forget password send verification code.
     * code:1 represents sending verification code successfully.
     * code:0 represents sending password failed. The account does not exist.
     * @param userNumber
     * @return Result
     */
    public Result forgetPasswordSendVerificationCode(String userNumber) throws MessagingException {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user != null && Objects.equals(user.getUserStatus(), "normal"))
            return sendVerificationCode(userNumber);
        else if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "deleted"))
            return new Result(0, "Forget password failed. The account does not exist", null);
        else
            return new Result(3, "Forget password failed. The user is banned", null);
    }

    /**
     * forget password check verification code.
     * code:3 represents forget password failed. The account does not exist.
     * code:2 represents the password is not the same.
     * code:1 represents checking verification code successfully.
     * code:0 represents the verification code is wrong.
     * @param userNumber
     * @param verificationCode
     * @param password
     * @param checkPassword
     * @return Result
     */
    public Result forgetPasswordCheckVerificationCode(String userNumber, String verificationCode, String password, String checkPassword) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned"))
            return new Result(3, "Forget password failed. The account does not exist", null);
        if (Objects.equals(verificationCode, user.getUserIdentifyingCode())) {
            if (!Objects.equals(password, checkPassword))
                return new Result(2, "Forget password failed. The password is not the same", null);
            else {
                userMapper.updateUserPasswordByStudentNumber(password, userNumber);
                return new Result(1, "Forget password successfully.", null);
            }
        }
        else
            return new Result(0, "Forget password failed. The verification code is wrong", null);
    }

    /**
     * update password.
     * code:2 represents updating password failed. The user is banned.
     * code:1 represents updating password successfully.
     * code:0 represents updating password failed. The account does not exist.
     * @param userNumber
     * @param password
     * @return Result
     */
    public Result updatePassword(String userNumber, String password) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user != null && Objects.equals(user.getUserStatus(), "normal")) {
            userMapper.updateUserPasswordByStudentNumber(userNumber, password);
            return new Result(1, "Updating password succeeded", null);
        } else if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "deleted"))
            return new Result(0, "Updating password failed. The account does not exist", null);
        else
            return new Result(3, "Updating password failed. The user is banned", null);
    }

    /**
     * appeal account.
     * code:3 represents appealing account failed. The account does not exist.
     * code:2 represents appealing account succeeded.
     * code:1 represents appealing account failed. The account has been appealed.
     * code:0 represents appealing account failed. The account is valid.
     * @param userNumber
     * @param appealContent
     * @return Result
     */
    public Result appealAccount(String userNumber, String appealContent) {
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user != null && !Objects.equals(user.getUserStatus(), "unsigned") &&
                !Objects.equals(user.getUserStatus(), "deleted")) {
            if (Objects.equals(user.getUserStatus(), "normal"))
                return new Result(0, "Appealing account failed. The account is valid", null);

            List<Integer> appealOwnerList = appealMapper.selectAppealOwnerById(user.getUserId());
            if (appealOwnerList.contains(user.getUserId()))
                return new Result(1, "Appealing account failed. The account has been appealed", null);
            else {
                appealMapper.insertAppeal(user.getUserId(), appealContent);
                return new Result(2, "Appealing account succeeded", null);
            }
        } else
            return new Result(3, "Appeal account failed. The account does not exist", null);
    }

    /**
     * delete user by student number.
     * possible code:1 user not found
     * @param userNumber student number to delete
     * @return Result
     */
    public Result deleteUserByStudentNumber(String userNumber) {
        //check whether the user exists
        User user = userMapper.selectUserByStudentNumber(userNumber);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "deleted"))
            return new Result(0, "Delete user failed. The user does not exist", null);
        else {
            userMapper.updateUserStatusById("deleted", user.getUserId());
            userMapper.updateUserNicknameByStudentNumber("该账号已注销", userNumber);
            return Result.success();
        }
    }

    /**
     * update user status by user id.
     * code 1: update user status successfully
     * code 0: update user status failed
     * @param userStudentNumber
     * @param userStatus
     * @return Result
     */
    public Result updateUserStatusByUserId(String userStudentNumber, String userStatus) {
        //check whether the user exists
        User user = userMapper.selectUserByStudentNumber(userStudentNumber);
        if (user == null)
            return new Result(0, "Update user status failed. The user does not exist", null);
        else {
            userMapper.updateUserStatusById(userStatus, user.getUserId());
            return new Result(1, "Update user status successfully", null);
        }
    }

    public Result getUserByStudentNumber(String userStudentNumber){
        User user = userMapper.selectUserByStudentNumber(userStudentNumber);
        if (user == null)
            return new Result(0, "Get user failed. The user does not exist", null);
        else
            return new Result(1, "Get user successfully", user);
    }
}