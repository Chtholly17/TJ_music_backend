package com.example.tj_music;

import com.example.tj_music.controller.followController;
import com.example.tj_music.service.followService;
import com.example.tj_music.utils.MusicUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TjMusicAdminTests {
    @Autowired
    private com.example.tj_music.service.followService followService;
    @Autowired
    private com.example.tj_music.service.workService workService;
    @Autowired
    private com.example.tj_music.service.workCommentService workCommentService;

    private com.example.tj_music.controller.followController followController;

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
