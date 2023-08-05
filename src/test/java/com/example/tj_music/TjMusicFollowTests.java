package com.example.tj_music;

import com.example.tj_music.controller.FollowController;
import com.example.tj_music.db.entity.Follow;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.service.FollowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TjMusicFollowTests {

    @Autowired
    private FollowService followService;
    private FollowController followController;



    // test selectFollowByOwner
    @Test
    public void testSelectFollowByOwner() {
        System.out.println(("----- selectFollowByOwner method test ------"));
        //User
        System.out.println("----- User ------");
        List<User> return_value = followService.selectFollowByOwner(1);

        for (User user : return_value) {
            System.out.println(user);
        }
    }

    // test selectFollowByTarget
    @Test
    public void testSelectFollowByTarget() {
        System.out.println(("----- selectFollowByTarget method test ------"));
        //User
        System.out.println("----- User ------");
        List<User> return_value = followService.selectFollowByTarget(2);

        for (User user : return_value) {
            System.out.println(user);
        }
    }

    // test the select follow by owner and target student name
    @Test
    public void testSelectFollowByOwnerAndTarget() {
        System.out.println(("----- selectFollowByOwnerAndTarget method test ------"));
        User owner = followService.selectUserByStudentNumber("20170101");
        User target = followService.selectUserByStudentNumber("20170102");
        if (owner == null || target == null) {
            System.out.println("owner or target is null");
            return;
        }

        Follow returned_follow = followService.selectFollowByOwnerAndTarget(owner.getUserId(), target.getUserId());
        if (returned_follow == null) {
            System.out.println("returned follow is null");
            return;
        }else{
            System.out.println(returned_follow);
        }

    }

    // test the follow by owner and target student number
    @Test
    public void testFollowByOwnerAndTarget() {
        System.out.println(("----- followByOwnerAndTarget method test ------"));
        User owner = followService.selectUserByStudentNumber("20170102");
        User target = followService.selectUserByStudentNumber("20170101");
        if (owner == null || target == null) {
            System.out.println("owner or target is null");
            return;
        }

        followService.insertFollow(owner.getUserId(), target.getUserId());
    }

    // test the unfollowing by owner and target student number
    @Test
    public void testUnfollowByOwnerAndTarget() {
        System.out.println(("----- unfollowByOwnerAndTarget method test ------"));
        User owner = followService.selectUserByStudentNumber("20170102");
        User target = followService.selectUserByStudentNumber("20170101");
        if (owner == null || target == null) {
            System.out.println("owner or target is null");
            return;
        }
        // get the follow by owner and target
        Follow returned_follow = followService.selectFollowByOwnerAndTarget(owner.getUserId(), target.getUserId());
        if (returned_follow == null) {
            System.out.println("returned follow is null");
            return;
        }else{
            followService.deleteFollow(returned_follow.getId());
        }
    }

    @Test
    public void testUpdateFollow() {
//        System.out.println(("----- updateFollow method test ------"));
//        FollowController.follow("20170101","20170102" );
//        Integer current_follow_cnt= FollowService.selectUserByStudentNumber("20170101").getUserFollowCnt();
//        assert current_follow_cnt == 1;
//        Integer current_follower_cnt= FollowService.selectUserByStudentNumber("20170102").getUserFansCnt();
//        assert current_follower_cnt == 1;
//        FollowController.unfollow("20170101","20170102" );
//        current_follow_cnt= FollowService.selectUserByStudentNumber("20170101").getUserFollowCnt();
//        assert current_follow_cnt == 0;
//        current_follower_cnt= FollowService.selectUserByStudentNumber("20170102").getUserFansCnt();
//        assert current_follower_cnt == 0;

    }
}
