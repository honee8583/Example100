package com.example.example100.notice.service.impl;

import com.example.example100.notice.entity.BoardType;
import com.example.example100.notice.model.BoardCountResponse;
import com.example.example100.notice.model.BoardTypeEnabledInput;
import com.example.example100.notice.model.BoardTypeInput;
import com.example.example100.notice.model.BoardTypeUpdateInput;
import com.example.example100.notice.model.ServiceResult;
import com.example.example100.notice.repository.BoardRepository;
import com.example.example100.notice.repository.BoardTypeCustomRepository;
import com.example.example100.notice.repository.BoardTypeRepository;
import com.example.example100.notice.service.BoardService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardTypeRepository boardTypeRepository;
    private final BoardTypeCustomRepository boardTypeCustomRepository;
    private final BoardRepository boardRepository;

    @Override
    public ServiceResult addBoard(BoardTypeInput boardTypeInput) {
        BoardType boardType = boardTypeRepository.findByBoardName(boardTypeInput.getName());

        // 동일한 게시판 제목이 있는 경우
        if (boardType != null && boardTypeInput.getName().equals(boardType.getBoardName())) {
            return ServiceResult.fail("이미 동일한 게시판이 존재합니다.");
        }

        BoardType addBoardType = BoardType.builder()
                .boardName(boardTypeInput.getName())
                .regDate(LocalDateTime.now())
                .build();
        boardTypeRepository.save(addBoardType);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult updateBoard(BoardTypeUpdateInput boardTypeUpdateInput) {
        Optional<BoardType> boardType = boardTypeRepository.findById(boardTypeUpdateInput.getId());
        if (!boardType.isPresent()) {
            return ServiceResult.fail("수정할 게시판이 존재하지 않습니다.");
        }

        BoardType savedBoardType = boardType.get();
        if (savedBoardType.getBoardName().equals(boardTypeUpdateInput.getName())) {
            return ServiceResult.fail("수정할 이름이 동일한 게시판입니다.");
        }

        savedBoardType.setBoardName(boardTypeUpdateInput.getName());
        savedBoardType.setUpdateDate(LocalDateTime.now());
        boardTypeRepository.save(savedBoardType);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult deleteBoard(Long id) {
        Optional<BoardType> boardType = boardTypeRepository.findById(id);
        if (!boardType.isPresent()) {
            return ServiceResult.fail("삭제할 게시판타입이 존재하지 않습니다.");
        }

        BoardType savedBoardType = boardType.get();

        if (boardRepository.countByBoardType(savedBoardType) > 0) {
            return ServiceResult.fail("삭제할 게시판타입에 해당하는 게시글이 존재합니다.");
        }

        boardTypeRepository.delete(savedBoardType);

        return ServiceResult.success();
    }

    @Override
    public List<BoardType> getBoardTypeList() {
        return boardTypeRepository.findAll();
    }

    @Override
    public ServiceResult updateBoardTypeEnabled(Long id, BoardTypeEnabledInput boardTypeEnabledInput) {
        Optional<BoardType> boardType = boardTypeRepository.findById(id);
        if (!boardType.isPresent()) {
            return ServiceResult.fail("사용여부를 수정할 게시판 타입이 존재하지 않습니다.");
        }

        BoardType savedBoardType = boardType.get();
        savedBoardType.setUsingYn(boardTypeEnabledInput.isUsingYn());
        boardTypeRepository.save(savedBoardType);

        return ServiceResult.success();
    }

    @Override
    public List<BoardCountResponse> getBoardCountByBoardType() {
        return boardTypeCustomRepository.getBoardCountByBoardType();
    }
}
