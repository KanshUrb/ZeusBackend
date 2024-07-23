package com.kansh.zeus.domain.dto.trainings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingInputWrapperDto {

    private String name;

    private String note;

    private List<TrainingItemInputDto> trainingItems;

    private List<String> sharedWith;

}
