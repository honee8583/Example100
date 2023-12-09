package com.example.example100.notice.service.impl;

import com.example.example100.notice.entity.BoardType;
import com.example.example100.notice.model.BoardTypeInput;
import com.example.example100.notice.model.BoardTypeUpdateInput;
import com.example.example100.notice.model.ServiceResult;
import com.example.example100.notice.repository.BoardTypeRepository;
import com.example.example100.notice.service.BoardService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardTypeRepository boardTypeRepository;

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
}
