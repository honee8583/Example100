package com.example.example100.board.service;

import com.example.example100.board.entity.Board;
import com.example.example100.board.entity.BoardBadReport;
import com.example.example100.board.entity.BoardType;
import com.example.example100.board.model.BoardBadReportInput;
import com.example.example100.board.model.BoardCountResponse;
import com.example.example100.board.model.BoardPeriod;
import com.example.example100.board.model.BoardTypeEnabledInput;
import com.example.example100.board.model.BoardTypeInput;
import com.example.example100.board.model.BoardTypeUpdateInput;
import com.example.example100.common.model.ServiceResult;
import com.example.example100.user.model.BoardCommentResponse;
import java.util.List;

public interface BoardService {
    ServiceResult addBoard(BoardTypeInput boardTypeInput);

    ServiceResult updateBoard(BoardTypeUpdateInput boardTypeUpdateInput);

    ServiceResult deleteBoard(Long id);

    List<BoardType> getBoardTypeList();

    ServiceResult updateBoardTypeEnabled(Long id, BoardTypeEnabledInput boardTypeEnabledInput);

    List<BoardCountResponse> getBoardCountByBoardType();

    ServiceResult setBoardTop(Long id, boolean flag);

    ServiceResult setBoardPeriod(Long id, BoardPeriod boardPeriod);

    ServiceResult increaseBoardHits(Long id, String email);

    ServiceResult increaseBoardLike(Long id, String email);

    ServiceResult setBoardUnLike(Long id, String email);

    ServiceResult addBadReport(Long id, String email, BoardBadReportInput boardBadReportInput);

    List<BoardBadReport> getBadReportList();

    ServiceResult scrapBoard(Long id, String email);

    ServiceResult removeScrap(Long id, String email);

    ServiceResult addBookmark(Long id, String email);

    ServiceResult removeBookmark(Long id, String email);

    List<Board> getMyBoardList(String email);

    List<BoardCommentResponse> getMyBoardCommentList(String email);

    Board detail(Long id);

    List<Board> list();
}
