package com.example.example100.board.repository;

import com.example.example100.board.entity.Board;
import com.example.example100.board.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    long countByBoardType(BoardType boardType);
}
