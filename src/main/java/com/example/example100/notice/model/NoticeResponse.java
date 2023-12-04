package com.example.example100.notice.model;

import com.example.example100.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private int hits;
    private int likes;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    private Long regUserId;
    private String regUserName;

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .hits(notice.getHits())
                .likes(notice.getLikes())
                .regDate(notice.getRegDate())
                .updateDate(notice.getUpdateDate())
                .regUserId(notice.getUser().getId())
                .regUserName(notice.getUser().getUserName())
                .build();
    }
}
