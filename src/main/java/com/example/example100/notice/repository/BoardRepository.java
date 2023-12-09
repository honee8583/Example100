package com.example.example100.notice.repository;

import com.example.example100.notice.entity.Board;
import com.example.example100.notice.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    long countByBoardType(BoardType boardType);
}
