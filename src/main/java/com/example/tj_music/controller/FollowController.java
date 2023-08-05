package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Follow;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.object.FollowInfo;
import com.example.tj_music.service.FollowService;
import com.example.tj_music.object.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController // @RestController = @Controller + @ResponseBody (return json)
@RequestMapping("/user/follow")
public class FollowController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    // user service
    @Autowired // auto-inject
    private FollowService followService;


    /**
     * 关注某人
     * 通过调用这个API来关注某人 code 0:成功 code 1:用户不存在 code 2:关注已存在 code3:自己关注自己
     * @param followInfo 包含当前发起关注用户的学号和被关注的用户的学号
     * @return Result 成功返回success，失败返回fail,可能出现的message有： user or target does not exist
     */
    @PostMapping("/follow")
    public Result follow(@RequestBody FollowInfo followInfo) {
        // check whether the user and target are the same
        String userStudentNumber = followInfo.getUserStudentNumber();
        String targetStudentNumber = followInfo.getTargetStudentNumber();
        if (userStudentNumber.equals(targetStudentNumber)) {
            return new Result(3, "user and target are the same", null);
        }

        // this api should be called after login so the user does exist
        User user = followService.selectUserByStudentNumber(userStudentNumber);
        User target = followService.selectUserByStudentNumber(targetStudentNumber);

        if (user == null || target == null) {
            return new Result(1, "user or target does not exist", null);
        }
        // check whether the follow already exists
        Follow follow = followService.selectFollowByOwnerAndTarget(user.getUserId(), target.getUserId());
        if (follow != null) {
            return new Result(2, "follow already exists", null);
        }
        followService.insertFollow(user.getUserId(), target.getUserId());
        // update the attribute of currently real data
        followService.updateUserFollowCntById(user.getUserId());
        followService.updateUserFansCntById(target.getUserId());
        return new Result(0, "success", null);
    }


    /**
     * 取消关注某人
     * 通过这个api来取消关注某人 code: 0:成功 code: 1:用户不存在 code: 2:关注不存在
     * @param followInfo 包含当前发起取消关注用户的学号和被取消关注的用户的学号
     * @return Result 成功返回success，失败返回fail,可能出现的message有： user or target does not exist/follow does not exist
     */
    @DeleteMapping("/unfollow")
    public Result unfollow(@RequestBody FollowInfo followInfo) {
        // this api should be called after login so the user does exist
        String userStudentNumber = followInfo.getUserStudentNumber();
        String targetStudentNumber = followInfo.getTargetStudentNumber();
        User user = followService.selectUserByStudentNumber(userStudentNumber);
        User target = followService.selectUserByStudentNumber(targetStudentNumber);
        if (user == null || target == null) {
            return new Result(1, "user or target does not exist", null);
        }
        Follow follow = followService.selectFollowByOwnerAndTarget(user.getUserId(), target.getUserId());
        if (follow == null) {
            return new Result(2, "follow does not exist", null);
        }
        // update the follow cnt and fans cnt
        followService.deleteFollow(follow.getId());
        // update the attribute of currently real data
        followService.updateUserFollowCntById(user.getUserId());
        followService.updateUserFansCntById(target.getUserId());
        return new Result(0, "success", null);
    }


    /**
     * 查看某人的关注列表
     * 通过这个api来查看某人的关注列表,成功返回的是一个list，list中的每一个元素是一个user code: 0:成功 code: 1:用户不存在
     * @param userStudentNumber 当前发起查看关注列表用户的学号
     * @return Result 成功返回success，失败返回fail,可能出现的message有： user does not exist
     */
    @GetMapping("/display_user_following")
    public Result displayUserFollowing(@RequestParam("userStudentNumber") @NotNull String userStudentNumber) {
        // this api should be called after login so the user does exist
        User user = followService.selectUserByStudentNumber(userStudentNumber);
        if(user == null) {
            return new Result(1, "user does not exist", null);
        }
        List<User> followList = followService.selectFollowByOwner(user.getUserId());
        return new Result(0, "success", followList);
    }

    /**
     * 查看某人的粉丝列表 code 0:成功 code 1:用户不存在
     * 通过这个api来查看某人的粉丝列表,成功返回的是一个list，list中的每一个元素是一个user
     * @param userStudentNumber 当前发起查看粉丝列表用户的学号
     * @return Result 成功返回success，失败返回fail,可能出现的message有： user does not exist
     */
    @GetMapping("/display_user_follower")
    public Result displayUserFollower(@RequestParam("userStudentNumber") @NotNull String userStudentNumber) {
        // this api should be called after login so the user does exist
        User user = followService.selectUserByStudentNumber(userStudentNumber);
        if(user == null) {
            return new Result(1, "user does not exist", null);
        }
        List<User> followList = followService.selectFollowByTarget(user.getUserId());
        return new Result(0, "success", followList);
    }

    /**
     * 查看是否已经关注某人
     * 通过这个api来查看是否已经关注某人 code 0:未关注 code 1:已关注 code 2:用户不存在
     * @param followInfo 包含当前发起查看关注用户的学号和被查看的用户的学号
     * @return Result 成功返回success，失败返回fail,可能出现的message有： user or target does not exist/follow does not exist
     */
    @GetMapping("/check_follow")
    public Result checkFollow(@RequestBody FollowInfo followInfo) {
        // this api should be called after login so the user does exist
        String userStudentNumber = followInfo.getUserStudentNumber();
        String targetStudentNumber = followInfo.getTargetStudentNumber();
        User user = followService.selectUserByStudentNumber(userStudentNumber);
        User target = followService.selectUserByStudentNumber(targetStudentNumber);
        if (user == null || target == null) {
            return new Result(2, "user or target does not exist", null);
        }
        Follow follow = followService.selectFollowByOwnerAndTarget(user.getUserId(), target.getUserId());
        if (follow == null) {
            return new Result(0, "not follow", null);
        }
        return new Result(1, "follow", null);
    }
}