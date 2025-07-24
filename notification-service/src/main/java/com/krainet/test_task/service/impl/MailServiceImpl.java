package com.krainet.test_task.service.impl;

import com.krainet.test_task.service.MailService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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

    private final String EMAIL_FROM = "TestTaskKrainet@miniuser.ru";

    private final JavaMailSender mailSender;
    private ExecutorService emailExecutor;

    @PostConstruct
    public void init() {
        emailExecutor = Executors.newFixedThreadPool(COUNT_OF_THREADS_FOR_MAIL_SERVICE);
    }

    @Override
    public void sendSimpleMail(String to, String subject, String text) {
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