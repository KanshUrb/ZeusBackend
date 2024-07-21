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
@Table(name = "trainings")
public class TrainingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String note;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UsersEntity createdBy;

}
