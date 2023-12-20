package com.example.example100.common.scheduler;

import com.example.example100.logs.service.LogsService;
import com.example.example100.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final LogsService logsService;
    private final UserService userService;

    /**
     * 99. 스프링 스케줄러를 이용하여 매일 새벽 4시에 로그정보를 삭제하는 기능을 작성하시오.
     * 초 분 시 일 월 년
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void deleteLog() {
        log.info("####################");
        log.info("스케줄 실행 -> 로그 삭제");

        logsService.deleteLog();
    }

    /**
     * 100. 스프링 스케줄러를 이용하여 회원중 가입일이 1년이 도래한 회원들에 대해서 서비스 이용내역 통지 메일을 보내는 기능을 작성하시오.
     */
    @Scheduled(cron = "0 0 12 * * *")
    public void sendServiceNotice() {
        log.info("####################");
        log.info("스케줄 실행 -> 로그 삭제");

        userService.sendServiceNotice();
    }
}
