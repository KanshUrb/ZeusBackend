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
public class SupersetsDto {

    private Long id;

    private String name;

    private ExercisesDto exercise1;

    private ExercisesDto exercise2;

    private Float rate;

    private Integer userCounter;

    private UsersDto createdBy;

}
