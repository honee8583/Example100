package com.example.example100.board.controller;

import com.example.example100.board.entity.BoardBadReport;
import com.example.example100.board.model.BoardReplyInput;
import com.example.example100.board.service.BoardService;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.common.model.ServiceResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Board API", description = "관리자용 게시판 API 입니다.")
@RestController
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;

    /**
     * 74. 게시글의 신고 목록을 조회하는 api를 작성하시오.
     */
    @Operation(summary = "문제74", description = "Q. 게시글의 신고 목록을 조회하는 api를 작성하시오.")
    @GetMapping("/api/admin/board/badreport")
    public ResponseEntity<?> badReportList() {
        List<BoardBadReport> boardBadReports = boardService.getBadReportList();
        return ResponseResult.success(boardBadReports);
    }

    /**
     * 98. 문의 게시판에 답변을 달경우 메일을 전송하는 API를 작성하시오.
     */
    @Operation(summary = "문제98", description = "Q. 문의 게시판에 답변을 달경우 메일을 전송하는 API를 작성하시오.")
    @PostMapping("/api/admin/board/{id}/reply")
    public ResponseEntity<?> reply(@PathVariable Long id, @RequestBody BoardReplyInput boardReplyInput,
                                   @RequestHeader("Authorization") String token) {
        ServiceResult result = boardService.replyBoard(id, boardReplyInput);
        return ResponseResult.result(result);
    }
}
