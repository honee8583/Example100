package com.example.example100.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.example100.board.service.BoardService;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.util.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board Bookmark API", description = "게시판 북마킹 API 입니다.")
@RestController
@RequiredArgsConstructor
public class BoardBookmarkController {
    private final BoardService boardService;

    /**
     * 77. 게시글의 북마크를 추가/삭제하는 api를 작성하시오.
     */
    @Operation(summary = "문제77", description = "Q. 게시글의 북마크를 추가하는 api를 작성하시오.")
    @PutMapping("/api/board/{id}/bookmark")
    public ResponseEntity<?> boardBookmark(@PathVariable Long id,
                                           @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(boardService.addBookmark(id, email));
    }

    @Operation(summary = "문제77", description = "Q. 게시글의 북마크를 삭제하는 api를 작성하시오.")
    @DeleteMapping("/api/bookmark/{id}")
    public ResponseEntity<?> deleteBookmark(@PathVariable Long id,
                                            @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        return ResponseResult.result(boardService.removeBookmark(id, email));
    }
}
