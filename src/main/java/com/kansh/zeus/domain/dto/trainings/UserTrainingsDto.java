package com.kansh.zeus.domain.dto.trainings;

import com.kansh.zeus.domain.dto.users.UsersDto;
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

    private UsersDto user;

    private UsersDto sharedWith;
}
