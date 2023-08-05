package com.example.tj_music.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageDTO {
    String senderStudentNumber;
    String receiverStudentNumber;
    String content;
}
