package com.kansh.zeus.integration;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.friends.PostRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.SocialMediaService;
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
        user1 = TestDataUtil.createTestUserEntityA();
        user2 = TestDataUtil.createTestUserEntityB();
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    public void testAddAndRemoveFriend() {
        socialMediaService.addFriend(user1.getId(), user2.getHash());
        List<FriendEntity> friends = socialMediaService.getFriends(user1.getId());
        assertThat(friends).hasSize(1);

        socialMediaService.removeFriend(user1.getId(), user2.getHash());
        friends = socialMediaService.getFriends(user1.getId());
        assertThat(friends).isEmpty();
    }

    @Test
    public void testCreateAndGetPost() {
        PostEntity post = TestDataUtil.createTestPostEntity();
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(user1, post.getContent());

        Optional<PostEntity> retrievedPost = Optional.ofNullable(socialMediaService.getPostById(savedPost.getId()));
        assertThat(retrievedPost).isPresent();
        assertThat(retrievedPost.get().getContent()).isEqualTo("This is a test post content.");
    }

    @Test
    public void testUpdateAndDeletePost() {
        PostEntity post = TestDataUtil.createTestPostEntity();
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(user1, post.getContent());

        PostEntity updatedPost = socialMediaService.updatePost(savedPost.getId(), "Updated content");
        assertThat(updatedPost.getContent()).isEqualTo("Updated content");

        socialMediaService.deletePost(updatedPost.getId());
        Optional<PostEntity> deletedPost = postRepository.findById(updatedPost.getId());
        assertThat(deletedPost).isEmpty();
    }

    @Test
    public void testLikeAndUnlikePost() {
        PostEntity post = TestDataUtil.createTestPostEntity();
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(user1, post.getContent());

        socialMediaService.likePost(savedPost.getId(), user2.getId());
        PostEntity likedPost = socialMediaService.getPostById(savedPost.getId());
        assertThat(likedPost.getLikesCount()).isEqualTo(1);

        socialMediaService.unlikePost(savedPost.getId(), user2.getId());
        PostEntity unlikedPost = socialMediaService.getPostById(savedPost.getId());
        assertThat(unlikedPost.getLikesCount()).isEqualTo(0);
    }

    @Test
    public void testCommentOnPost() {
        PostEntity post = TestDataUtil.createTestPostEntity();
        post.setCreatedBy(user1);
        PostEntity savedPost = socialMediaService.createPost(user1, post.getContent());

        PostCommentEntity comment = TestDataUtil.createTestPostCommentEntity();
        comment.setUser(user2);
        socialMediaService.commentOnPost(savedPost.getId(), comment);

        List<PostCommentEntity> comments = socialMediaService.getCommentsForPost(savedPost.getId());
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getComment()).isEqualTo("This is a test comment.");
    }

    @Test
    public void testGetFriendsPosts() {
        PostEntity post1 = TestDataUtil.createTestPostEntity();
        post1.setContent("Post by user1");
        post1.setCreatedBy(user1);
        socialMediaService.createPost(user1, post1.getContent());

        PostEntity post2 = TestDataUtil.createTestPostEntity();
        post2.setContent("Post by user2");
        post2.setCreatedBy(user2);
        socialMediaService.createPost(user2, post2.getContent());

        socialMediaService.addFriend(user1.getId(), user2.getHash());

        Pageable pageable = PageRequest.of(0, 10);
        List<PostEntity> friendsPosts = socialMediaService.getPosts(user1.getId(), pageable).getContent();
        assertThat(friendsPosts).hasSize(2);
        assertThat(friendsPosts.get(0).getContent()).isEqualTo("Post by user2");
    }

}
