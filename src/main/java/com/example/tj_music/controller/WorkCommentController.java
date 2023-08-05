package com.example.tj_music.controller;

import com.example.tj_music.object.WorkCommentInfo;
import com.example.tj_music.service.WorkCommentService;
import com.example.tj_music.object.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController // @RestController = @Controller + @ResponseBody (return json)
public class WorkCommentController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    // user service
    @Autowired // auto-inject
    private WorkCommentService workCommentService;

    /**
     * get comments by work id.
     * code:1 represents get comments successfully.
     * code:0 represents get comments failed. There is no comments in the work.
     * @param workId
     * @return Result
     */
    @GetMapping("/getWorkCommentByWorkId")
    public Result getWorkCommentByWorkId(@RequestParam("workId") Integer workId) {
        return workCommentService.getWorkCommentByWorkId(workId);
    }

    /**
     * add comment to work.
     * code:4 represents work does not exist.
     * code:3 represents user is not login.
     * code:2 represents user does not exist.
     * code:1 represents add comment successfully.
     * code:0 represents add comment failed. The comment content is null.
     * @param workCommentInfo
     * @return Result
     */
    @PostMapping("/addWorkComment")
    public Result addWorkComment(@RequestBody WorkCommentInfo workCommentInfo) {
        Integer workCommentTarget = workCommentInfo.getWorkCommentTarget();
        String userStudentNumber = workCommentInfo.getUserStudentNumber();
        String workCommentContent = workCommentInfo.getWorkCommentContent();
        return workCommentService.addWorkComment(workCommentTarget, userStudentNumber, workCommentContent);
    }
}
