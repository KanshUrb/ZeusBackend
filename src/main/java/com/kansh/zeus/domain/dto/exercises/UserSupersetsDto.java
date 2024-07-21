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
public class UserSupersetsDto {

    private UsersDto user;

    private ExercisesDto exercise1;

    private ExercisesDto exercise2;

    private UsersDto sharedWith;
}
