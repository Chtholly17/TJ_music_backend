package com.example.tj_music.object;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class CommentInfo {
    String preciseScore;
    String qualityScore;
    String pitchScore;
}
