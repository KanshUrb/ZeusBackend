package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingsItemsDto {

    private Long id;
    private Long trainingId;
    private Integer itemType;
    private Long exerciseId;
    private Long supersetId;

}

