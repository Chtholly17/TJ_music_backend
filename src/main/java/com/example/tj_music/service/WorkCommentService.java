package com.example.tj_music.service;

import com.example.tj_music.db.entity.WorkComment;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.db.entity.Work;

import com.example.tj_music.db.mapper.WorkCommentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.tj_music.object.Result;

import java.util.*;

@Service
public class WorkCommentService {
    @Autowired
    private WorkCommentMapper workCommentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkMapper workMapper;

    /**
     * get comments by work id.
     * code:1 represents get comments successfully.
     * code:0 represents get comments failed. There is no comments in the work.
     * @param workId
     * @return Result
     */
    public Result getWorkCommentByWorkId(Integer workId) {
        List<WorkComment> workCommentList = workCommentMapper.selectWorkCommentByTarget(workId);
        if (workCommentList.isEmpty()) {
            return Result.fail("work comment does not exist.");
        }
        List<Map<String, Object>> comments = new ArrayList<>();
        for (WorkComment workComment : workCommentList) {
            Map<String, Object> comment = new HashMap<>();
            User user = userMapper.selectUserById(workComment.getWorkCommentOwner());
            comment.put("workComment", workComment);
            comment.put("workCommentUser", user);
            comments.add(comment);
        }
        return Result.success(comments);
    }

    /**
     * add comment to work.
     * code:4 represents work does not exist.
     * code:2 represents user does not exist.
     * code:1 represents add comment successfully.
     * code:0 represents add comment failed. The comment content is null.
     * @param workCommentTarget
     * @param userStudentNumber
     * @param workCommentContent
     * @return Result
     */
    public Result addWorkComment(Integer workCommentTarget, String userStudentNumber, String workCommentContent) {
        User user = userMapper.selectUserByStudentNumber(userStudentNumber);
        Work work = workMapper.selectWorkByWorkId(workCommentTarget);
        if (user == null || Objects.equals(user.getUserStatus(), "unsigned") ||
                Objects.equals(user.getUserStatus(), "banned"))
            return new Result(2, "user does not exist.", null);
        if (work == null)
            return new Result(4, "work does not exist.", null);
        if (workCommentContent == null)
            return new Result(0, "work comment content is null.", null);

        workCommentMapper.insertWorkComment(workCommentTarget, user.getUserId(), workCommentContent);
        return new Result(1, "add work comment successfully.", null);
    }

    // delete work comment by work_comment_target
    public Result deleteWorkCommentById(Integer workCommentTarget) {
        workCommentMapper.deleteWorkCommentById(workCommentTarget);
        return Result.success("delete work comment successfully.");
    }

}

