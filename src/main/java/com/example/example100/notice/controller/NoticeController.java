package com.example.example100.notice.controller;

import com.example.example100.notice.model.NoticeDto;
import java.time.LocalDateTime;
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

    /**
     * 7. 공지사항 게시판의 내용을 추상화한 모델(게시글ID, 제목, 내용, 등록일)이며
     * 데이터는 (게시글 ID = 1, 제목 = 공지사항입니다, 내용 = 공지사항 내용입니다, 등록일 = 2021-01-31) 리턴
     */
    @GetMapping("/api/notice2")
    public NoticeDto getNotice2() {
        return NoticeDto.builder()
                .id(1L)
                .title("공지사항입니다.")
                .content("공지사항 내용입니다.")
                .regDate(LocalDateTime.of(2021, 1, 31, 17, 17))
                .build();
    }
}
