package com.example.example100.common;

import com.example.example100.mail.model.MailInput;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailComponent {
    private final JavaMailSender javaMailSender;

    public boolean send(MailInput mailInput) {
        boolean result = false;

        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8");

                InternetAddress from = new InternetAddress();
                from.setAddress(mailInput.getFromEmail());
                from.setPersonal(mailInput.getFromName());

                InternetAddress to = new InternetAddress();
                to.setAddress(mailInput.getToEmail());
                to.setPersonal(mailInput.getToName());

                mimeMessageHelper.setFrom(from);
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(mailInput.getTitle());
                mimeMessageHelper.setText(mailInput.getContents(), true);
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
