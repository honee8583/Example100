package com.example.example100.mail.repository;

import com.example.example100.mail.entity.MailTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTemplateRepository extends JpaRepository<MailTemplate, Long> {
    Optional<MailTemplate> findByTemplateId(String templateId);
}
