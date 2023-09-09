package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactDto {

    private String FullName;

    private String Phone;

    private Date Website;

    private String State;

    private String Email;

    private Date Linkin;

    private String Country;
}
