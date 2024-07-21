package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingsHistoryItemsDto {

    private Long id;
    private Long trainingHistoryId;
    private Integer itemType;
    private Long exerciseId;
    private Long supersetId;
    private Integer seriesNumber;
    private Integer repetitions;
    private Float weight;

}

