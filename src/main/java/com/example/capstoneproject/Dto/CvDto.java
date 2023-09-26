package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CvDto {

    private int id;

    private String Content;

    private String Summary;

    private String cvBody;

    private UsersViewDto Users;

    private TemplateViewDto template;

}
