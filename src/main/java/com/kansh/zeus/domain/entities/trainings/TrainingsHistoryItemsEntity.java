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
@Table(name = "training_history_items")
public class TrainingsHistoryItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_history_id", nullable = false)
    private TrainingsHistoryEntity trainingHistory;

    @Column(nullable = false)
    private int itemType; // 0 for exercise, 1 for superset

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private ExercisesEntity exercise;

    @ManyToOne
    @JoinColumn(name = "superset_id")
    private SupersetsEntity superset;

    @Column(nullable = false)
    private int seriesNumber;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private Float weight;

}

