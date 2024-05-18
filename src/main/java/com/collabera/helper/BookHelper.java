package com.collabera.helper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BookHelper {

    public void pagination(int page, Page<?> lists, Map<Object, Object> responseMap) {
        int totalPages = lists.getTotalPages();
        responseMap.put("lists", lists);
        responseMap.put("totalPages", totalPages);
        responseMap.put("currentPage", page);
    }
}
