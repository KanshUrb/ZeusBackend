package com.kansh.zeus.controllers;

import com.kansh.zeus.config.ValidateToken;
import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.dto.friends.PostCommentDto;
import com.kansh.zeus.domain.dto.friends.PostDto;
import com.kansh.zeus.domain.dto.exercises.PageDto;
import com.kansh.zeus.domain.dto.friends.PostLikeDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;
import com.kansh.zeus.mappers.Mapper;
import com.kansh.zeus.repositories.friends.FriendRepository;
import com.kansh.zeus.repositories.users.UserRepository;
import com.kansh.zeus.services.SocialMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequestMapping("/api")
public class SocialMediaController {

    private final SocialMediaService socialMediaService;
    private final ValidateToken validateToken;
    private final Mapper<FriendEntity, FriendDto> friendMapper;
    private final Mapper<PostEntity, PostDto> postMapper;
    private final Mapper<PostCommentEntity, PostCommentDto> postCommentMapper;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public SocialMediaController(SocialMediaService socialMediaService,
                                 ValidateToken validateToken,
                                 Mapper<FriendEntity, FriendDto> friendMapper,
                                 Mapper<PostEntity, PostDto> postMapper,
                                 Mapper<PostCommentEntity, PostCommentDto> postCommentMapper,
                                 UserRepository userRepository, FriendRepository friendRepository) {
        this.socialMediaService = socialMediaService;
        this.validateToken = validateToken;
        this.friendMapper = friendMapper;
        this.postMapper = postMapper;
        this.postCommentMapper = postCommentMapper;
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    @PostMapping("/friends/{friendHash}")
    public ResponseEntity<FriendDto> addFriend(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable String friendHash) {
        log.info("SocialMediaController::addFriend START friendHash = {}", friendHash);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::addFriend ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            FriendEntity addedFriend = socialMediaService.addFriend(userToken.getId(), friendHash);
            if(isNull(addedFriend)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            FriendDto addedFriendDto = FriendDto.builder()
                    .hash(addedFriend.getFriend().getHash())
                    .firstName(addedFriend.getFriend().getFirstName())
                    .lastName(addedFriend.getFriend().getLastName())
                    .photo(addedFriend.getFriend().getPhoto())
                    .build();
            log.info("SocialMediaController::addFriend STOP addedFriend = {}", addedFriend);
            return new ResponseEntity<>(addedFriendDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::addFriend ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/friends/{friendHash}")
    public ResponseEntity<Void> removeFriend(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable String friendHash) {
        log.info("SocialMediaController::removeFriend START friendHash = {}", friendHash);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::removeFriend ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            socialMediaService.removeFriend(userToken.getId(), friendHash);
            log.info("SocialMediaController::removeFriend STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::removeFriend ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDto>> getFriends(@RequestHeader("Authorization") String authorizationHeader) {
        log.info("SocialMediaController::getFriends START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::getFriends ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("SocialMediaController::getFriends ERROR: : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            List<FriendEntity> friends = socialMediaService.getFriends(user.getId());
            log.info(friends.toString());
            List<FriendDto> friendDto = friends.stream().map(e -> isNull(e) ? null : FriendDto.builder()
                    .hash(e.getFriend().getHash())
                    .firstName(e.getFriend().getFirstName())
                    .lastName(e.getFriend().getLastName())
                    .photo(e.getFriend().getPhoto())
                    .build()
            ).toList();
            log.info("SocialMediaController::getFriends STOP, friends = {}", friendDto);
            return new ResponseEntity<>(friendDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::getFriends ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody String content) {
        log.info("SocialMediaController::createPost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::createPost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("BodyPramsController::updateBodyParams ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            PostEntity postEntity = socialMediaService.createPost(user, content);
            log.info("SocialMediaController::createPost STOP");
            PostDto postDto = postMapper.mapTo(postEntity);
            return new ResponseEntity<>(postDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::createPost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable Long postId) {
        log.info("SocialMediaController::getPostById START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::getPostById ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            PostEntity postEntity = socialMediaService.getPostById(postId);
            log.info("SocialMediaController::getPostById STOP");
            return new ResponseEntity<>(postMapper.mapTo(postEntity), HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::getPostById ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<PageDto<PostDto>> getPosts(@RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("SocialMediaController::getPosts START");
        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::getPosts ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PostEntity> posts = socialMediaService.getPosts(userToken.getId(), pageable);
            List<PostDto> postDto = posts.stream().map(postMapper::mapTo).toList();

            PageDto<PostDto> response = PageDto.<PostDto>builder()
                    .result(postDto)
                    .pageNumber(posts.getNumber())
                    .pageSize(posts.getSize())
                    .totalPages(posts.getTotalPages())
                    .totalElements(posts.getTotalElements())
                    .build();

            log.info("SocialMediaController::getPosts STOP, posts = {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("SocialMediaController::getPosts ERROR", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Long postId,
                                              @RequestBody String content) {
        log.info("SocialMediaController::updatePost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::updatePost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            PostEntity updatedPost = socialMediaService.updatePost(postId, content);
            log.info("SocialMediaController::updatePost STOP");
            return new ResponseEntity<>(postMapper.mapTo(updatedPost), HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::updatePost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long postId) {
        log.info("SocialMediaController::deletePost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::deletePost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            socialMediaService.deletePost(postId);
            log.info("SocialMediaController::deletePost STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::deletePost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<PostLikeDto> likePost(@RequestHeader("Authorization") String authorizationHeader,
                                         @PathVariable Long postId) {
        log.info("SocialMediaController::likePost START, postId = {}", postId);

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::likePost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Pair<Integer, Boolean> postLikes = socialMediaService.likePost(postId, userToken.getId());
            PostLikeDto output = PostLikeDto.builder()
                    .likesCount(postLikes.getFirst())
                    .build();
            log.info("SocialMediaController::likePost STOP, output = {}", output);
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::likePost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable Long postId) {
        log.info("SocialMediaController::unlikePost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::unlikePost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            socialMediaService.unlikePost(postId, userToken.getId());
            log.info("SocialMediaController::unlikePost STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::unlikePost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PostMapping("/posts/{postId}/comments")
    public ResponseEntity<PostCommentDto> commentOnPost(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Long postId,
                                              @RequestBody String comment) {
        log.info("SocialMediaController::commentOnPost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::commentOnPost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UsersEntity user = userRepository.findById(userToken.getId()).orElse(null);
            if (isNull(user)) {
                log.error("SocialMediaController::commentOnPost ERROR : User not found!");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            PostCommentEntity commentEntity = postCommentMapper.mapFrom(commentDto);
            commentEntity.setUser(user);
            socialMediaService.commentOnPost(postId, commentEntity);
            log.info("SocialMediaController::commentOnPost STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::commentOnPost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<PostCommentDto>> getCommentsForPost(@RequestHeader("Authorization") String authorizationHeader,
                                                                   @PathVariable Long postId) {
        log.info("SocialMediaController::getCommentsForPost START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::getCommentsForPost ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            List<PostCommentEntity> comments = socialMediaService.getCommentsForPost(postId);
            List<PostCommentDto> commentDto = comments.stream().map(postCommentMapper::mapTo).toList();
            log.info("SocialMediaController::getCommentsForPost STOP");
            return new ResponseEntity<>(commentDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::getCommentsForPost ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@RequestHeader("Authorization") String authorizationHeader,
                                              @PathVariable Long commentId) {
        log.info("SocialMediaController::deleteComment START");

        UserTokenDto userToken = validateToken.validateToken(authorizationHeader);
        if (isNull(userToken)) {
            log.error("SocialMediaController::deleteComment ERROR: Invalid authorization header!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            socialMediaService.deleteComment(commentId);
            log.info("SocialMediaController::deleteComment STOP");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("SocialMediaController::deleteComment ERROR", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
