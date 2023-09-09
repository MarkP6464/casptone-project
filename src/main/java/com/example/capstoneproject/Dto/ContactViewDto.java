package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactViewDto {
    private int id;

    private String FullName;

    private String Phone;

    private Date Website;

    private String State;

    private String Email;

    private Date Linkin;

    private String Country;
}
