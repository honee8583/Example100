package com.example.example100.board.repository;

import com.example.example100.board.entity.BoardComment;
import com.example.example100.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findByUser(User user);
}
