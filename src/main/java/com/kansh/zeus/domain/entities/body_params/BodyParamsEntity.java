package com.kansh.zeus.domain.entities.body_params;

import com.kansh.zeus.domain.entities.users.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "body_params")
public class BodyParamsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_params_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "biceps")
    private Float biceps;

    @Column(name = "chest")
    private Float chest;

    @Column(name = "waist")
    private Float waist;

    @Column(name = "neck")
    private Float neck;

    @Column(name = "hip")
    private Float hip;

    @Column(name = "thigh")
    private Float thigh;

    @Column(name = "bmi")
    private Float bmi;

    @Column(name = "lbm")
    private Float lbm;

    @Column(name = "bf")
    private Float bf;

}
