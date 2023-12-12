package com.example.example100.user.model;

import com.example.example100.board.entity.BoardComment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentResponse {
    private Long id;
    private String userName;
    private Long board_id;
    private String boardComments;
    private LocalDateTime regDate;

    public static BoardCommentResponse of(BoardComment boardComment) {
        return BoardCommentResponse.builder()
                .id(boardComment.getId())
                .userName(boardComment.getUser().getUserName())
                .board_id(boardComment.getBoard().getId())
                .boardComments(boardComment.getComments())
                .regDate(boardComment.getRegDate())
                .build();
    }
}
