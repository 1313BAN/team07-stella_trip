package com.ssafy.stella_trip.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> {
    private List<T> content;
    private boolean hasNext;
    private int totalPages;
    private int totalElements;
    private int page;
    private int size;
    private boolean isFirst;
    private boolean isLast;
}
