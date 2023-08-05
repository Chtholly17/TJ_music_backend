package com.example.tj_music;

import com.example.tj_music.db.entity.Work;
import com.example.tj_music.db.mapper.WorkMapper;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.service.InformationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TjMusicInformationTests {

    @Autowired
    private InformationService informationService;

    // test getInformationById
    @Test
    public void testGetInformationById() {
        System.out.println(("----- getInformationById method test ------"));
        //User
        System.out.println("----- User ------");
        User return_value = informationService.getInformationById(1);
        System.out.println(return_value);
    }
    @Autowired
    private WorkMapper workMapper;
    @Test
    public void test(){
        List<Work> works = workMapper.selectWorkByTagUserFansDesc("热歌");
        for(Work work:works){
            System.out.println(work);
        }
    }
}
