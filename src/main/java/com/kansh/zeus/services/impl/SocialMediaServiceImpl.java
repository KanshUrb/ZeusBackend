package com.kansh.zeus.services.impl;

import java.sql.Timestamp;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostLikeEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.friends.FriendRepository;
import com.kansh.zeus.repositories.friends.PostRepository;
import com.kansh.zeus.repositories.friends.PostCommentRepository;
import com.kansh.zeus.repositories.friends.PostLikeRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.SocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SocialMediaServiceImpl implements SocialMediaService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;

    public SocialMediaServiceImpl(FriendRepository friendRepository, UserRepository userRepository,PostRepository postRepository, PostLikeRepository postLikeRepository, PostCommentRepository postCommentRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
    }

    @Override
    @Transactional
    public FriendEntity addFriend(String userId, String friendHash) {
        Optional<UsersEntity> user = userRepository.findById(userId);
        Optional<UsersEntity> friend = userRepository.findByHash(friendHash);
        if (user.isPresent() && friend.isPresent()) {
            FriendEntity friendEntity = FriendEntity.builder()
                    .user(user.get())
                    .friend(friend.get())
                    .build();
            return friendRepository.save(friendEntity);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void removeFriend(String userId, String friendHash) {
        Optional<UsersEntity> friend = userRepository.findByHash(friendHash);
        if(friend.isPresent()) {
            friendRepository.deleteByUserIdAndFriendId(userId, friend.get().getId());
        } else {
            throw new RuntimeException("Friend not found");
        }
    }

    @Override
    public List<FriendEntity> getFriends(String userId) {
        return friendRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public PostEntity createPost(UsersEntity user, String postContent) {
        log.info("SocialMediaServiceImpl::createPost START");
        log.info("SocialMediaServiceImpl::createPost user = {}, postContent = {}", user, postContent);
        PostEntity postEntity = PostEntity.builder()
                .content(postContent)
                .createdBy(user)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        PostEntity savedPost = postRepository.save(postEntity);
        log.info("SocialMediaServiceImpl::createPost STOP, createdPost = {}", savedPost);
        return savedPost;
    }

    @Override
    public PostEntity getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public Page<PostEntity> getPosts(String userId, Pageable pageable) {
        List<FriendEntity> friends = friendRepository.findAllByUserId(userId);
        List<String> friendIds = friends.stream().map(friend -> friend.getFriend().getId()).collect(Collectors.toList());
        return postRepository.findAllByCreatedByIn(friendIds, pageable);
    }

    @Override
    @Transactional
    public PostEntity updatePost(Long postId, PostEntity postEntity) {
        PostEntity existingPost = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        existingPost.setContent(postEntity.getContent());
        return postRepository.save(existingPost);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void likePost(Long postId, String userId) {
        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            PostLikeEntity like = new PostLikeEntity();
            like.setPost(postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found")));
            like.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            postLikeRepository.save(like);

            PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            post.setLikesCount(postLikeRepository.countByPostId(postId).intValue());
            postRepository.save(post);
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, String userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            postLikeRepository.deleteByPostIdAndUserId(postId, userId);

            PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            post.setLikesCount(postLikeRepository.countByPostId(postId).intValue());
            postRepository.save(post);
        }
    }

    @Override
    @Transactional
    public void commentOnPost(Long postId, PostCommentEntity commentEntity) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        commentEntity.setPost(post);
        postCommentRepository.save(commentEntity);
    }

    @Override
    public List<PostCommentEntity> getCommentsForPost(Long postId) {
        return postCommentRepository.findByPostId(postId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        postCommentRepository.deleteById(commentId);
    }
}
