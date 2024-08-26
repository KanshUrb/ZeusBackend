package com.kansh.zeus.repository.friends;

import com.kansh.zeus.domain.entities.friends.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    Long countByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, String userId);
    void deleteByPostIdAndUserId(Long postId, String userId);
}
