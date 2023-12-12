package com.example.example100.board.repository;

import com.example.example100.board.entity.BoardBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {
}
