package com.example.example100.board.controller;

import com.example.example100.board.entity.BoardBadReport;
import com.example.example100.board.service.BoardService;
import com.example.example100.common.model.ResponseResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;

    /**
     * 74. 게시글의 신고 목록을 조회하는 api를 작성하시오.
     */
    @GetMapping("/api/admin/board/badreport")
    public ResponseEntity<?> badReportList() {
        List<BoardBadReport> boardBadReports = boardService.getBadReportList();
        return ResponseResult.success(boardBadReports);
    }
}
