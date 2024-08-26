package com.kansh.zeus;

import com.kansh.zeus.domain.dto.body_params.BodyParamsDto;
import com.kansh.zeus.domain.dto.exercises.*;
import com.kansh.zeus.domain.dto.friends.FriendDto;
import com.kansh.zeus.domain.dto.trainings.SeriesDto;
import com.kansh.zeus.domain.dto.trainings.TrainingItemDto;
import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.friends.FriendEntity;
import com.kansh.zeus.domain.entities.friends.PostCommentEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsEntity;
import com.kansh.zeus.domain.entities.trainings.TrainingsItemsSeriesEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TestDataUtil {

    private TestDataUtil() {
    }

    public static final String ID_A = "U83rEkVPo4XJj87dm2NnSfXlS123";
    public static final String FIRST_NAME_A = "Artur";
    public static final String LAST_NAME_A = "Nowak";
    public static final String EMAIL_A = "test@test.com";
    public static final Integer GENDER_A = 1;
    public static final String HASH_A = "123456";
    public static final String PHOTO_A = "https://example.org/photo.jpg";

    public static final LocalDate DATE_A_1 = LocalDate.parse("2023-01-01");
    public static final Integer HEIGHT_A_1 = 180;
    public static final Float WEIGHT_A_1 = 75.0f;
    public static final Float BICEPS_A_1 = 32.0f;
    public static final Float CHEST_A_1 = 102.0f;
    public static final Float WAIST_A_1 = 80.0f;
    public static final Float NECK_A_1 = 40.0f;
    public static final Float HIP_A_1 = 95.0f;
    public static final Float THIGH_A_1 = 55.0f;
    public static final Float BMI_A_1 = 23.15f;
    public static final Float BF_A_1 = 16.54f;
    public static final Float LBM_A_1 = 62.57f;

    public static final LocalDate DATE_A_2 = LocalDate.parse("2023-06-01");
    public static final Integer HEIGHT_A_2 = 180;
    public static final Float WEIGHT_A_2 = 78.0f;
    public static final Float BICEPS_A_2 = 34.0f;
    public static final Float CHEST_A_2 = 105.0f;
    public static final Float WAIST_A_2 = 82.0f;
    public static final Float NECK_A_2 = 41.0f;
    public static final Float HIP_A_2 = 97.0f;
    public static final Float THIGH_A_2 = 57.0f;
    public static final Float BMI_A_2 = 24.07f;
    public static final Float BF_A_2 = 17.23f;
    public static final Float LBM_A_2 = 64.58f;

    public static final LocalDate DATE_A_3 = LocalDate.parse("2023-12-01");
    public static final Integer HEIGHT_A_3 = 180;
    public static final Float WEIGHT_A_3 = 80.0f;
    public static final Float BICEPS_A_3 = 36.0f;
    public static final Float CHEST_A_3 = 108.0f;
    public static final Float WAIST_A_3 = 84.0f;
    public static final Float NECK_A_3 = 42.0f;
    public static final Float HIP_A_3 = 99.0f;
    public static final Float THIGH_A_3 = 59.0f;
    public static final Float BMI_A_3 = 24.69f;
    public static final Float BF_A_3 = 17.87f;
    public static final Float LBM_A_3 = 65.74f;

    public static final String ID_B = "U83rEkVPo4XJj87dm2NnSfXlS124";
    public static final String FIRST_NAME_B = "Jenny";
    public static final String LAST_NAME_B = "Rose";
    public static final String EMAIL_B = "test1@test.com";
    public static final Integer GENDER_B = 2;
    public static final String HASH_B = "654321";
    public static final String PHOTO_B = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Pat_Mat.jpg/800px-Pat_Mat.jpg";

    public static final Long EXERCISE_ID_1 = 1L;
    public static final Long USER_EXERCISE_ID_1 = 4L;
    public static final String EXERCISE_NAME_1 = "Push-up";
    public static final String EXERCISE_DESC_1 = "Push-up description";
    public static final String EXERCISE_MUSCLE_GROUP_1 = "Chest";
    public static final Integer EXERCISE_DIFFICULTY_1 = 3;
    public static final String EXERCISE_VIDEO_URL_1 = "http://video.url";
    public static final Float EXERCISE_RATE_1 = 0.0F;
    public static final Integer EXERCISE_USER_COUNTER_1 = 0;
    public static final Long EXERCISE_ID_2 = 2L;
    public static final Long USER_EXERCISE_ID_2 = 5L;
    public static final String EXERCISE_NAME_2 = "Squat";
    public static final String EXERCISE_DESC_2 = "Squat description";
    public static final String EXERCISE_MUSCLE_GROUP_2 = "Legs";
    public static final Integer EXERCISE_DIFFICULTY_2 = 4;
    public static final String EXERCISE_VIDEO_URL_2 = "http://video.url/squat";
    public static final Float EXERCISE_RATE_2 = 4.0F;
    public static final Integer EXERCISE_USER_COUNTER_2 = 20;
    public static final Long EXERCISE_ID_3 = 3L;
    public static final String EXERCISE_NAME_3 = "Pull-up";
    public static final String EXERCISE_DESC_3 = "Pull-up description";
    public static final String EXERCISE_MUSCLE_GROUP_3 = "Back";
    public static final Integer EXERCISE_DIFFICULTY_3 = 5;
    public static final String EXERCISE_VIDEO_URL_3 = "http://video.url/pullup";
    public static final Float EXERCISE_RATE_3 = 4.8F;
    public static final Integer EXERCISE_USER_COUNTER_3 = 15;

    public static final Long SUPERSET_ID_1 = 1L;
    public static final Float SUPERSET_RATE_1 = 4.5F;
    public static final String SUPERSET_NAME_1 = "Superset 1";
    public static final Long SUPERSET_ID_2 = 2L;
    public static final Float SUPERSET_RATE_2 = 4.0F;
    public static final String SUPERSET_NAME_2 = "Superset 2";

    public static final Long TRAINING_ID_1 = 1L;
    public static final String TRAINING_NAME_1 = "Full Body Workout";
    public static final String TRAINING_NOTE_1 = "This is a note for the full body workout.";

    public static final Long TRAINING_ID_2 = 2L;

    public static final Long TRAINING_ITEM_ID_1 = 101L;
    public static final Long SERIES_ID_1 = 1001L;
    public static final Integer ITEM_TYPE_EXERCISE = 1;

    public static final Long POST_ID_1 = 1L;
    public static final String POST_CONTENT_1 = "This is a test post content.";
    public static final Long COMMENT_ID_1 = 101L;
    public static final String COMMENT_CONTENT_1 = "This is a test comment.";

    public static final String SYSTEM_TRAINING_NAME_1 = "System Training 1";
    public static final Float SERIES_WEIGHT_1 = 20.0F;
    public static final Integer SERIES_REPS_1 = 10;


    public static UsersEntity createTestUserEntityA() {
        return UsersEntity.builder()
                .id(ID_A)
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .email(EMAIL_A)
                .gender(GENDER_A)
                .hash(HASH_A)
                .photo(PHOTO_A)
                .build();
    }

    public static UserDto createTestUserDtoA() {
        return UserDto.builder()
                .id(ID_A)
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .hash(HASH_A)
                .photo(PHOTO_A)
                .build();
    }

    public static UserInputDto createTestUserInputDtoA() {
        return UserInputDto.builder()
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .gender(GENDER_A)
                .photo(PHOTO_A)
                .build();
    }

    public static UserTokenDto createTestUserTokenA() {
        return UserTokenDto.builder()
                .id(ID_A)
                .email(EMAIL_A)
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .photo(PHOTO_A)
                .build();
    }

    public static FriendDto createFriendDtoA() {
        return FriendDto.builder()
                .hash(HASH_A)
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .photo(PHOTO_A)
                .build();
    }

    public static FriendDto createFriendDtoB() {
        return FriendDto.builder()
                .hash(HASH_B)
                .firstName(FIRST_NAME_B)
                .lastName(LAST_NAME_B)
                .photo(PHOTO_B)
                .build();
    }

    public static PostEntity createTestPostEntity() {
        return PostEntity.builder()
                .id(POST_ID_1)
                .content(POST_CONTENT_1)
                .createdBy(createTestUserEntityA())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public static PostCommentEntity createTestPostCommentEntity() {
        return PostCommentEntity.builder()
                .id(COMMENT_ID_1)
                .comment(COMMENT_CONTENT_1)
                .post(createTestPostEntity())
                .user(createTestUserEntityA())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public static FriendEntity createTestFriendEntity() {
        return FriendEntity.builder()
                .user(createTestUserEntityA())
                .friend(createTestUserEntityB())
                .build();
    }

    public static ExerciseDetailsDto createExerciseDetailsDtoB() {
        return ExerciseDetailsDto.builder()
                .exercise(createTestExerciseDto2())
                .sharedWith(List.of(createFriendDtoA()))
                .build();
    }

    public static SupersetWrapperDto createSupersetWrapperDtoA() {
        return SupersetWrapperDto.builder()
                .sharedWith(List.of(createFriendDtoB()))
                .superset(createSupersetDtoA())
                .build();
    }

    public static SupersetsDto createSupersetDtoA() {
        return SupersetsDto.builder()
                .id(SUPERSET_ID_1)
                .name(SUPERSET_NAME_1)
                .rate(SUPERSET_RATE_1)
                .exercise1(EXERCISE_ID_1)
                .exercise2(EXERCISE_ID_2)
                .build();
    }

    public static SupersetsEntity createSupersetEntityA() {
        return SupersetsEntity.builder()
                .id(SUPERSET_ID_1)
                .name(SUPERSET_NAME_1)
                .rate(SUPERSET_RATE_1)
                .exercise1(createTestExerciseEntity1())
                .exercise2(createTestExerciseEntity2())
                .build();
    }

    public static SupersetsEntity createUserSupersetEntityA() {
        return SupersetsEntity.builder()
                .name(SUPERSET_NAME_1)
                .rate(SUPERSET_RATE_1)
                .exercise1(createTestExerciseEntity1())
                .exercise2(createTestExerciseEntity2())
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static SupersetsEntity createSupersetEntityB() {
        return SupersetsEntity.builder()
                .id(SUPERSET_ID_2)
                .name(SUPERSET_NAME_2)
                .rate(SUPERSET_RATE_2)
                .exercise1(createTestExerciseEntity2())
                .exercise2(createTestExerciseEntity3())
                .build();
    }

    public static SupersetsEntity createUserSupersetEntityB() {
        return SupersetsEntity.builder()
                .name(SUPERSET_NAME_2)
                .rate(SUPERSET_RATE_2)
                .exercise1(createTestExerciseEntity2())
                .exercise2(createTestExerciseEntity3())
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static TrainingsItemsEntity createTestTrainingItemEntity1() {
        return TrainingsItemsEntity.builder()
                .id(TRAINING_ITEM_ID_1)
                .itemType(ITEM_TYPE_EXERCISE)
                .exercise(createTestExerciseEntity1())
                .training(createTestTrainingEntity1())
                .build();
    }

    public static TrainingsItemsSeriesEntity createTestTrainingItemSeriesEntity1() {
        return TrainingsItemsSeriesEntity.builder()
                .id(SERIES_ID_1)
                .repetitions(10)
                .weight1(50.0f)
                .weight2(0.0f)
                .trainingItem(createTestTrainingItemEntity1())
                .seriesNumber(1)
                .build();
    }

    public static TrainingItemDto createTestTrainingItemDto1() {
        return TrainingItemDto.builder()
                .id(TRAINING_ITEM_ID_1)
                .itemType(ITEM_TYPE_EXERCISE)
                .exercise(createTestExerciseDto1())
                .series(List.of(createSeriesDto1()))
                .build();
    }

    public static TrainingItemDto createTestTrainingItemDto2() {
        return TrainingItemDto.builder()
                .id(TRAINING_ITEM_ID_1)
                .itemType(ITEM_TYPE_EXERCISE)
                .exercise(createTestExerciseDto2())
                .series(List.of(createSeriesDto1()))
                .build();
    }

    public static TrainingsEntity createTestUserTrainingEntity1() {
        return TrainingsEntity.builder()
                .id(TRAINING_ID_2)
                .name(TRAINING_NAME_1)
                .note(TRAINING_NOTE_1)
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static TrainingsEntity createTestTrainingEntity1() {
        return TrainingsEntity.builder()
                .id(TRAINING_ID_1)
                .name(TRAINING_NAME_1)
                .note(TRAINING_NOTE_1)
                .build();
    }

    public static UsersEntity createTestUserEntityB() {
        return UsersEntity.builder()
                .id(ID_B)
                .firstName(FIRST_NAME_B)
                .lastName(LAST_NAME_B)
                .email(EMAIL_B)
                .gender(GENDER_B)
                .hash(HASH_B)
                .photo(PHOTO_B)
                .build();
    }

    public static BodyParamsEntity createTestBodyParamsEntityA1() {
        return BodyParamsEntity.builder()
                .user(createTestUserEntityA())
                .date(DATE_A_1)
                .height(HEIGHT_A_1)
                .weight(WEIGHT_A_1)
                .biceps(BICEPS_A_1)
                .chest(CHEST_A_1)
                .waist(WAIST_A_1)
                .neck(NECK_A_1)
                .hip(HIP_A_1)
                .thigh(THIGH_A_1)
                .bmi(BMI_A_1)
                .lbm(LBM_A_1)
                .bf(BF_A_1)
                .build();
    }

    public static BodyParamsEntity createTestBodyParamsEntityA2() {
        return BodyParamsEntity.builder()
                .user(createTestUserEntityA())
                .date(DATE_A_2)
                .height(HEIGHT_A_2)
                .weight(WEIGHT_A_2)
                .biceps(BICEPS_A_2)
                .chest(CHEST_A_2)
                .waist(WAIST_A_2)
                .neck(NECK_A_2)
                .hip(HIP_A_2)
                .thigh(THIGH_A_2)
                .bmi(BMI_A_2)
                .lbm(LBM_A_2)
                .bf(BF_A_2)
                .build();
    }

    public static BodyParamsEntity createTestBodyParamsEntityA3() {
        return BodyParamsEntity.builder()
                .user(createTestUserEntityA())
                .date(DATE_A_3)
                .height(HEIGHT_A_3)
                .weight(WEIGHT_A_3)
                .biceps(BICEPS_A_3)
                .chest(CHEST_A_3)
                .waist(WAIST_A_3)
                .neck(NECK_A_3)
                .hip(HIP_A_3)
                .thigh(THIGH_A_3)
                .bmi(BMI_A_3)
                .lbm(LBM_A_3)
                .bf(BF_A_3)
                .build();
    }

    public static BodyParamsDto createTestBodyParamsDtoA1() {
        return BodyParamsDto.builder()
                .date(DATE_A_1)
                .height(HEIGHT_A_1)
                .weight(WEIGHT_A_1)
                .biceps(BICEPS_A_1)
                .chest(CHEST_A_1)
                .waist(WAIST_A_1)
                .neck(NECK_A_1)
                .hip(HIP_A_1)
                .thigh(THIGH_A_1)
                .bmi(BMI_A_1)
                .lbm(LBM_A_1)
                .bf(BF_A_1)
                .build();
    }

    public static ExercisesEntity createTestExerciseEntity1() {
        return ExercisesEntity.builder()
                .id(EXERCISE_ID_1)
                .name(EXERCISE_NAME_1)
                .description(EXERCISE_DESC_1)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_1)
                .difficultyLevel(EXERCISE_DIFFICULTY_1)
                .videoUrl(EXERCISE_VIDEO_URL_1)
                .rate(EXERCISE_RATE_1)
                .userCounter(EXERCISE_USER_COUNTER_1)
                .build();
    }

    public static ExercisesEntity createTestUserExerciseEntity1() {
        return ExercisesEntity.builder()
                .id(USER_EXERCISE_ID_1)
                .name(EXERCISE_NAME_1)
                .description(EXERCISE_DESC_1)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_1)
                .difficultyLevel(EXERCISE_DIFFICULTY_1)
                .videoUrl(EXERCISE_VIDEO_URL_1)
                .rate(EXERCISE_RATE_1)
                .userCounter(EXERCISE_USER_COUNTER_1)
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static ExercisesDto createTestExerciseDto1() {
        return ExercisesDto.builder()
                .name(EXERCISE_NAME_1)
                .description(EXERCISE_DESC_1)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_1)
                .difficultyLevel(EXERCISE_DIFFICULTY_1)
                .videoUrl(EXERCISE_VIDEO_URL_1)
                .rate(EXERCISE_RATE_1)
                .userCounter(EXERCISE_USER_COUNTER_1)
                .build();
    }

    public static ExercisesDto createTestExerciseDto2() {
        return ExercisesDto.builder()
                .name(EXERCISE_NAME_2)
                .description(EXERCISE_DESC_2)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_2)
                .difficultyLevel(EXERCISE_DIFFICULTY_2)
                .videoUrl(EXERCISE_VIDEO_URL_2)
                .rate(EXERCISE_RATE_2)
                .userCounter(EXERCISE_USER_COUNTER_2)
                .build();
    }

    public static ExercisesEntity createTestExerciseEntity2() {
        return ExercisesEntity.builder()
                .id(EXERCISE_ID_2)
                .name(EXERCISE_NAME_2)
                .description(EXERCISE_DESC_2)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_2)
                .difficultyLevel(EXERCISE_DIFFICULTY_2)
                .videoUrl(EXERCISE_VIDEO_URL_2)
                .rate(EXERCISE_RATE_2)
                .userCounter(EXERCISE_USER_COUNTER_2)
                .build();
    }

    public static ExercisesEntity createTestUserExerciseEntity2() {
        return ExercisesEntity.builder()
                .id(USER_EXERCISE_ID_2)
                .name(EXERCISE_NAME_2)
                .description(EXERCISE_DESC_2)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_2)
                .difficultyLevel(EXERCISE_DIFFICULTY_2)
                .videoUrl(EXERCISE_VIDEO_URL_2)
                .rate(EXERCISE_RATE_2)
                .userCounter(EXERCISE_USER_COUNTER_2)
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static ExercisesEntity createTestExerciseEntity3() {
        return ExercisesEntity.builder()
                .id(EXERCISE_ID_3)
                .name(EXERCISE_NAME_3)
                .description(EXERCISE_DESC_3)
                .muscleGroup(EXERCISE_MUSCLE_GROUP_3)
                .difficultyLevel(EXERCISE_DIFFICULTY_3)
                .videoUrl(EXERCISE_VIDEO_URL_3)
                .rate(EXERCISE_RATE_3)
                .userCounter(EXERCISE_USER_COUNTER_3)
                .build();
    }

    public static TrainingsEntity createUserTrainingEntity1() {
        return TrainingsEntity.builder()
                .name(SYSTEM_TRAINING_NAME_1)
                .createdBy(createTestUserEntityA())
                .build();
    }

    public static SeriesDto createSeriesDto1() {
        return SeriesDto.builder()
                .id(SERIES_ID_1)
                .repetitions(SERIES_REPS_1)
                .weight1(SERIES_WEIGHT_1)
                .build();
    }

}
