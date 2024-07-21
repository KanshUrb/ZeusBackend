package com.kansh.zeus.repositories.friends;

import com.kansh.zeus.domain.entities.friends.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p WHERE p.createdBy.id IN :usersId")
    Page<PostEntity> findAllByCreatedByIn(@Param("usersId") List<String> usersId, Pageable pageable);

}
