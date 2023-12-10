package com.example.example100.notice.repository;

import com.example.example100.notice.model.BoardCountResponse;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardTypeCustomRepository {
    private final EntityManager entityManager;

    public List<BoardCountResponse> getBoardCountByBoardType() {
        String sql = "select bt.id, bt.board_name, bt.reg_date, bt.using_yn, "
                + "(select count(*) from BOARD b where b.board_type_id = bt.id) board_count "
                + "from BOARD_TYPE bt";

        Query nativeQuery = entityManager.createNativeQuery(sql);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        List<BoardCountResponse> boardCountResponses = jpaResultMapper.list(nativeQuery, BoardCountResponse.class);

        return boardCountResponses;
    }
}
