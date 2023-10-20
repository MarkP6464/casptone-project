package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private String text;
    private String comment;
}
