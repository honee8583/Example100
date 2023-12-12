package com.example.example100.board.service.impl;

import com.example.example100.board.entity.Board;
import com.example.example100.board.entity.BoardBadReport;
import com.example.example100.board.entity.BoardHits;
import com.example.example100.board.entity.BoardLike;
import com.example.example100.board.entity.BoardScrap;
import com.example.example100.board.entity.BoardType;
import com.example.example100.board.model.BoardBadReportInput;
import com.example.example100.board.model.BoardCountResponse;
import com.example.example100.board.model.BoardPeriod;
import com.example.example100.board.model.BoardTypeEnabledInput;
import com.example.example100.board.model.BoardTypeInput;
import com.example.example100.board.model.BoardTypeUpdateInput;
import com.example.example100.board.repository.BoardScrapRepository;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.board.repository.BoardBadReportRepository;
import com.example.example100.board.repository.BoardHitsRepository;
import com.example.example100.board.repository.BoardLikeRepository;
import com.example.example100.board.repository.BoardRepository;
import com.example.example100.board.repository.BoardTypeCustomRepository;
import com.example.example100.board.repository.BoardTypeRepository;
import com.example.example100.board.service.BoardService;
import com.example.example100.user.entity.User;
import com.example.example100.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BoardHitsRepository boardHitsRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardBadReportRepository boardBadReportRepository;
    private final BoardScrapRepository boardScrapRepository;

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

    @Override
    public ServiceResult setBoardTop(Long id, boolean flag) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }

        Board savedBoard = board.get();
        if (savedBoard.isTopYn() == flag) {
            if (flag) {
                return ServiceResult.fail("이미 최상단에 위치한 게시글입니다.");
            } else {
                return ServiceResult.fail("최상단에 위치한 게시글이 아닙니다.");
            }
        }

        savedBoard.setTopYn(flag);
        boardRepository.save(savedBoard);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardPeriod(Long id, BoardPeriod boardPeriod) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }

        Board savedBoard = board.get();
        savedBoard.setPublishStartDate(boardPeriod.getStartDate());
        savedBoard.setPublishEndDate(boardPeriod.getEndDate());
        boardRepository.save(savedBoard);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult increaseBoardHits(Long id, String email) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board savedBoard = board.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }

        User savedUser = user.get();

        if (boardHitsRepository.countByBoardAndUser(savedBoard, savedUser) > 0) {
            return ServiceResult.fail("이미 조회수를 증가한 이력이 있습니다.");
        }

        boardHitsRepository.save(BoardHits.builder()
                        .board(savedBoard)
                        .user(savedUser)
                        .regDate(LocalDateTime.now())
                        .build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult increaseBoardLike(Long id, String email) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board savedBoard = board.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        long boardLikeCount = boardLikeRepository.countByBoardAndUser(savedBoard, savedUser);
        if (boardLikeCount > 0) {
            return ServiceResult.fail("이미 좋아요한 내역이 있습니다.");
        }

        boardLikeRepository.save(BoardLike.builder()
                        .board(savedBoard)
                        .user(savedUser)
                        .regDate(LocalDateTime.now()).build());

        return ServiceResult.success();
    }

    @Override
    public ServiceResult setBoardUnLike(Long id, String email) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board savedBoard = board.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        Optional<BoardLike> boardLike = boardLikeRepository.findByBoardAndUser(savedBoard, savedUser);
        if (!boardLike.isPresent()) {
            return ServiceResult.fail("좋아요한 내역이 존재하지 않습니다.");
        }

        BoardLike savedBoardLike = boardLike.get();
        boardLikeRepository.delete(savedBoardLike);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult addBadReport(Long id, String email, BoardBadReportInput boardBadReportInput) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board savedBoard = board.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        BoardBadReport boardBadReport = BoardBadReport.builder()
                .userId(savedUser.getId())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getEmail())
                .boardId(savedBoard.getId())
                .boardUserId(savedBoard.getUser().getId())
                .boardTitle(savedBoard.getTitle())
                .boardContents(savedBoard.getContent())
                .boardRegDate(savedBoard.getRegDate())
                .comments(boardBadReportInput.getComments())
                .regDate(LocalDateTime.now())
                .build();

        boardBadReportRepository.save(boardBadReport);

        return ServiceResult.success();
    }

    @Override
    public List<BoardBadReport> getBadReportList() {
        return boardBadReportRepository.findAll();
    }

    @Override
    public ServiceResult scrapBoard(Long id, String email) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            return ServiceResult.fail("게시글이 존재하지 않습니다.");
        }
        Board savedBoard = board.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        BoardScrap boardScrap = BoardScrap.builder()
                .user(savedUser)
                .boardId(savedBoard.getId())
                .boardTypeId(savedBoard.getBoardType().getId())
                .boardUserId(savedBoard.getUser().getId())
                .boardTitle(savedBoard.getTitle())
                .boardContents(savedBoard.getContent())
                .boardRegDate(savedBoard.getRegDate())
                .regDate(LocalDateTime.now())
                .build();
        boardScrapRepository.save(boardScrap);

        return ServiceResult.success();
    }

    @Override
    public ServiceResult removeScrap(Long id, String email) {
        Optional<BoardScrap> boardScrap = boardScrapRepository.findById(id);
        if (!boardScrap.isPresent()) {
            return ServiceResult.fail("삭제할 스크랩이 존재하지 않습니다.");
        }
        BoardScrap savedBoardScrap = boardScrap.get();

        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return ServiceResult.fail("회원정보가 존재하지 않습니다.");
        }
        User savedUser = user.get();

        if (savedBoardScrap.getUser().getId() != savedUser.getId()) {
            return ServiceResult.fail("본인의 스크랩만 삭제할 수 있습니다.");
        }

        boardScrapRepository.delete(savedBoardScrap);

        return ServiceResult.success();
    }
}
