package com.kansh.zeus.domain.dto.trainings;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingItemInputDto {

    private Integer itemType; // 1 - exercise, 2 - superset

    @Builder.Default
    private Long itemId = null; // if itemType == 1 then exerciseId, if itemType == 2 then supersetId

    private List<SeriesDto> series;
}
