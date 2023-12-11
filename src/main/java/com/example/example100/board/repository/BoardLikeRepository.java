package com.example.example100.board.repository;

import com.example.example100.board.entity.Board;
import com.example.example100.board.entity.BoardLike;
import com.example.example100.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    long countByBoardAndUser(Board board, User user);
    Optional<BoardLike> findByBoardAndUser(Board board, User user);
}
