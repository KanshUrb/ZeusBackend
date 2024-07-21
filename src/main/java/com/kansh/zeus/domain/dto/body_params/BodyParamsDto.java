package com.kansh.zeus.domain.dto.body_params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodyParamsDto {

    private Long id;

    private LocalDate date;

    private Integer height;

    private Float weight;

    private Float biceps;

    private Float chest;

    private Float waist;

    private Float neck;

    private Float hip;

    private Float thigh;

    private Float bmi;

    private Float lbm;

    private Float bf;
}
