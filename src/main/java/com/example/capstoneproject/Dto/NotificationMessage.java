package com.example.capstoneproject.Dto;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationMessage {
    private String token;
    private String title;
    private String body;
    private String image;
    private Map<String,String> data;


}
