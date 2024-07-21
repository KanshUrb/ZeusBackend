package com.kansh.zeus.domain.entities.exercises;

import com.kansh.zeus.domain.entities.users.UsersEntity;
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
@Table(name = "supersets")
public class SupersetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "exercise_id_1")
    private ExercisesEntity exercise1;

    @ManyToOne
    @JoinColumn(name = "exercise_id_2")
    private ExercisesEntity exercise2;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "users_counter")
    private Integer userCounter;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UsersEntity createdBy;

    @PrePersist
    @PostLoad
    private void ensureRateNotNull() {
        if (this.rate == null) {
            this.rate = 0.0F;
        }
        if (this.userCounter == null) {
            this.userCounter = 0;
        }
    }

}
