package com.example.example100.notice.repository;

import com.example.example100.notice.entity.Notice;
import com.example.example100.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<List<Notice>> findByIdIn(List<Long> ids);

    Optional<List<Notice>> findByTitleAndContentAndRegDateIsGreaterThanEqual(String title, String content, LocalDateTime checkDate);

    int countByTitleAndContentAndRegDateIsGreaterThanEqual(String title, String content, LocalDateTime checkDate);

    Optional<List<Notice>> findByUser(User user);
}
