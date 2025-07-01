package io.cavia.trader.module.notice.page;

import java.util.*;

public class PagingUtil {
    public static Map<String, Object> makePaging(String pageName, int totalCnt, int listSize, int pageSize, int cp) {
        Map<String, Object> result = new HashMap<>();
        // 총 페이지 수
        int totalPage = (int) Math.ceil((double) totalCnt / listSize);
        cp = Math.max(1, Math.min(cp, totalPage));
        int userGroup = (cp-1) / pageSize;
        //시작 페이지
        int startPage = userGroup * pageSize + 1;
        //마지막 페이지
        int endPage = Math.min(startPage + pageSize-1, totalPage);
        List<Integer> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }

        // 이전/다음 페이지 그룹 이동 값
        int prevPage = Math.max(1, startPage - pageSize);
        int nextPage = Math.min(totalPage, startPage + pageSize);

        // 이전/다음 그룹 존재 여부
        boolean hasPrev = startPage > 1;
        boolean hasNext = endPage < totalPage;

        result.put("pageName", pageName);
        result.put("pageList", pageList);
        result.put("hasPrev", hasPrev);
        result.put("hasNext", hasNext);
        result.put("prevPage", prevPage);
        result.put("nextPage", nextPage);
        result.put("cp", cp);
        result.put("totalPage", totalPage);
        result.put("firstPage", 1);
        return result;
    }
}
