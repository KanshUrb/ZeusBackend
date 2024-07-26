package com.kansh.zeus.domain.dto.trainings;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetOutputDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingItemDto {

    private Long id;

    private Integer itemType;

    @Builder.Default
    private ExercisesDto exercise = null;

    @Builder.Default
    private SupersetOutputDto superset = null;

    private List<SeriesDto> series;
}
