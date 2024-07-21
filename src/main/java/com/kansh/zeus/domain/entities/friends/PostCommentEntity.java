package com.kansh.zeus.domain.entities.friends;

import com.kansh.zeus.domain.entities.users.UsersEntity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post_comments")
public class PostCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "TEXT")
    private UsersEntity user;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Builder.Default
    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
