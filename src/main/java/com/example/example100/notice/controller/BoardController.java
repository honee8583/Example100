package com.example.example100.notice.controller;

import com.example.example100.error.ErrorResponse;
import com.example.example100.notice.model.BoardTypeInput;
import com.example.example100.notice.model.BoardTypeUpdateInput;
import com.example.example100.notice.model.ServiceResult;
import com.example.example100.notice.service.BoardService;
import com.example.example100.user.model.ResponseMessage;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}