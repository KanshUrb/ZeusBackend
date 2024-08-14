package com.kansh.zeus;

import com.kansh.zeus.domain.dto.body_params.BodyParamsDto;
import com.kansh.zeus.domain.dto.users.UserDto;
import com.kansh.zeus.domain.dto.users.UserInputDto;
import com.kansh.zeus.domain.dto.users.UserTokenDto;
import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.entities.body_params.BodyParamsEntity;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.users.UsersEntity;

import java.time.LocalDate;

public class TestDataUtil {

    private TestDataUtil() {
    }

    static final String ID_A = "U83rEkVPo4XJj87dm2NnSfXlS123";
    static final String FIRST_NAME_A = "Artur";
    static final String LAST_NAME_A = "Nowak";
    static final String EMAIL_A = "test@test.com";
    static final Integer GENDER_A = 1;
    static final String HASH_A = "12345678";
    static final String PHOTO_A = "https://example.org/photo.jpg";

    static final LocalDate DATE_A_1 = LocalDate.parse("2023-01-01");
    static final Integer HEIGHT_A_1 = 180;
    static final Float WEIGHT_A_1 = 75.0f;
    static final Float BICEPS_A_1 = 32.0f;
    static final Float CHEST_A_1 = 102.0f;
    static final Float WAIST_A_1 = 80.0f;
    static final Float NECK_A_1 = 40.0f;
    static final Float HIP_A_1 = 95.0f;
    static final Float THIGH_A_1 = 55.0f;
    static final Float BMI_A_1 = 23.15f;
    static final Float BF_A_1 = 16.54f;
    static final Float LBM_A_1 = 62.57f;

    static final LocalDate DATE_A_2 = LocalDate.parse("2023-06-01");
    static final Integer HEIGHT_A_2 = 180;
    static final Float WEIGHT_A_2 = 78.0f;
    static final Float BICEPS_A_2 = 34.0f;
    static final Float CHEST_A_2 = 105.0f;
    static final Float WAIST_A_2 = 82.0f;
    static final Float NECK_A_2 = 41.0f;
    static final Float HIP_A_2 = 97.0f;
    static final Float THIGH_A_2 = 57.0f;
    static final Float BMI_A_2 = 24.07f;
    static final Float BF_A_2 = 17.23f;
    static final Float LBM_A_2 = 64.58f;

    static final LocalDate DATE_A_3 = LocalDate.parse("2023-12-01");
    static final Integer HEIGHT_A_3 = 180;
    static final Float WEIGHT_A_3 = 80.0f;
    static final Float BICEPS_A_3 = 36.0f;
    static final Float CHEST_A_3 = 108.0f;
    static final Float WAIST_A_3 = 84.0f;
    static final Float NECK_A_3 = 42.0f;
    static final Float HIP_A_3 = 99.0f;
    static final Float THIGH_A_3 = 59.0f;
    static final Float BMI_A_3 = 24.69f;
    static final Float BF_A_3 = 17.87f;
    static final Float LBM_A_3 = 65.74f;

    static final String ID_B = "U83rEkVPo4XJj87dm2NnSfXlS124";
    static final String FIRST_NAME_B = "Jenny";
    static final String LAST_NAME_B = "Rose";
    static final String EMAIL_B = "test1@test.com";
    static final Integer GENDER_B = 2;
    static final String HASH_B = "87654321";
    static final String PHOTO_B = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Pat_Mat.jpg/800px-Pat_Mat.jpg";

    static final LocalDate DATE_B_1 = LocalDate.parse("2023-01-01");
    static final Integer HEIGHT_B_1 = 165;
    static final Float WEIGHT_B_1 = 70.0f;
    static final Float BICEPS_B_1 = 28.0f;
    static final Float CHEST_B_1 = 90.0f;
    static final Float WAIST_B_1 = 75.0f;
    static final Float NECK_B_1 = 35.0f;
    static final Float HIP_B_1 = 95.0f;
    static final Float THIGH_B_1 = 50.0f;
    static final Float BMI_B_1 = 25.71f;
    static final Float BF_B_1 = 52.68f;
    static final Float LBM_B_1 = 33.25f;

    static final LocalDate DATE_B_2 = LocalDate.parse("2023-06-01");
    static final Integer HEIGHT_B_2 = 165;
    static final Float WEIGHT_B_2 = 65.0f;
    static final Float BICEPS_B_2 = 27.0f;
    static final Float CHEST_B_2 = 88.0f;
    static final Float WAIST_B_2 = 72.0f;
    static final Float NECK_B_2 = 34.0f;
    static final Float HIP_B_2 = 93.0f;
    static final Float THIGH_B_2 = 48.0f;
    static final Float BMI_B_2 = 23.88f;
    static final Float BF_B_2 = 50.48f;
    static final Float LBM_B_2 = 32.19f;

    static final LocalDate DATE_B_3 = LocalDate.parse("2023-12-01");
    static final Integer HEIGHT_B_3 = 165;
    static final Float WEIGHT_B_3 = 60.0f;
    static final Float BICEPS_B_3 = 26.0f;
    static final Float CHEST_B_3 = 85.0f;
    static final Float WAIST_B_3 = 68.0f;
    static final Float NECK_B_3 = 33.0f;
    static final Float HIP_B_3 = 90.0f;
    static final Float THIGH_B_3 = 46.0f;
    static final Float BMI_B_3 = 22.04f;
    static final Float BF_B_3 = 47.24f;
    static final Float LBM_B_3 = 31.66f;

    static final Long EXERCISE_ID_1 = 1L;
    static final String EXERCISE_NAME_1 = "Push-up";
    static final String EXERCISE_DESC_1 = "Push-up description";
    static final String EXERCISE_MUSCLE_GROUP_1 = "Chest";
    static final Integer EXERCISE_DIFFICULTY_1 = 3;
    static final String EXERCISE_VIDEO_URL_1 = "http://video.url";
    static final Float EXERCISE_RATE_1 = 4.5F;
    static final Integer EXERCISE_USER_COUNTER_1 = 10;
    static final Long EXERCISE_ID_2 = 2L;
    static final String EXERCISE_NAME_2 = "Squat";
    static final String EXERCISE_DESC_2 = "Squat description";
    static final String EXERCISE_MUSCLE_GROUP_2 = "Legs";
    static final Integer EXERCISE_DIFFICULTY_2 = 4;
    static final String EXERCISE_VIDEO_URL_2 = "http://video.url/squat";
    static final Float EXERCISE_RATE_2 = 4.0F;
    static final Integer EXERCISE_USER_COUNTER_2 = 20;
    static final Long EXERCISE_ID_3 = 3L;
    static final String EXERCISE_NAME_3 = "Pull-up";
    static final String EXERCISE_DESC_3 = "Pull-up description";
    static final String EXERCISE_MUSCLE_GROUP_3 = "Back";
    static final Integer EXERCISE_DIFFICULTY_3 = 5;
    static final String EXERCISE_VIDEO_URL_3 = "http://video.url/pullup";
    static final Float EXERCISE_RATE_3 = 4.8F;
    static final Integer EXERCISE_USER_COUNTER_3 = 15;

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
                .email(EMAIL_A)
                .firstName(FIRST_NAME_A)
                .lastName(LAST_NAME_A)
                .photo(PHOTO_A)
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

    public static UserDto createTestUserDtoB() {
        return UserDto.builder()
                .id(ID_B)
                .firstName(FIRST_NAME_B)
                .lastName(LAST_NAME_B)
                .hash(HASH_B)
                .photo(PHOTO_B)
                .build();
    }

    public static UserTokenDto createTestUserTokenB() {
        return UserTokenDto.builder()
                .id(ID_B)
                .email(EMAIL_B)
                .firstName(FIRST_NAME_B)
                .lastName(LAST_NAME_B)
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

    public static BodyParamsDto createTestBodyParamsDtoA2() {
        return BodyParamsDto.builder()
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

    public static BodyParamsDto createTestBodyParamsDtoA3() {
        return BodyParamsDto.builder()
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

    public static BodyParamsEntity createTestBodyParamsEntityB1() {
        return BodyParamsEntity.builder()
                .date(DATE_B_1)
                .height(HEIGHT_B_1)
                .weight(WEIGHT_B_1)
                .biceps(BICEPS_B_1)
                .chest(CHEST_B_1)
                .waist(WAIST_B_1)
                .neck(NECK_B_1)
                .hip(HIP_B_1)
                .thigh(THIGH_B_1)
                .bmi(BMI_B_1)
                .lbm(LBM_B_1)
                .bf(BF_B_1)
                .build();
    }

    public static BodyParamsEntity createTestBodyParamsEntityB2() {
        return BodyParamsEntity.builder()
                .user(createTestUserEntityB())
                .date(DATE_B_2)
                .height(HEIGHT_B_2)
                .weight(WEIGHT_B_2)
                .biceps(BICEPS_B_2)
                .chest(CHEST_B_2)
                .waist(WAIST_B_2)
                .neck(NECK_B_2)
                .hip(HIP_B_2)
                .thigh(THIGH_B_2)
                .bmi(BMI_B_2)
                .lbm(LBM_B_2)
                .bf(BF_B_2)
                .build();
    }

    public static BodyParamsEntity createTestBodyParamsEntityB3() {
        return BodyParamsEntity.builder()
                .user(createTestUserEntityB())
                .date(DATE_B_3)
                .height(HEIGHT_B_3)
                .weight(WEIGHT_B_3)
                .biceps(BICEPS_B_3)
                .chest(CHEST_B_3)
                .waist(WAIST_B_3)
                .neck(NECK_B_3)
                .hip(HIP_B_3)
                .thigh(THIGH_B_3)
                .bmi(BMI_B_3)
                .lbm(LBM_B_3)
                .bf(BF_B_3)
                .build();
    }

    public static BodyParamsDto createTestBodyParamsDtoB1() {
        return BodyParamsDto.builder()
                .date(DATE_B_1)
                .height(HEIGHT_B_1)
                .weight(WEIGHT_B_1)
                .biceps(BICEPS_B_1)
                .chest(CHEST_B_1)
                .waist(WAIST_B_1)
                .neck(NECK_B_1)
                .hip(HIP_B_1)
                .thigh(THIGH_B_1)
                .bmi(BMI_B_1)
                .lbm(LBM_B_1)
                .bf(BF_B_1)
                .build();
    }

    public static BodyParamsDto createTestBodyParamsDtoB2() {
        return BodyParamsDto.builder()
                .date(DATE_B_2)
                .height(HEIGHT_B_2)
                .weight(WEIGHT_B_2)
                .biceps(BICEPS_B_2)
                .chest(CHEST_B_2)
                .waist(WAIST_B_2)
                .neck(NECK_B_2)
                .hip(HIP_B_2)
                .thigh(THIGH_B_2)
                .bmi(BMI_B_2)
                .lbm(LBM_B_2)
                .bf(BF_B_2)
                .build();
    }

    public static BodyParamsDto createTestBodyParamsDtoB3() {
        return BodyParamsDto.builder()
                .date(DATE_B_3)
                .height(HEIGHT_B_3)
                .weight(WEIGHT_B_3)
                .biceps(BICEPS_B_3)
                .chest(CHEST_B_3)
                .waist(WAIST_B_3)
                .neck(NECK_B_3)
                .hip(HIP_B_3)
                .thigh(THIGH_B_3)
                .bmi(BMI_B_3)
                .lbm(LBM_B_3)
                .bf(BF_B_3)
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
}
