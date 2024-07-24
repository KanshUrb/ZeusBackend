package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingSummaryDto {

    private Long id;

    private String name;

    private String note;

}

