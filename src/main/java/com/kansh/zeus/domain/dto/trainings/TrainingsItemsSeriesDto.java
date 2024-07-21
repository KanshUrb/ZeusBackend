package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingsItemsSeriesDto {

    private Long id;
    private Long trainingItemId;
    private Integer seriesNumber;
    private Integer repetitions;
    private Float weight;

}
