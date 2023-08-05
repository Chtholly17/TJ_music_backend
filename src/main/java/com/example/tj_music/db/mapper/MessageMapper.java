package com.example.tj_music.db.mapper;

import com.example.tj_music.db.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    // select------------------------------------------------
    // get all messages
    @Select("select * from message")
    public List<Message> getAllMessage();

    // select by sender id
    @Select("select * from message where sender_id = #{senderId}")
    public List<Message> selectBySenderId(Integer senderId);

    // select by receiver id, and sort by time desc
    @Select("select * from message where receiver_id = #{receiverId} order by create_time desc")
    public List<Message> selectByReceiverIdDescTime(Integer receiverId);


    // select by sender id and receiver id by time desc
    @Select("select * from message where sender_id = #{senderId} and receiver_id = #{receiverId} order by create_time desc")
    public List<Message> selectBySenderIdAndReceiverIdDescTime(Integer senderId, Integer receiverId);

    // select by sender id and receiver id by time desc with limit
    @Select("select * from message where sender_id = #{senderId} and receiver_id = #{receiverId} order by create_time desc limit #{limit}")
    public List<Message> selectBySenderIdAndReceiverIdDescTimeWithLimit(Integer senderId, Integer receiverId, Integer limit);

    // select by message id
    @Select("select * from message where message_id = #{messageId}")
    public Message selectByMessageId(Integer messageId);

    // select all the user that send message to the given receiver
    @Select("select distinct sender_id from message where receiver_id = #{receiverId}")
    public List<Integer> selectAllSenderIdByReceiverId(Integer receiverId);

    // select messages between two users and sort by time desc and limit
    @Select("SELECT * FROM message "+
            "WHERE (sender_id = #{user1Id} AND receiver_id = #{user2Id}) "+
            "OR (sender_id = #{user2Id} AND receiver_id = #{user1Id} ) "+
            "ORDER BY create_time DESC" +
            " LIMIT #{limit}")
    public List<Message> selectMessageBetweenTwoUserTimeDescLimit(Integer user1Id, Integer user2Id, Integer limit);

    // select messages that the given user is the sender or receiver and sort by time desc
    @Select("SELECT * FROM message "+
            "WHERE sender_id = #{userId} OR receiver_id = #{userId} "+
            "ORDER BY create_time DESC")
    public List<Message> selectMessageContainUserIdTimeDesc(Integer userId);



    // insert------------------------------------------------
    // insert message
    @Insert("insert into message( sender_id,receiver_id, content) values(#{senderId}, #{receiverId}, #{content})")
    void insertMessage(Integer senderId, Integer receiverId, String content);
}
