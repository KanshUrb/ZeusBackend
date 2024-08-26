package com.kansh.zeus.service;

import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface SocialMediaService {

    FriendEntity addFriend(String userId, String friendHash);

    void removeFriend(String userId, String friendId);

    List<FriendEntity> getFriends(String userId);

    PostEntity createPost(UsersEntity user, String postContent);

    PostEntity getPostById(Long postId);

    Page<PostEntity> getPosts(String userId, Pageable pageable);

    PostEntity updatePost(Long postId, String postContent);

    void deletePost(Long postId);

    Pair<Integer, Boolean> likePost(Long postId, String userId);

    void unlikePost(Long postId, String userId);

    void commentOnPost(Long postId, PostCommentEntity commentEntity);

    List<PostCommentEntity> getCommentsForPost(Long postId);

    void deleteComment(Long commentId);

}


