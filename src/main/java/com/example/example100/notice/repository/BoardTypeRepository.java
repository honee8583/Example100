package com.example.example100.notice.repository;

import com.example.example100.notice.entity.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {
    BoardType findByBoardName(String name);
}
