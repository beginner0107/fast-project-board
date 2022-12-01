package com.fast.projectboard.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2), 0);
        // 0,1 은 음수 나오는 걸 해결하기 위해 Math.max
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);
        // 마지막 페이지 설정할 때는 totalPages 와 비교해서 작은 값을 써야 한다.

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
