package com.ssafy.stella_trip.common.util;

import com.ssafy.stella_trip.common.dto.PageDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class PaginationUtils {

    /**
     * 페이지네이션 처리를 위한 제네릭 메서드
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param totalElementsSupplier 전체 요소 수를 가져오는 함수
     * @param elementsSupplier 페이지 요소를 가져오는 함수
     * @param dtoConverter DTO를 응답 DTO로 변환하는 함수
     * @return 페이징된 응답 DTO
     */
    public static <T, R> PageDTO<R> getPagedResult(
            int page,
            int size,
            Supplier<Integer> totalElementsSupplier,
            BiFunction<Integer, Integer, List<T>> elementsSupplier,
            Function<T, R> dtoConverter) {

        int totalElements = totalElementsSupplier.get();
        PageInfo pageInfo = calculatePageInfo(totalElements, page, size);

        List<T> elements = elementsSupplier.apply(pageInfo.offset, size);
        List<R> responseElements = elements.stream()
                .map(dtoConverter)
                .toList();

        return PageDTO.<R>builder()
                .content(responseElements)
                .totalElements(totalElements)
                .totalPages(pageInfo.totalPages)
                .page(page)
                .size(size)
                .isFirst(pageInfo.isFirst)
                .isLast(pageInfo.isLast)
                .hasNext(pageInfo.hasNext)
                .build();
    }

    /**
     * 페이징 정보를 계산하는 메서드
     */
    public static PageInfo calculatePageInfo(int totalElements, int page, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }

        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        int offset = (page - 1) * size;
        boolean hasNext = page < totalPages;
        boolean isFirst = page == 1;
        boolean isLast = totalPages == 0 || page >= totalPages;

        return new PageInfo(totalPages, offset, hasNext, isFirst, isLast);
    }

    /**
     * 페이징 정보를 담는 클래스
     */
    @Getter
    @RequiredArgsConstructor
    public static class PageInfo {
        private final int totalPages;
        private final int offset;
        private final boolean hasNext;
        private final boolean isFirst;
        private final boolean isLast;
    }
}