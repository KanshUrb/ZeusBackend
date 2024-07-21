package com.kansh.zeus.controllers;

//TODO na 11.07
// - controller dla Socialmedia i trening
// - refaktor (a sie przyda)
// - dokonczyc treningi
// - zrobic jakies pobranie wszystkich bodyparams uzytkownika dla danego okresu
// - obejrzec tutorial do fluttera

//TODO na 12.07-14.07
// jakakolwiek implementacja klienta

import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repositories.friends.FriendRepository;
import com.kansh.zeus.repositories.friends.PostRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.SocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Slf4j
public class SocialMediaServiceIntegrationTests {

    @Autowired
    private SocialMediaService socialMediaService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private UsersEntity user1;
    private UsersEntity user2;

    @BeforeEach
    public void setUp() {
        user1 = new UsersEntity();
        user1.setId("user1");
        user1.setFirstName("User");
        user1.setLastName("One");
        userRepository.save(user1);

        user2 = new UsersEntity();
        user2.setId("user2");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setHash("123");
        userRepository.save(user2);
    }

    @Test
    public void testAddAndRemoveFriend() {
        socialMediaService.addFriend(user1.getId(), user2.getHash());
        List<FriendEntity> friends = socialMediaService.getFriends(user1.getId());
        assertThat(friends).hasSize(1);

        socialMediaService.removeFriend(user1.getId(), user2.getId());
        friends = socialMediaService.getFriends(user1.getId());
        assertThat(friends).isEmpty();
    }

    @Test
    public void testCreateAndGetPost() {
        PostEntity post = new PostEntity();
        post.setContent("This is a test post");
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(post);

        Optional<PostEntity> retrievedPost = Optional.ofNullable(socialMediaService.getPostById(savedPost.getId()));
        assertThat(retrievedPost).isPresent();
        assertThat(retrievedPost.get().getContent()).isEqualTo("This is a test post");
    }

    @Test
    public void testUpdateAndDeletePost() {
        PostEntity post = new PostEntity();
        post.setContent("This is a test post");
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(post);

        savedPost.setContent("Updated content");
        PostEntity updatedPost = socialMediaService.updatePost(savedPost.getId(), savedPost);
        assertThat(updatedPost.getContent()).isEqualTo("Updated content");

        socialMediaService.deletePost(updatedPost.getId());
        Optional<PostEntity> deletedPost = postRepository.findById(updatedPost.getId());
        assertThat(deletedPost).isEmpty();
    }

    @Test
    public void testLikeAndUnlikePost() {
        PostEntity post = new PostEntity();
        post.setContent("This is a test post");
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(post);
        log.info(savedPost.toString());

        socialMediaService.likePost(savedPost.getId(), user2.getId());
        PostEntity likedPost = socialMediaService.getPostById(savedPost.getId());
        log.info(likedPost.toString());
        assertThat(likedPost.getLikesCount()).isEqualTo(1);

        socialMediaService.unlikePost(savedPost.getId(), user2.getId());
        PostEntity unlikedPost = socialMediaService.getPostById(savedPost.getId());
        assertThat(unlikedPost.getLikesCount()).isEqualTo(0);
    }

    @Test
    public void testCommentOnPost() {
        PostEntity post = new PostEntity();
        post.setContent("This is a test post");
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(post);

        PostCommentEntity comment = new PostCommentEntity();
        comment.setComment("This is a test comment");
        comment.setUser(user2);
        socialMediaService.commentOnPost(savedPost.getId(), comment);

        List<PostCommentEntity> comments = socialMediaService.getCommentsForPost(savedPost.getId());
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getComment()).isEqualTo("This is a test comment");
    }

    @Test
    public void testGetFriendsPosts() {
        PostEntity post1 = new PostEntity();
        post1.setContent("Post by user1");
        post1.setCreatedBy(user1);
        socialMediaService.createPost(post1);

        PostEntity post2 = new PostEntity();
        post2.setContent("Post by user2");
        post2.setCreatedBy(user2);
        socialMediaService.createPost(post2);

        socialMediaService.addFriend(user1.getId(), user2.getHash());

        Pageable pageable = PageRequest.of(0, 10);
        List<PostEntity> friendsPosts = socialMediaService.getPosts(user1.getId(), pageable).getContent();
        assertThat(friendsPosts).hasSize(1);
        assertThat(friendsPosts.get(0).getContent()).isEqualTo("Post by user2");
    }
}

