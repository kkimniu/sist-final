package io.cavia.trader.module.notice.controller;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.page.PagingUtil;
import io.cavia.trader.module.notice.service.NoticeServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("notices")
@RequiredArgsConstructor
public class ViewNoticeController {

    private final NoticeServiceImple noticeServiceImple;

    @GetMapping("/noticeList")
    public String noticeList(@RequestParam(value = "cp", defaultValue = "1") int cp, Model model) {
        int listSize = 30;
        int pageSize = 5;
        int countNotice = noticeServiceImple.noticeCount();
        List<NoticeDto> list = noticeServiceImple.noticeList(cp, listSize);
        Map<String, Object> paging = PagingUtil.makePaging("notices/noticeList", countNotice, listSize, pageSize, cp);

        model.addAttribute("lists", list);
        model.addAttribute("paging", paging);
        return "notices/noticeList";
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(@RequestParam(value = "id") int id,@RequestParam(value = "cp" , defaultValue = "1") int cp, Model model) {
        NoticeDto dto = noticeServiceImple.findById(id);
        model.addAttribute("dto", dto);
        model.addAttribute("cp" , cp);
        return "notices/noticeDetail";
    }
}
