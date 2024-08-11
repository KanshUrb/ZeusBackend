package com.kansh.zeus.domain.dto.exercises;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupersetOutputDto {

    private Long id;

    private String name;

    private ExercisesDto exercise1;

    private ExercisesDto exercise2;

    private Float rate;

    private Integer userCounter;

    private String createdBy;

}
