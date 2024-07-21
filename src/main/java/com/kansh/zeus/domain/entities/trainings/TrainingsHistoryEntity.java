package com.kansh.zeus.domain.entities.trainings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "training_history")
public class TrainingsHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private TrainingsEntity training;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Timestamp startTime;

    @Column(nullable = false)
    private Timestamp endTime;

    @Column(nullable = false)
    private int duration;

    @Column
    private String note;

}

