package com.kansh.zeus.domain.dto.exercises;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupersetsSummaryDto {

    private Long id;

    private String name;

    private Float rate;
}
