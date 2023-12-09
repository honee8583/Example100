package com.example.example100.notice.service;

import com.example.example100.notice.model.BoardTypeInput;
import com.example.example100.notice.model.BoardTypeUpdateInput;
import com.example.example100.notice.model.ServiceResult;

public interface BoardService {
    ServiceResult addBoard(BoardTypeInput boardTypeInput);

    ServiceResult updateBoard(BoardTypeUpdateInput boardTypeUpdateInput);
}
