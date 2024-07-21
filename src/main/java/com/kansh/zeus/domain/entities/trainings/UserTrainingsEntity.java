package com.kansh.zeus.domain.entities.trainings;

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
@Table(name = "user_trainings")
public class UserTrainingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private TrainingsEntity training;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @ManyToOne
    @JoinColumn(name = "shared_with")
    private UsersEntity sharedWith;
}
