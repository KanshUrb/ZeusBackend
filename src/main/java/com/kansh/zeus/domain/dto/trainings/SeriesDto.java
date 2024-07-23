package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeriesDto {

    private Long id;

    private Integer repetitions;

    private Float weight1;

    @Builder.Default
    private Float weight2 = 0.0F;
}
