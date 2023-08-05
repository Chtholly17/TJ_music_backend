package com.example.tj_music.controller;

import com.example.tj_music.db.entity.Origin;
import com.example.tj_music.service.OriginService;
import com.example.tj_music.object.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/origin")
public class OriginController {
    // please use the logger to print the log
    private static Logger log = Logger.getLogger("UserController.class");
    // user service
    @Autowired // auto-inject
    private OriginService originService;

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

    /**
     * get origin by origin id.
     * code:0 represents origin does not exist.
     * code:1 represents search succeeded.
     * @param originId
     * @return Result
     */
    @PostMapping("/getOriginByOriginId")
    public Result getOriginByOriginId(@RequestParam("originId") Integer originId) {
        Origin origin = originService.getOriginByOriginId(originId);
        if (origin == null)
            return new Result(0, "origin does not exist", null);
        else
            return new Result(1, "search succeeded", origin);
    }

    @GetMapping("/getOriginList")
    public Result getOriginList() {
        return originService.getOriginList();
    }

    @DeleteMapping("/deleteOrigin")
    public Result deleteOrigin(@RequestParam("originId") Integer originId) {
        return originService.deleteOrigin(originId);
    }

    /**
     * update origin tag by origin id
     * code:0 represents origin does not exist.
     * code:1 represents update succeeded.
     * @param originId
     * @param originTag
     * @return Result
     */
    @PostMapping("/updateOriginTag")
    public Result updateOriginTag(@RequestParam("originId") Integer originId,
                                  @RequestParam("originTag") String originTag) {
        Origin origin = originService.getOriginByOriginId(originId);
        if (origin == null)
            return new Result(0, "origin does not exist", null);
        originService.updateOriginTag(originId, originTag);
        return new Result(1, "update succeeded", null);
    }
}
