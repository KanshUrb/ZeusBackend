package com.kansh.zeus.domain.entities.trainings;

import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "training_items")
public class TrainingsItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private TrainingsEntity training;

    @Column(nullable = false)
    private int itemType; // 0 for exercise, 1 for superset

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExercisesEntity exercise;

    @ManyToOne
    @JoinColumn(name = "superset_id")
    private SupersetsEntity superset;

}
