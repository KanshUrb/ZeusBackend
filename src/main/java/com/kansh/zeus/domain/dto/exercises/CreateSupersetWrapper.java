package com.kansh.zeus.domain.dto.exercises;

import com.kansh.zeus.domain.dto.users.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSupersetWrapper {

    private SupersetsDto superset;

    private UsersDto user;

    private List<String> sharedWith;
}
