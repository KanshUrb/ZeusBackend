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

    private Integer itemType;

    @Builder.Default
    private Long itemId = null;

    private List<SeriesDto> series;
}
