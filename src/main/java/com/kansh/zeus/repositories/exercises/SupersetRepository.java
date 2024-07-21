package com.kansh.zeus.repositories.exercises;

import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupersetRepository extends JpaRepository<SupersetsEntity, Long> {

    @Query(value = "SELECT "
            + "  s.id, "
            + "  s.name, "
            + "  s.rate "
            + "FROM SupersetsEntity s "
            + "LEFT JOIN UserSupersetsEntity se ON s.id = se.superset.id "
            + "WHERE s.createdBy IS NULL "
            + "   OR s.createdBy.id = :userId "
            + "   OR se.sharedWith.id = :userId "
            + "GROUP BY s.id ")
    Page<Object[]> findAllSupersetsSummariesAvailableForUser(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT s "
            + "FROM SupersetsEntity s "
            + "LEFT JOIN UserSupersetsEntity se ON s.id = se.superset.id "
            + "WHERE s.id= :supersetId "
            + "  AND (s.createdBy IS NULL "
            + "   OR s.createdBy.id = :userId "
            + "   OR se.sharedWith.id = :userId) "
            + "GROUP BY s.id ")
    Optional<SupersetsEntity> findSupersetByUserAndId(@Param("userId") String userId, @Param("supersetId") Long supersetId);

    @Modifying
    @Transactional
    void deleteByIdAndCreatedBy_Id(Long id, String createdBy);

}
