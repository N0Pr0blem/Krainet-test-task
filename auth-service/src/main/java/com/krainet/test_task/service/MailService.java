package com.krainet.test_task.service;

import com.krainet.test_task.dto.user.UserMailDto;
import com.krainet.test_task.model.UserChangeType;

import java.util.List;

public interface MailService {
    void sendMails(List<String> mails, UserMailDto userMailDto, UserChangeType userChangeType);
}
