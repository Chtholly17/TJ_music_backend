package com.example.tj_music.object;

import lombok.Data;

@Data
public class WorkInfo {
    String workName;
    String workComment;
    String workOwner;
    int workOriginVersion;
    String workVoiceFilename;
    int workPreciseScore;
    int workQualityScore;
    int workPitchScore;
}
