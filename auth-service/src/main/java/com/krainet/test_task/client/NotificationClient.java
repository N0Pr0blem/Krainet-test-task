package com.krainet.test_task.client;

import com.krainet.test_task.dto.notification.MailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://notification-service:8082/api/v1/mails")
public interface NotificationClient {
    @PostMapping()
    void send(@RequestBody MailRequestDto mailRequestDto);
}
