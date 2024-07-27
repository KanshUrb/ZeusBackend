package com.kansh.zeus.domain.dto.exercises;

import com.kansh.zeus.domain.dto.friends.FriendDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseDetailsDto {

    private ExercisesDto exercise;

    private List<FriendDto> sharedWith;

}