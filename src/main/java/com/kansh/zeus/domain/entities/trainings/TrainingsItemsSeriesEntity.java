package com.kansh.zeus.domain.entities.trainings;

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
@Table(name = "training_item_series")
public class TrainingsItemsSeriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_item_id", nullable = false)
    private TrainingsItemsEntity trainingItem;

    @Column(nullable = false)
    private int seriesNumber;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private Float weight;

}
