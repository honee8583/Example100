package com.example.example100.board.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.example100.board.entity.Board;
import com.example.example100.board.model.BoardInput;
import com.example.example100.common.exception.BizException;
import com.example.example100.common.model.ResponseResult;
import com.example.example100.error.ErrorResponse;
import com.example.example100.board.entity.BoardType;
import com.example.example100.board.model.BoardBadReportInput;
import com.example.example100.board.model.BoardPeriod;
import com.example.example100.board.model.BoardCountResponse;
import com.example.example100.board.model.BoardTypeEnabledInput;
import com.example.example100.board.model.BoardTypeInput;
import com.example.example100.board.model.BoardTypeUpdateInput;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.board.service.BoardService;
import com.example.example100.user.model.ResponseMessage;
import com.example.example100.util.JWTUtils;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 61. 게시판 타입을 추가하는 api를 작성하시오.
     * 동일한 게시판 제목이 있는 경우 status=200, result=false, message에 이미 존재하는 게시판이 있다고 리턴.
     * 게시판 이름은 필수항목에 대한 부분 체크
     */
    @PostMapping("/api/board/type")
    public ResponseEntity<?> addBoardType(@RequestBody @Valid BoardTypeInput boardTypeInput, Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = ErrorResponse.of(errors.getAllErrors());
            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", errorResponses), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = boardService.addBoard(boardTypeInput);

        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 62. 게시판 타입을 수정하는 api를 작성하시오.
     */
    @PutMapping("/api/board/type/{id}")
    public ResponseEntity<?> updateBoardType(@PathVariable Long id,
                                             @RequestBody @Valid BoardTypeUpdateInput boardTypeUpdateInput,
                                             Errors errors) {
        if (errors.hasErrors()) {
            List<ErrorResponse> errorResponses = ErrorResponse.of(errors.getAllErrors());
            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", errorResponses), HttpStatus.BAD_REQUEST);
        }

        boardTypeUpdateInput.setId(id);
        ServiceResult result = boardService.updateBoard(boardTypeUpdateInput);

        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 63. 게시판 타입을 삭제하는 api를 작성하시오.
     * 삭제할 게시판 타입의 게시판이 존재하면 삭제 진행 불가 처리.
     */
    @DeleteMapping("/api/board/type/{id}")
    public ResponseEntity<?> deleteBoardType(@PathVariable Long id) {
        ServiceResult result = boardService.deleteBoard(id);
        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 64. 게시판 타입의 목록을 리턴하는 api를 작성하시오.
     */
    @GetMapping("/api/board/type/list")
    public ResponseEntity<?> getBoardTypeList() {
        List<BoardType> boardTypes = boardService.getBoardTypeList();

        return ResponseEntity.ok().body(ResponseMessage.success(boardTypes));
    }

    /**
     * 65. 게시판 타입의 사용여부를 설정하는 api를 작성하시오.
     */
    @PatchMapping("/api/board/type/{id}/using")
    public ResponseEntity<?> enableBoardType(@PathVariable Long id,
                                             @RequestBody BoardTypeEnabledInput boardTypeEnabledInput) {
        ServiceResult result = boardService.updateBoardTypeEnabled(id, boardTypeEnabledInput);
        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 66. 게시판별 작성된 게시글의 개수를 리턴하는 api를 작성하시오.
     * 현재 사용가능한 게시판에 대해서 게시글의 개수를 리턴.
     */
    @GetMapping("/api/board/type/count")
    public ResponseEntity<?> getBoardCountByBoardType() {
        List<BoardCountResponse> boardCountResponses =
                boardService.getBoardCountByBoardType();

        return ResponseEntity.ok().body(boardCountResponses);
    }

    /**
     * 67. 게시글을 최상단에 배치하는 api를 작성하시오.
     */
    @PatchMapping("/api/board/{id}/top")
    public ResponseEntity<?> setBoardTop(@PathVariable Long id) {
        ServiceResult result = boardService.setBoardTop(id, true);
        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 68. 최상단에 위차한 게시글을 최상단 배치를 해제하는 api를 작성하시오.
     */
    @PatchMapping("/api/board/{id}/top/clear")
    public ResponseEntity<?> setBoardTopClear(@PathVariable Long id) {
        ServiceResult result = boardService.setBoardTop(id, false);
        if (!result.isResult()) {
            return new ResponseEntity<>(ResponseMessage.fail(result.getMessage()), HttpStatus.OK);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 69. 게시글의 게시기간을 시작일과 종요일로 설정하는 api를 작성하시오.
     */
    @PatchMapping("/api/board/{id}/publish")
    public ResponseEntity<?> boardPeriod(@PathVariable Long id, @RequestBody BoardPeriod boardPeriod) {
        ServiceResult result = boardService.setBoardPeriod(id, boardPeriod);
        if (!result.isResult()) {
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseResult.success();
    }

    /**
     * 70. 게시글의 조회수를 증가시키는 api를 작성하시오.
     * 동일 사용자에 의한 게시글 조회수 증가를 방지하는 로직 포함.
     * JWT 인증을 통과한 사용자에 대해서 진행.
     */
    @PutMapping("/api/board/{id}/hits")
    public ResponseEntity<?> boardHits(@PathVariable Long id,
                                       @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.increaseBoardHits(id, email);
        if (!result.isResult()) {
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseResult.success();
    }

    /**
     * 71. 게시글에 대한 좋아요하기 기능을 수행하는 api를 작성하시오.
     */
    @PutMapping("/api/board/{id}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long id,
                                       @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.increaseBoardLike(id, email);

        return ResponseResult.result(result);
    }

    /**
     * 72. 게시글에 좋아요를 취소하는 api를 작성하시오.
     */
    @PutMapping("/api/board/{id}/unlike")
    public ResponseEntity<?> boardUnLike(@PathVariable Long id,
                                       @RequestHeader("Authorization") String token) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.setBoardUnLike(id, email);

        return ResponseResult.result(result);
    }

    /**
     * 73. 게시판을 신고하는 기능의 api를 작성하시오.
     */
    @PutMapping("/api/board/{id}/badreport")
    public ResponseEntity<?> boardBadReport(@PathVariable Long id,
                               @RequestHeader("Authorization") String token,
                               @RequestBody BoardBadReportInput boardBadReportInput) {
        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (JWTVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = boardService.addBadReport(id, email, boardBadReportInput);

        return ResponseResult.result(result);
    }

    /**
     * 85. AOP의 Around를 이용하여 게시판 상세 조회에 대한 히스토리를 기록하는 기능을 작성하시오.
     */
    @GetMapping("/api/board/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        Board board = null;
        try {
            board = boardService.detail(id);
        } catch (BizException e) {
            return ResponseResult.fail(e.getMessage());
        }

        return ResponseResult.success(board);
    }

    /**
     * 92. 인터셉터를 이용하여 API 요청에 대한 정보를 LOG에 기록하는 기능을 작성하시오.
     */
    @GetMapping("/api/board")
    public ResponseEntity<?> list() {
        List<Board> boards = boardService.list();

        return ResponseResult.success(boards);
    }

    /**
     * 93. 인터셉터를 활용하여 JWT 인증이 필요한 API에 대해서 토큰 유효성을 검증하는 API를 작성하시오.
     */
    @PostMapping("/api/board")
    public ResponseEntity<?> addBoard(@RequestHeader("Authorization") String token,
                                      @RequestBody BoardInput boardInput) {
        String email = JWTUtils.getIssuer(token);
        ServiceResult result = boardService.add(email, boardInput);
        return ResponseResult.result(result);
    }

    /**
     * 97. 게시판 작성을 완료하면 사용자에게 작성완료 메일을 전송하는 API를 작성하시오.
     */
    @PostMapping("/api/board2")
    public ResponseEntity<?> addBoard2(@RequestHeader("Authorization") String token,
                                      @RequestBody BoardInput boardInput) {
        String email = JWTUtils.getIssuer(token);
        ServiceResult result = boardService.add(email, boardInput);
        return ResponseResult.result(result);
    }
}
