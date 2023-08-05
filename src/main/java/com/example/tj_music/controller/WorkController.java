package com.example.tj_music.controller;

import com.example.tj_music.object.WorkInfo;
import com.example.tj_music.service.OriginService;
import com.example.tj_music.service.WorkService;
import com.example.tj_music.object.Result;
import com.example.tj_music.db.entity.Origin;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkController {
    private static Logger log = Logger.getLogger("UserController.class");

    @Autowired
    private WorkService workService;
    @Autowired
    private OriginService originService;

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


    /**
     * 通过tag搜索作品，同时按照order排序
     * tag的可选项: “民谣” "摇滚" "金属" "古典" "电子" "热歌" “新歌” "飙升"
     * order的可选项: "like" "comment" "fans"
     * @param tag
     * @param order
     * @return
     */
    @GetMapping("/getWorkList")
    public Result getWorkList(@RequestParam("tag") String tag, @RequestParam("order") String order) {
        return workService.getWorkList(tag, order);
    }


    /**
     * 通过workId获取作品
     * @param workId 作品id
     * @return
     */
    @GetMapping("/getWorkById")
    public Result getWorkById(@RequestParam("workId") int workId) {
        return workService.getWorkById(workId);
    }

    /**
     * Insert work
     * @param workInfo
     * @return
     */
    @PostMapping("/insertWork")
    public Result insertWork(@RequestBody WorkInfo workInfo) {
        String workPrefaceFilename;
        Origin origin = originService.getOriginByOriginId(workInfo.getWorkOriginVersion());
        if (origin == null) {
            return Result.fail("origin does not exist.");
        }
        return workService.insertWork(
            workInfo.getWorkName(),
            workInfo.getWorkComment(),
            workInfo.getWorkOwner(),
            workInfo.getWorkOriginVersion(),
            0,
            workInfo.getWorkVoiceFilename(),
            origin.getOriginTag(),
            origin.getOriginPrefaceFilename(),
            workInfo.getWorkPreciseScore(),
            workInfo.getWorkQualityScore(),
            workInfo.getWorkPitchScore()
        );
    }

    /**
     * 通过workId给作品点赞
     * 需要提供workId
     * @param workId 作品id
     * @return 成功返回success，失败返回fail(code:0)
     */
    @PostMapping("/addLikeToWork")
    public Result addLikeToWork(@RequestParam("workId") int workId) {
        return workService.addLikeToWork(workId);
    }

}
