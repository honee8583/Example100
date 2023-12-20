package com.example.example100.logs.service.impl;

import com.example.example100.logs.entity.Logs;
import com.example.example100.logs.repository.LogsRepository;
import com.example.example100.logs.service.LogsService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsService {
    private final LogsRepository logsRepository;

    @Override
    public void add(String text) {
        logsRepository.save(Logs.builder()
                .text(text)
                .regDate(LocalDateTime.now())
                .build());
    }

    @Override
    public void deleteLog() {
        logsRepository.deleteAll();
    }
}
