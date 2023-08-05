package com.example.tj_music.controller;

import com.example.tj_music.db.entity.AppealContent;
import com.example.tj_music.db.entity.OriginFrontEnd;
import com.example.tj_music.object.AppealAcceptInfo;
import com.example.tj_music.object.AppealRejectInfo;
import com.example.tj_music.service.AccountService;
import com.example.tj_music.service.AdministerService;
import com.example.tj_music.object.Result;
import com.example.tj_music.service.OriginService;
import com.example.tj_music.service.WorkService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.List;

@RestController
@RequestMapping("/user/administer")
public class AdministerController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    // user service
    @Autowired // auto-inject
    private AdministerService administerService;
    @Autowired
    private WorkService workService;
    @Autowired
    private OriginService originService;
    @Autowired
    private AccountService accountService;

    /**
     * 获取所有申诉
     * 用这个API可以获取所有申诉
     * @param
     * @return: list of objects: [appealId, ownerStudentNumber,appealContent]
     */
    @GetMapping("/allAppeals")
    public Result allAppeals() {
        // get all appeals
        List<List<Object>> appeals = administerService.selectAllAppeals();
        return Result.success(appeals);
    }

    /**
     * 通过申诉
     * 输入申诉的ID和申诉者的StudentNumber，通过申诉
     * @param appealAcceptInfo: 需要通过的申诉的ID和申诉者的StudentNumber
     * @return Result(success)
     */
    @PostMapping("/acceptAppeal")
    public Result acceptAppeal(@RequestBody AppealAcceptInfo appealAcceptInfo) {
        administerService.acceptAppeal(appealAcceptInfo.getAppealId(),appealAcceptInfo.getUserStudentNumber());
        return Result.success();
    }

    /**
     * 拒绝申诉
     * 输入申诉的ID和拒绝理由，拒绝申诉
     * @param appealRejectInfo: 需要拒绝的申诉的ID和拒绝理由
     * @return Result(success)
     */
    @PostMapping("/rejectAppeal")
    public Result rejectAppeal(@RequestBody AppealRejectInfo appealRejectInfo) {
        administerService.rejectAppeal(appealRejectInfo.getAppealId(),appealRejectInfo.getRejectReason());
        return Result.success();
    }

    /**
     * 根据id删除指定的作品以及其所有评论
     * 可能的返回值：Result(success) or Result(fail) for work not found
     * @param workId 作品id
     * @return Result(success) or Result(fail) for work not found
     */
    @PostMapping("/deleteWorkAndCommentById")
    public Result deleteWorkAndCommentById(@RequestParam("workId") int workId) {
        boolean result = workService.deleteWorkAndCommentById(workId);
        if(result) {
            return Result.success();
        } else {
            return Result.fail("work not found");
        }
    }

    /**
     * 上传原唱(文件传输有bug)
     * @param request
     * @details:
     * @return
     */
    @PostMapping("/insertOrigin")
    public Result insertOrigin(MultipartHttpServletRequest request) {
        OriginFrontEnd originFrontEnd = new OriginFrontEnd();
        // insert a new origin
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)){

            MultipartFile file = request.getFile("originBgMusicFile");
            // print out the file name
            System.out.println(file.getOriginalFilename());

        }else{
            System.out.println("没有文件上传");
        }
        String originId = request.getParameter("originId");
        // convert originId to int
        Integer originIdInt = Integer.parseInt(originId);


        originFrontEnd.setOriginName(request.getParameter("originName"));
        originFrontEnd.setOriginAuthor(request.getParameter("originAuthor"));
        originFrontEnd.setoriginBgMusicFile(request.getFile("originBgMusicFile"));
        originFrontEnd.setOriginVoiceFile(request.getFile("originVoiceFile"));
        originFrontEnd.setOriginPrefaceFile(request.getFile("originPrefaceFile"));
        originFrontEnd.setOriginIntroduction(request.getParameter("originIntroduction"));
        originFrontEnd.setOriginLrcFile(request.getFile("originLrcFile"));
        originFrontEnd.setOriginMusicTag(request.getParameter("originMusicTag"));
        originService.insertOrigin(originFrontEnd,originIdInt);
        return Result.success();
    }

    /**
     * get all appeal content, appeal status and student number
     * @return
     */
    @GetMapping("/allAppealContent")
    public Result allAppealContent() {
        List<AppealContent> appealContent = administerService.getAllAppealContentAndStatusAndStudentNumber();
        return Result.success(appealContent);
    }

    /**
     * 通过学号删除用户
     * @param studentNumber 学号
     * @return Result(success) or Result(fail) for user not found
     */
    @PostMapping("/deleteUserByStudentNumber")
    public Result deleteUserByStudentNumber(@RequestParam("studentNumber") String studentNumber) {
        return accountService.deleteUserByStudentNumber(studentNumber);
    }
}
