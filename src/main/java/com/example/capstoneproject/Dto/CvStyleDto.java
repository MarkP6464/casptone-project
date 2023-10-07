package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class CvStyleDto {
    String fontSize = "9pt";
    Double lineHeight = 1.4;
    String fontFamily = "Merriweather";
    String fontWeight = "normal";
    String zoom = "130%";
    String paperSize = "letter";
    Boolean hasDivider = true;
    Boolean hasIndent = false;
    String fontColor = "rgb(0, 0, 0)";

}
