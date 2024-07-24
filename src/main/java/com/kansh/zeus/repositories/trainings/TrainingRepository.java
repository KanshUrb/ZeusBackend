package com.kansh.zeus.repositories.trainings;

import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainingRepository extends JpaRepository<TrainingsEntity, Long> {

    @Query(value = "SELECT "
            + "  t "
            + "FROM TrainingsEntity t "
            + "LEFT JOIN UserTrainingsEntity ut ON t.id = ut.training.id "
            + "WHERE t.createdBy IS NULL "
            + "   OR t.createdBy.id = :userId "
            + "   OR ut.sharedWith.id = :userId "
            + "GROUP BY t.id ")
    List<TrainingsEntity> findAllTrainingSummariesAvailableForUser(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT t "
                 + "FROM TrainingsEntity t "
                 + "LEFT JOIN UserTrainingsEntity ut ON t.id = ut.training.id "
                 + "WHERE t.id = :trainingId "
                 + "  AND (t.createdBy.id IS NULL "
                 + "   OR t.createdBy.id = :userId "
                 + "   OR ut.sharedWith.id = :userId) "
                 + "GROUP BY t.id ")
    Optional<TrainingsEntity> findTrainingByUserAndId(@Param("userId") String userId, @Param("trainingId") Long trainingId);

    @Query(value = "SELECT ti "
            + "FROM TrainingsItemsEntity ti "
            + "WHERE ti.training.id = :trainingId ")
    List<TrainingsItemsEntity> findTrainingItemsByTrainingId(Long trainingId);

    @Query(value = "SELECT tis "
            + "FROM TrainingsItemsSeriesEntity tis "
            + "WHERE tis.trainingItem.id = :trainingItemsId ")
    List<TrainingsItemsSeriesEntity> findTrainingItemsSeriesByTrainingItemsId(Long trainingItemsId);

    @Modifying
    @Transactional
    void deleteByIdAndCreatedBy_Id(Long id, String createdBy);

}

