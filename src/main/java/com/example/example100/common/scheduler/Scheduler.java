package com.example.example100.common.scheduler;

import com.example.example100.logs.service.LogsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final LogsService logsService;

    /**
     * 99. 스프링 스케줄러를 이용하여 매일 새벽 4시에 로그정보를 삭제하는 기능을 작성하시오.
     * 초 분 시 일 월 년
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void deleteLog() {
//        log.info("$$$$$$$$$$$$$$$$$$$$");
//        log.info("스케줄 실행 -> 로그 삭제");
//        log.info("$$$$$$$$$$$$$$$$$$$$");
        logsService.deleteLog();

    }
}
