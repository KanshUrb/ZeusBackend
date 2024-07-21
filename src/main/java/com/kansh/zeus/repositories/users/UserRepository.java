package com.kansh.zeus.repositories.users;

import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UsersEntity, Integer> {

    Optional<UsersEntity> findById(String id);

    @Query(value = "SELECT COUNT(*) > 0 FROM UsersEntity WHERE hash = :hash")
    boolean checkIfHashIsAvailable(@Param("hash") String hash);

    Optional<UsersEntity> findByHash(String hash);

}
