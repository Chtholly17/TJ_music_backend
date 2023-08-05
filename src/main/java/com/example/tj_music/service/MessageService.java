package com.example.tj_music.service;

import com.example.tj_music.object.dto.SendMessageDTO;
import com.example.tj_music.object.vo.GetMessageBriefListVO;
import com.example.tj_music.object.vo.GetMessageListVO;
import com.example.tj_music.db.entity.Message;
import com.example.tj_music.db.entity.User;
import com.example.tj_music.db.mapper.MessageMapper;
import com.example.tj_music.db.mapper.UserMapper;
import com.example.tj_music.object.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;


    // insert message by sender and receiver's student number and content
    public Result insertMessage(SendMessageDTO sendMessageDTO) {
        // get the user by student number
        User sender=userMapper.selectUserByStudentNumber(sendMessageDTO.getSenderStudentNumber());
        if(sender==null){
            return Result.fail("sender does not exist");
        }
        User receiver=userMapper.selectUserByStudentNumber(sendMessageDTO.getReceiverStudentNumber());
        if(receiver==null){
            return Result.fail("receiver does not exist");
        }
        messageMapper.insertMessage(sender.getUserId(), receiver.getUserId(), sendMessageDTO.getContent());
        return Result.success("insert message successfully");
    }

    // get message brief by user's student number
    public Result getMessageBrief(String userStudentNumber) {
        User receiver=userMapper.selectUserByStudentNumber(userStudentNumber);
        if(receiver==null){
            return Result.fail("user does not exist");
        }
        List<GetMessageBriefListVO> getMessageBriefListVOList=new ArrayList<>();

        // get all the users that have messages with current user(receiver)
        Set<User> partnerList = new LinkedHashSet<>();
        List<Message> relatedMessage = messageMapper.selectMessageContainUserIdTimeDesc(receiver.getUserId());
        for(int i=0;i<relatedMessage.size();i++){
            if(relatedMessage.get(i).getSenderId()==receiver.getUserId()){
                partnerList.add(userMapper.selectUserById(relatedMessage.get(i).getReceiverId()));
            }
            else{
                partnerList.add(userMapper.selectUserById(relatedMessage.get(i).getSenderId()));
            }
        }

        // iterate all the partner and find out the newest message
        for(User partner:partnerList){
            List<Message> messageList=messageMapper.selectMessageBetweenTwoUserTimeDescLimit(receiver.getUserId(),partner.getUserId(),1);
            GetMessageBriefListVO getMessageBriefListVO=new GetMessageBriefListVO();
            getMessageBriefListVO.setLastMessageContent(messageList.get(0).getContent());
            getMessageBriefListVO.setNickname(partner.getUserNickname());
            getMessageBriefListVO.setStudentNumber(partner.getUserStudentNumber());
            getMessageBriefListVO.setProfileImageFilename(partner.getUserProfileImageFilename());
            getMessageBriefListVOList.add(getMessageBriefListVO);
        }

        return Result.success(getMessageBriefListVOList);
    }

    // get the message list by sender and receiver's student number and descending time
    public Result getMessageListSenderReceiver(String senderStudentNumber, String receiverStudentNumber) {
        User sender=userMapper.selectUserByStudentNumber(senderStudentNumber);
        if(sender==null){
            return Result.fail("sender does not exist");
        }
        User receiver=userMapper.selectUserByStudentNumber(receiverStudentNumber);
        if(receiver==null){
            return Result.fail("receiver does not exist");
        }
        List<Message> messageList=messageMapper.selectBySenderIdAndReceiverIdDescTime(sender.getUserId(),receiver.getUserId());
        return Result.success(messageList);
    }

    // get the message list by sender and receiver's student number and ascending time with limit
    public Result getMessageListSenderReceiverLimit(String senderStudentNumber, String receiverStudentNumber, int limit) {
        User sender=userMapper.selectUserByStudentNumber(senderStudentNumber);
        if(sender==null){
            return Result.fail("sender does not exist");
        }
        User receiver=userMapper.selectUserByStudentNumber(receiverStudentNumber);
        if(receiver==null){
            return Result.fail("receiver does not exist");
        }
        List<Message> messageList=messageMapper.selectBySenderIdAndReceiverIdDescTimeWithLimit(sender.getUserId(),receiver.getUserId(),limit);
        return Result.success(messageList);
    }


    // get the message list given 2 user's student number and ascending time with limit
    public Result getMessageList2UserLimit(String user1StudentNumber, String user2StudentNumber, int limit) {
        User user1=userMapper.selectUserByStudentNumber(user1StudentNumber);
        if(user1==null){
            return Result.fail("user1 does not exist");
        }
        User user2=userMapper.selectUserByStudentNumber(user2StudentNumber);
        if(user2==null){
            return Result.fail("user2 does not exist");
        }
        List<Message> messageList=messageMapper.selectMessageBetweenTwoUserTimeDescLimit(user1.getUserId(),user2.getUserId(),limit);
        List<GetMessageListVO> ret=new ArrayList<>();
        for(int i=0;i<messageList.size();i++){
            GetMessageListVO getMessageListVO=new GetMessageListVO();
            if(messageList.get(i).getSenderId()==user1.getUserId()){
                getMessageListVO.setSenderStudentNumber(user1StudentNumber);
            }
            else{
                getMessageListVO.setSenderStudentNumber(user2StudentNumber);
            }
            getMessageListVO.setMessageContent(messageList.get(i).getContent());
            getMessageListVO.setMessageTime(messageList.get(i).getCreateTime());
            ret.add(getMessageListVO);
        }
        return Result.success(ret);
    }
}
