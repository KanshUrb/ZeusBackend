package com.kansh.zeus.unit;

import com.kansh.zeus.TestDataUtil;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.friends.PostLikeEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.repository.friends.FriendRepository;
import com.kansh.zeus.repository.friends.PostCommentRepository;
import com.kansh.zeus.repository.friends.PostLikeRepository;
import com.kansh.zeus.repository.friends.PostRepository;
import com.kansh.zeus.repository.users.UserRepository;
import com.kansh.zeus.service.impl.SocialMediaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SocialMediaServiceUnitTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private PostCommentRepository postCommentRepository;

    @InjectMocks
    private SocialMediaServiceImpl socialMediaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFriend() {
        when(userRepository.findById(TestDataUtil.ID_A)).thenReturn(Optional.of(TestDataUtil.createTestUserEntityA()));
        when(userRepository.findByHash(TestDataUtil.HASH_B)).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));
        when(friendRepository.save(any(FriendEntity.class))).thenReturn(TestDataUtil.createTestFriendEntity());

        FriendEntity friendEntity = socialMediaService.addFriend(TestDataUtil.ID_A, TestDataUtil.HASH_B);

        assertNotNull(friendEntity);
        assertEquals(TestDataUtil.createTestUserEntityB(), friendEntity.getFriend());
        verify(friendRepository, times(1)).save(any(FriendEntity.class));
    }

    @Test
    public void testRemoveFriend() {
        when(userRepository.findByHash(TestDataUtil.HASH_B)).thenReturn(Optional.of(TestDataUtil.createTestUserEntityB()));
        doNothing().when(friendRepository).deleteByUserIdAndFriendId(TestDataUtil.ID_A, TestDataUtil.ID_B);

        socialMediaService.removeFriend(TestDataUtil.ID_A, TestDataUtil.HASH_B);

        verify(friendRepository, times(1)).deleteByUserIdAndFriendId(TestDataUtil.ID_A, TestDataUtil.ID_B);
    }

    @Test
    public void testGetFriends() {
        when(friendRepository.findAllByUserId(TestDataUtil.ID_A)).thenReturn(List.of(TestDataUtil.createTestFriendEntity()));

        List<FriendEntity> friends = socialMediaService.getFriends(TestDataUtil.ID_A);

        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals(TestDataUtil.createTestUserEntityB(), friends.get(0).getFriend());
    }

    @Test
    public void testCreatePost() {
        UsersEntity user = TestDataUtil.createTestUserEntityA();
        PostEntity post = TestDataUtil.createTestPostEntity();
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostEntity createdPost = socialMediaService.createPost(user, TestDataUtil.POST_CONTENT_1);

        assertNotNull(createdPost);
        assertEquals(TestDataUtil.POST_CONTENT_1, createdPost.getContent());
        verify(postRepository, times(1)).save(any(PostEntity.class));
    }

    @Test
    public void testGetPostById() {
        when(postRepository.findById(TestDataUtil.POST_ID_1)).thenReturn(Optional.of(TestDataUtil.createTestPostEntity()));

        PostEntity post = socialMediaService.getPostById(TestDataUtil.POST_ID_1);

        assertNotNull(post);
        assertEquals(TestDataUtil.POST_CONTENT_1, post.getContent());
    }

    @Test
    public void testGetPosts() {
        when(friendRepository.findAllByUserId(TestDataUtil.ID_A)).thenReturn(List.of(TestDataUtil.createTestFriendEntity()));
        when(postRepository.findAllByCreatedByIn(anyList(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(TestDataUtil.createTestPostEntity())));

        Page<PostEntity> posts = socialMediaService.getPosts(TestDataUtil.ID_A, PageRequest.of(0, 10));

        assertNotNull(posts);
        assertEquals(1, posts.getTotalElements());
    }

    @Test
    public void testUpdatePost() {
        PostEntity post = TestDataUtil.createTestPostEntity();
        when(postRepository.findById(TestDataUtil.POST_ID_1)).thenReturn(Optional.of(post));
        when(postRepository.save(any(PostEntity.class))).thenReturn(post);

        PostEntity updatedPost = socialMediaService.updatePost(TestDataUtil.POST_ID_1, "Updated Content");

        assertNotNull(updatedPost);
        assertEquals("Updated Content", updatedPost.getContent());
    }

    @Test
    public void testDeletePost() {
        doNothing().when(postRepository).deleteById(TestDataUtil.POST_ID_1);

        socialMediaService.deletePost(TestDataUtil.POST_ID_1);

        verify(postRepository, times(1)).deleteById(TestDataUtil.POST_ID_1);
    }

    @Test
    public void testLikePost() {
        when(postLikeRepository.existsByPostIdAndUserId(TestDataUtil.POST_ID_1, TestDataUtil.ID_A)).thenReturn(false);
        when(postRepository.findById(TestDataUtil.POST_ID_1)).thenReturn(Optional.of(TestDataUtil.createTestPostEntity()));
        when(userRepository.findById(TestDataUtil.ID_A)).thenReturn(Optional.of(TestDataUtil.createTestUserEntityA()));
        when(postLikeRepository.countByPostId(TestDataUtil.POST_ID_1)).thenReturn(1L);

        Pair<Integer, Boolean> result = socialMediaService.likePost(TestDataUtil.POST_ID_1, TestDataUtil.ID_A);

        assertNotNull(result);
        assertEquals(1, result.getFirst());
        assertTrue(result.getSecond());
        verify(postLikeRepository, times(1)).save(any(PostLikeEntity.class));
    }

    @Test
    public void testUnlikePost() {
        when(postLikeRepository.existsByPostIdAndUserId(TestDataUtil.POST_ID_1, TestDataUtil.ID_A)).thenReturn(true);
        when(postRepository.findById(TestDataUtil.POST_ID_1)).thenReturn(Optional.of(TestDataUtil.createTestPostEntity()));

        socialMediaService.unlikePost(TestDataUtil.POST_ID_1, TestDataUtil.ID_A);

        verify(postLikeRepository, times(1)).deleteByPostIdAndUserId(TestDataUtil.POST_ID_1, TestDataUtil.ID_A);
    }

    @Test
    public void testCommentOnPost() {
        when(postRepository.findById(TestDataUtil.POST_ID_1)).thenReturn(Optional.of(TestDataUtil.createTestPostEntity()));
        when(postCommentRepository.save(any(PostCommentEntity.class))).thenReturn(TestDataUtil.createTestPostCommentEntity());

        PostCommentEntity comment = TestDataUtil.createTestPostCommentEntity();
        socialMediaService.commentOnPost(TestDataUtil.POST_ID_1, comment);

        verify(postCommentRepository, times(1)).save(any(PostCommentEntity.class));
    }

    @Test
    public void testGetCommentsForPost() {
        when(postCommentRepository.findByPostId(TestDataUtil.POST_ID_1)).thenReturn(List.of(TestDataUtil.createTestPostCommentEntity()));

        List<PostCommentEntity> comments = socialMediaService.getCommentsForPost(TestDataUtil.POST_ID_1);

        assertNotNull(comments);
        assertEquals(1, comments.size());
    }

    @Test
    public void testDeleteComment() {
        doNothing().when(postCommentRepository).deleteById(TestDataUtil.COMMENT_ID_1);

        socialMediaService.deleteComment(TestDataUtil.COMMENT_ID_1);

        verify(postCommentRepository, times(1)).deleteById(TestDataUtil.COMMENT_ID_1);
    }
}

