package com.kansh.zeus.domain.entities.exercises;

import com.kansh.zeus.domain.entities.users.UsersEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "exercises")
public class ExercisesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "muscle_group")
    private String muscleGroup;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "users_counter")
    private Integer userCounter;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UsersEntity createdBy;

    @PrePersist
    @PostLoad
    private void ensureExerciseRateNotNull() {
        if (this.rate == null) {
            this.rate = 0.0F;
        }
        if (this.userCounter == null) {
            this.userCounter = 0;
        }
    }

}
