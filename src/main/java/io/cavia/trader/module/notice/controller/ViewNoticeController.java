package io.cavia.trader.module.notice.controller;

import io.cavia.trader.module.notice.dto.NoticeDto;
import io.cavia.trader.module.notice.page.PagingUtil;
import io.cavia.trader.module.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class ViewNoticeController {

    private final NoticeService noticeService;

    @GetMapping("")
    public String noticeList(@RequestParam(defaultValue = "1") int cp,
                             @RequestParam(defaultValue = "20") int listSize,
                             Model model) {
        int pageSize = 5;
        int countNotice = noticeService.noticeCount();
        List<NoticeDto> list = noticeService.noticeList(cp, listSize);
        Map<String, Object> paging = PagingUtil
                .makePaging("notices/main", countNotice, listSize, pageSize, cp);

        model.addAttribute("lists", list);
        model.addAttribute("paging", paging);
        return "notices/main";
    }

    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable int id,
                               @RequestParam(defaultValue = "1") int cp,
                               Model model) {
        NoticeDto dto = noticeService.findById(id);
        String content = dto.getContent();
        if(content==null){
            dto.setContent("");
        }else{
            dto.setContent(content.replace("\n","<br/>"));
        }
        model.addAttribute("dto", dto);
        model.addAttribute("cp", cp);
        return "notices/details";
    }
}
