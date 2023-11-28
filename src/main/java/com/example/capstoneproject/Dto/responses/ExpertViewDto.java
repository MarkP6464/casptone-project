package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.ExpertDto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpertViewDto {
    private int id;

    private String name;

    private String avatar;

    private String phone;

    private String personalWebsite;

    private String email;

    private String linkin;

    private ExpertDto expert;

}
