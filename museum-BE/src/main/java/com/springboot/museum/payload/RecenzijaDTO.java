package com.springboot.museum.payload;

import com.springboot.museum.entity.User;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RecenzijaDTO {
    private long id;
    @NotEmpty(message = "Komentar ne sme ostati prazan")
    private String komentar;
    @NotNull(message = "Morate dati ocenu")
    @Min(1)
    @Max(10)
    private int ocena;
    private String userIme;
}
