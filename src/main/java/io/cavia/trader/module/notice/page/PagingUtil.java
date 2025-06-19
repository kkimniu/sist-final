package io.cavia.trader.module.notice.page;

import java.util.*;

public class PagingUtil {
    public static Map<String, Object> makePaging(String pageName, int totalCnt, int listSize, int pageSize, int cp) {
        Map<String, Object> result = new HashMap<>();
        // 총 페이지 수
        int totalPage = (totalCnt / listSize) + 1;
        if (totalCnt % listSize == 0) totalPage--;
        int userGroup = cp / pageSize;
        if (cp % pageSize == 0) userGroup--;
        //시작 페이지
        int stratPage = userGroup * pageSize + 1;
        //마지막 페이지
        int endPage = Math.min(stratPage + pageSize, totalPage);
        List<Integer> pageList = new ArrayList<>();
        for (int i = stratPage; i <= endPage; i++) {
            pageList.add(i);
        }
        //현재페이지
        int prevPage = (userGroup - 1) * pageSize + pageSize;
        //다음페이지
        int nextPage = (userGroup + 1) * pageSize + 1;
        result.put("pageName", pageName);
        result.put("pageList", pageList);
        result.put("hasPrev", userGroup != 0);
        result.put("hasNext", ((totalPage / pageSize) - (totalPage % pageSize == 0 ? 1 : 0)) != userGroup);
        result.put("prevPage", prevPage);
        result.put("nextPage", nextPage);
        result.put("cp", cp);
        result.put("totalPage", totalPage);
        return result;
    }
}
