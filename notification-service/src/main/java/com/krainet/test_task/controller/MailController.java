package com.krainet.test_task.controller;

import com.krainet.test_task.dto.MailRequestDto;
import com.krainet.test_task.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mails")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping
    public ResponseEntity<String> sendMails(@RequestBody MailRequestDto mailRequestDto){
        for (String mail: mailRequestDto.getMails()){
            mailService.sendSimpleMail(mail,mailRequestDto.getSubject(),mailRequestDto.getText());
        }
        return ResponseEntity.ok("Mails successfully send");
    }
}
