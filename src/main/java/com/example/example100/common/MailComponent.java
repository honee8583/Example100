package com.example.example100.common;

import com.example.example100.common.model.JoinSuccessMailForm;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public boolean send(JoinSuccessMailForm mailForm) {
        boolean result = false;

        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8");

                InternetAddress from = new InternetAddress();
                from.setAddress(fromEmail);
                from.setPersonal(mailForm.getFromName());

                InternetAddress to = new InternetAddress();
                to.setAddress(mailForm.getToEmail());
                to.setPersonal(mailForm.getToName());

                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(mailForm.getTitle());
                mimeMessageHelper.setText(mailForm.getContents(), true);
            }
        };

        try{
            javaMailSender.send(mimeMessagePreparator);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return result;
    }
}
