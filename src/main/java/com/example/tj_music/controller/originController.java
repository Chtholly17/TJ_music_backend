package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.service.originService;
import com.example.tj_music.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController // @RestController = @Controller + @ResponseBody (return json)
public class originController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    // user service
    @Autowired // auto-inject
    private originService originService;

    /**
     * search origin by key word.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originTag
     * @return Result
     */
    @PostMapping("/searchOriginByTag")
    public Result searchOriginByTag(@RequestParam("originTag") String originTag) {
        return originService.searchOriginByTag(originTag);
    }

    /**
     * search origin by work id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param workId
     * @return Result
     */
    @PostMapping("/searchOriginByWorkId")
    public Result searchOriginByWorkId(@RequestParam("workId") Integer workId) {
        return originService.searchOriginByWorkId(workId);
    }
}