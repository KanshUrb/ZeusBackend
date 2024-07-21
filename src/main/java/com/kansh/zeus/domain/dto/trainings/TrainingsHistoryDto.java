package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingsHistoryDto {

    private Long id;
    private Long trainingId;
    private String userId;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer duration;
    private String note;

}

