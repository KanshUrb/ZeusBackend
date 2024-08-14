package com.kansh.zeus.domain.dto.trainings;

import com.kansh.zeus.domain.dto.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTrainingsDto {

    private TrainingsDto training;

    private UserDto user;

    private UserDto sharedWith;
}
