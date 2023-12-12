package com.example.example100.board.entity;

import com.example.example100.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoardScrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    // 스크랩 내용
    private Long boardId;
    private Long boardTypeId;
    private Long boardUserId;
    private String boardTitle;
    private String boardContents;
    private LocalDateTime boardRegDate;

    private LocalDateTime regDate;
}
