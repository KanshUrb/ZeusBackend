package com.kansh.zeus.repositories.friends;

import com.kansh.zeus.domain.entities.friends.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    List<FriendEntity> findAllByUserId(String userId);
    Integer getIdByUserIdAndFriendId(String userId, String friendId);
    void deleteByUserIdAndFriendId(String userId, String friendId);
}

