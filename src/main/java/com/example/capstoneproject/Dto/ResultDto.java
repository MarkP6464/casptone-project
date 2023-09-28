package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultDto {
    private List<BulletPointDto> evaluate;
//    private List<BulletPointDto> punctuatedList;
//    private List<BulletPointDto> numberList;
//    private List<BulletPointDto> personalPronounsList;
//    private List<BulletPointDto> FillerList;
//    private List<BulletPointDto> QuantifiedList;
//    private List<BulletPointDto> BuzzwordsList;
}
