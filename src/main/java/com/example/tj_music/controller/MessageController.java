package com.example.tj_music.controller;


import com.example.tj_music.object.dto.SendMessageDTO;
import com.example.tj_music.service.MessageService;
import com.example.tj_music.object.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 发送消息
     * 通过调用该接口以通过发送者和接收者的学号发送消息
     * @param senderStudentNumber 发送者学号
     * @param receiverStudentNumber 接收者学号
     * @param content 消息内容
     * @return 可能出现的错误信息
     */
    @PostMapping("/sendMessage")
    public Result sendMessage(@RequestParam String senderStudentNumber,
                              @RequestParam String receiverStudentNumber,
                              @RequestParam String content) {
        SendMessageDTO sendMessageDTO=new SendMessageDTO();
        sendMessageDTO.setSenderStudentNumber(senderStudentNumber);
        sendMessageDTO.setReceiverStudentNumber(receiverStudentNumber);
        sendMessageDTO.setContent(content);
        return messageService.insertMessage(sendMessageDTO);
    }

    /**
     * 获取简略消息列表
     * 通过用户学号来获得该用户的简略消息列表：包含发送者昵称、发送者头像、最后一条消息内容
     * @param userStudentNumber 用户学号
     * @return
     */
    @GetMapping("/getMessageBrief")
    public Result getMessageBrief(@RequestParam String userStudentNumber) {
        return messageService.getMessageBrief(userStudentNumber);
    }


    /**
     * 获取两个用户间消息列表
     * 提供两个用户的学号，以及获取消息的数量，返回两个用户间的消息列表
     * @param user1StudentNumber 用户1学号
     * @param user2StudentNumber 用户2学号
     * @return
     */
    @GetMapping("/getMessageList")
    public Result getMessageListTwoUser(@RequestParam String user1StudentNumber,
                                        @RequestParam String user2StudentNumber){
        return messageService.getMessageList2UserLimit(user1StudentNumber,user2StudentNumber,10);
    }


}
