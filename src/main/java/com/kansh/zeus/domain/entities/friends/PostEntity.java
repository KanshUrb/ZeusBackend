package com.kansh.zeus.domain.entities.friends;

import com.kansh.zeus.domain.entities.users.UsersEntity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UsersEntity createdBy;

    @Builder.Default
    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default
    @Column(name = "likes_count")
    private Integer likesCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLikeEntity> likes = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostCommentEntity> comments = new HashSet<>();
}