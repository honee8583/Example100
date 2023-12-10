package com.example.example100.notice.model;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardCountResponse {
    private Long id;
    private String boardName;
    private LocalDateTime regDate;
    private boolean usingYn;
    private Long boardCount;

    public BoardCountResponse(BigInteger id, String boardName, Timestamp regDate, Boolean usingYn, BigInteger boardCount) {
        this.id = id.longValue();
        this.boardName = boardName;
        this.regDate = regDate.toLocalDateTime();
        this.usingYn = usingYn;
        this.boardCount = boardCount.longValue();
    }
}
