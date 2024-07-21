package com.kansh.zeus.services;

import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SocialMediaService {

    void addFriend(String userId, String friendHash);
    void removeFriend(String userId, String friendId);
    List<FriendEntity> getFriends(String userId);
    PostEntity createPost(PostEntity postEntity);
    PostEntity getPostById(Long postId);
    Page<PostEntity> getPosts(String userId, Pageable pageable);
    PostEntity updatePost(Long postId, PostEntity postEntity);
    void deletePost(Long postId);
    void likePost(Long postId, String userId);
    void unlikePost(Long postId, String userId);
    void commentOnPost(Long postId, PostCommentEntity commentEntity);
    List<PostCommentEntity> getCommentsForPost(Long postId);
    void deleteComment(Long commentId);
}


