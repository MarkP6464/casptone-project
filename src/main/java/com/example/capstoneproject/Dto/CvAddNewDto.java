package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Contact;
import com.example.capstoneproject.entity.Customer;
import com.example.capstoneproject.entity.Template;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CvAddNewDto {
    private int id;

    private String Content;

    private String Summary;

    private CvStatus Status;

    private Customer customer;

    private Template template;

    private Contact contact;
}
