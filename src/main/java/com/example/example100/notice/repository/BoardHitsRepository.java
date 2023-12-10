package com.example.example100.notice.repository;

import com.example.example100.notice.entity.Board;
import com.example.example100.notice.entity.BoardHits;
import com.example.example100.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardHitsRepository extends JpaRepository<BoardHits, Long> {
    long countByBoardAndUser(Board board, User user);
}
