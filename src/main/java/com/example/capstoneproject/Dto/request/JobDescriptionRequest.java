package com.example.capstoneproject.Dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDescriptionRequest {
    String title;
    String company;
    String description;
    Integer jobPostingId;
}
