package com.kansh.zeus.domain.dto.exercises;

import com.kansh.zeus.domain.dto.users.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExercisesDto {

    private Long id;

    private String name;

    private String description;

    private String muscleGroup;

    private Integer difficultyLevel;

    private String videoUrl;

    private Float rate;

    private Integer userCounter;

    private UsersDto createdBy;
}
