package com.krainet.test_task.service.impl;

import com.krainet.test_task.client.NotificationClient;
import com.krainet.test_task.dto.notification.MailRequestDto;
import com.krainet.test_task.dto.user.UserMailDto;
import com.krainet.test_task.model.UserChangeType;
import com.krainet.test_task.service.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final NotificationClient notificationClient;

    private Logger logger = LogManager.getLogger(MailServiceImpl.class);

    @Override
    public void sendMails(List<String> mails, UserMailDto userMailDto, UserChangeType userChangeType) {
        logger.info("Sending request to notification service");
        notificationClient.send(MailRequestDto.builder()
                .user(userMailDto)
                .mails(mails)
                .changeType(userChangeType)
                .build());
    }
}
