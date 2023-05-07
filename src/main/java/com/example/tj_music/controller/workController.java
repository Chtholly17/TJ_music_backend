package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Work;
import com.example.tj_music.service.workService;
import com.example.tj_music.utils.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class workController {
    private static Logger log = Logger.getLogger("UserController.class");

    @Autowired
    private workService workService;

    /**
     * get related works by origin id.Use for origin detail page
     * @param originId: origin id
     * @return Result, data is a list of work can be null.
     */
    @GetMapping("/relatedWork")
    public Result selectRelatedWorks(@RequestParam("originId") int originId) {
        return workService.selectWorkByOriginId(originId);
    }

    /**
     * get work in main page, with highest n like numbers.
     * @param workNumber: work number
     * @return Result, data is a list of work, length is workNumber.
     */
    @GetMapping("/mainPageWorks")
    public Result getMainPageWorks(@RequestParam("workNumber") int workNumber) {
        return workService.getNWorks(workNumber);
    }

}