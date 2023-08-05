package com.example.tj_music.service;

import com.example.tj_music.db.entity.Follow;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.db.mapper.FollowMapper;
import com.example.tj_music.db.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper  userMapper;

    /**
     * insert follow by owner and target
     * @param followOwner follow owner
     * @param followTarget follow target
     */
    public void insertFollow(Integer followOwner, Integer followTarget) {
        followMapper.insertFollow(followOwner, followTarget);
    }

    /**
     * delete follow by id
     * @param followId the id of follow to delete
     */
    public void deleteFollow(Integer followId){
        followMapper.deleteFollowById(followId);
    }

    // select the follow by owner, return every follow id and target User
    public List<User> selectFollowByOwner(Integer followOwner){
        List<Follow> followList = followMapper.selectFollowByOwner(followOwner);
        List<User> return_value = new ArrayList<>();
        for (Follow follow : followList) {
            return_value.add(userMapper.selectUserById(follow.getFollowTarget()));
        }
        return return_value;
    }

    // select the follow by target, return every follow id and owner User
    public List<User> selectFollowByTarget(Integer followTarget){
        List<Follow> followList = followMapper.selectFollowByTarget(followTarget);
        List<User> return_value = new ArrayList<>();
        for (Follow follow : followList) {
            return_value.add(userMapper.selectUserById(follow.getFollowOwner()));
        }
        return return_value;
    }

    // select the user by id
    public User selectUserById(Integer userId){
        return userMapper.selectUserById(userId);
    }

    // select the user by student number
    public User selectUserByStudentNumber(String userStudentNumber){
        return userMapper.selectUserByStudentNumber(userStudentNumber);
    }

    // select the follow by owner and target
    public Follow selectFollowByOwnerAndTarget(Integer followOwner, Integer followTarget){
        return followMapper.selectFollowByOwnerAndTarget(followOwner, followTarget);
    }

    // update the user follow cnt by id
    public void updateUserFollowCntById(Integer userId){
        List<User> follow_list= selectFollowByOwner(userId);
        Integer follow_cnt = follow_list.size();
        // then update the user follow cnt
        userMapper.updateUserFollowCntById(follow_cnt,userId);
    }

    // update the user fans cnt by id
    public void updateUserFansCntById(Integer userId){
        // first get the true fans cnt
        List<User> fans_list= selectFollowByTarget(userId);
        Integer fans_cnt = fans_list.size();
        // then update the user fans cnt
        userMapper.updateUserFansCntById(fans_cnt,userId);
    }

    // update the user posts cnt by id
    public void updateUserPostsCntById(Integer userId){
        // first get the true posts cnt
        List<Follow> follow_list= followMapper.selectFollowByTarget(userId);
        Integer posts_cnt = follow_list.size();
        // then update the user posts cnt
        userMapper.updateUserPostsCntById( posts_cnt,userId);
    }


}