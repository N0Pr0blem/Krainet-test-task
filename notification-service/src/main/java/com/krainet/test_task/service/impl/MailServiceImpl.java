package com.krainet.test_task.service.impl;

import com.krainet.test_task.dto.MailRequestDto;
import com.krainet.test_task.service.MailService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.properties.thread_count}")
    private int COUNT_OF_THREADS_FOR_MAIL_SERVICE;

    private final JavaMailSender mailSender;

    private Logger logger = LogManager.getLogger(MailServiceImpl.class);
    private ExecutorService emailExecutor;

    private final String EMAIL_FROM = "TestTaskKrainet@miniuser.ru";

    @PostConstruct
    public void init() {
        emailExecutor = Executors.newFixedThreadPool(COUNT_OF_THREADS_FOR_MAIL_SERVICE);
    }

    @Override
    public void sendMails(MailRequestDto mailRequestDto) {
        String subject = String.format("%s пользователь %s",
                mailRequestDto.getChangeType().getTitle(),
                mailRequestDto.getUser().getUsername()
        );
        String text = String.format("%s пользователь с именем - %s, паролем - %s и почтой - %s",
                mailRequestDto.getChangeType().getTitle(),
                mailRequestDto.getUser().getUsername(),
                mailRequestDto.getUser().getPassword(),
                mailRequestDto.getUser().getEmail()
        );

        for(String mail : mailRequestDto.getMails()){
            sendSimpleMail(mail,subject,text);
            logger.info("Send mail to "+mail);
        }
    }

    private void sendSimpleMail(String to, String subject, String text) {
        emailExecutor.submit(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(EMAIL_FROM);

            mailSender.send(message);
        });
    }

    @PreDestroy
    public void shutdownExecutor() {
        emailExecutor.shutdown();
    }
}