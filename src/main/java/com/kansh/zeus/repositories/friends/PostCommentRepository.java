package com.kansh.zeus.repositories.friends;

import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Long> {
    List<PostCommentEntity> findByPostId(Long postId);
}
