package com.kansh.zeus.domain.dto.exercises;

import com.kansh.zeus.domain.dto.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExercisesDto {

    private UserDto user;

    private ExercisesDto exercise;

    private UserDto sharedWith;
}
