package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CvDto {

    private Integer id;

    private String Content;

    private String Summary;
    private BasicStatus Status;

    private CvBodyDto cvBody;

    private UsersViewDto Users;

    private TemplateViewDto template;

}
