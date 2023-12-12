package com.example.example100.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.example100.board.service.BoardService;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardScrapController {
    private final BoardService boardService;

    /**
     * 75. 게시글의 스크랩을 추가하는 api를 작성하시오.
     */
    @PutMapping("/api/board/{id}/scrap")
    public ResponseEntity<?> boardScrap(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 유효하지 않습니다.");
        }

        return ResponseResult.result(boardService.scrapBoard(id, email));
    }
}
