package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCoverLetterDto {
    private String Name;
    private String Phone;
    private String Email;
    private String Address;
}
