package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {
    private Integer id;

    private String version;

    private String cvBody;

    private LocalDate timestamp;

    private Cv cv;
}
