package com.example.example100.user.repository;

import com.example.example100.user.model.UserLogCount;
import com.example.example100.user.model.UserNoticeCount;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {
    private final EntityManager entityManager;

    public List<UserNoticeCount> findUserNoticeCount() {
        // 네이티브 쿼리 사용.
        String sql = " select u.id, u.email, u.user_name, (select count(*) from Notice n where n.user_id = u.id) notice_count from USER_ENTITY u ";
        List<UserNoticeCount> userNoticeCounts = entityManager.createNativeQuery(sql).getResultList();

        return userNoticeCounts;
    }

    public List<UserLogCount> findUserLogCount() {
        String sql = " select u.id, u.email, u.user_name, "
                + "(select count(*) from NOTICE n where n.user_id = u.id) notice_count, "
                + "(select count(*) from NOTICE_LIKE nl where nl.user_id = u.id) notice_like_count "
                + "from USER_ENTITY u ";
        List<UserLogCount> userLogCounts = entityManager.createNativeQuery(sql).getResultList();

        return userLogCounts;
    }
}
