package com.springboot.museum.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.museum.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ObilazakDTO {
    private Long id;
    private String naziv;
    private String opisEksponata;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime vremeObilaska;
    private Status status;
    private int ocena;
    private int eksponatId;
    private String userIme;

}
