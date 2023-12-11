package com.example.example100.board.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoardBadReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고자 정보
    private Long userId;
    private String userName;
    private String userEmail;

    // 신고 게시글 정보
    private Long boardId;
    private Long boardUserId;
    private String boardTitle;
    private String boardContents;
    private LocalDateTime boardRegDate;

    private String comments;
    private LocalDateTime regDate;
}
