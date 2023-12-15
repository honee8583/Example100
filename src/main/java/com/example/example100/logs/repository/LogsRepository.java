package com.example.example100.logs.repository;

import com.example.example100.logs.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Logs, Long> {

}
