package com.example.tj_music.object.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetMessageListVO {
    String senderStudentNumber;
    String messageContent;
    LocalDateTime messageTime;
}
