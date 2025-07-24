package com.krainet.test_task.service;

import com.krainet.test_task.dto.MailRequestDto;

public interface MailService {
    void sendMails(MailRequestDto mailRequestDto);
}
