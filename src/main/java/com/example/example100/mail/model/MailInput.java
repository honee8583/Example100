package com.example.example100.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailInput {
    private String title;
    private String contents;
    private String fromEmail;
    private String fromName;
    private String toEmail;
    private String toName;
}
