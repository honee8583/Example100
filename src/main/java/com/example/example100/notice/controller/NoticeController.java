package com.example.example100.notice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {

    /**
     * 6. "공지사항입니다." 문구를 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/notice")
    public String getNotice() {
        return "공지사항입니다.";
    }
}
