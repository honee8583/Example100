package com.example.example100.notice.service;

import com.example.example100.notice.entity.BoardType;
import com.example.example100.notice.model.BoardCountResponse;
import com.example.example100.notice.model.BoardPeriod;
import com.example.example100.notice.model.BoardTypeEnabledInput;
import com.example.example100.notice.model.BoardTypeInput;
import com.example.example100.notice.model.BoardTypeUpdateInput;
import com.example.example100.notice.model.ServiceResult;
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
}
