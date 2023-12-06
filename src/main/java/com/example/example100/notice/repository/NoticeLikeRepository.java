package com.example.example100.notice.repository;

import com.example.example100.notice.entity.NoticeLike;
import com.example.example100.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {
    List<NoticeLike> findByUser(User user);
}
