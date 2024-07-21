package com.kansh.zeus.domain.dto.exercises;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<T> {

    private List<T> result;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;
}
