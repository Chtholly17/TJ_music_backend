package com.example.tj_music;

import com.example.tj_music.db.entity.Message;
import com.example.tj_music.db.mapper.MessageMapper;
import com.example.tj_music.service.MessageService;
import com.example.tj_music.object.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TjMusicMessageTests {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageService messageService;

    // test getAllMessage
    @Test
    public void testGetAllMessage() {
        System.out.println(("----- getAllMessage method test ------"));
        //List<Message>
        System.out.println("----- List<Message> ------");
        List<Message> return_value = messageMapper.getAllMessage();
        System.out.println(return_value);
    }

    // test insertMessage
    @Test
    public void testInsertMessage() {
        System.out.println(("----- insertMessage method test ------"));
        //void
        System.out.println("----- void ------");
        messageMapper.insertMessage(3, 1, "test construction");

        //select
        System.out.println("----- select ------");
        List<Message> return_value = messageMapper.selectBySenderIdAndReceiverIdDescTime(1, 2);
        System.out.println(return_value);

        // select
        System.out.println("----- select ------");
        return_value = messageMapper.selectByReceiverIdDescTime(2);
        System.out.println(return_value);
    }

    @Test
    public void testMessageBrief(){
        System.out.println(("----- testMessageBrief method test ------"));

        System.out.println("----- List<Message> ------");
        Result return_value = messageService.getMessageBrief("20170101");
        System.out.println(return_value);
    }
}
