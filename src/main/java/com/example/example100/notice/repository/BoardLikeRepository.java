package com.example.example100.notice.repository;

import com.example.example100.notice.entity.Board;
import com.example.example100.notice.entity.BoardLike;
import com.example.example100.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    long countByBoardAndUser(Board board, User user);
    Optional<BoardLike> findByBoardAndUser(Board board, User user);
}
