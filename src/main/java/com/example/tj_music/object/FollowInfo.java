package com.example.tj_music.object;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@Data
public class FollowInfo {
    String userStudentNumber;
    String targetStudentNumber;
}
