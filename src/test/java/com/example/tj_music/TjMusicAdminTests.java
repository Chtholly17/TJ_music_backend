package com.example.tj_music;

import com.example.tj_music.controller.FollowController;
import com.example.tj_music.service.FollowService;
import com.example.tj_music.service.WorkCommentService;
import com.example.tj_music.service.WorkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TjMusicAdminTests {
    @Autowired
    private FollowService followService;
    @Autowired
    private WorkService workService;
    @Autowired
    private WorkCommentService workCommentService;

    private FollowController followController;

    @Test
    void test_delete_work_by_id(){
        System.out.println("----- delete work by id ------");
        int workId = 55;
        workService.deleteWorkAndCommentById(workId);
    }

    // test the mp3 duration calculation
    @Test
    void test_mp3_duration() {
        System.out.println("----- mp3 duration ------");
        String mp3Path = "test.mp3";
        // check whether the file exists using the path

        // MusicUtils musicUtils = new MusicUtils();
        // System.out.println(musicUtils.getMp3Duration(mp3Path));
    }
}
