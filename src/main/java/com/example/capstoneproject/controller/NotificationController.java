package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.NotificationMessage;
import com.example.capstoneproject.service.impl.FirebaseMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    FirebaseMessageServiceImpl firebaseMessageService;

    @PostMapping
    public String sendNotification(@RequestBody NotificationMessage notificationMessage){
        return  firebaseMessageService.sendNotificationByToken(notificationMessage);
    }
}
