package com.springboot.museum.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springboot.museum.entity.Recenzija;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class EksponatDTO {
    private long id;
    @NotEmpty
    @Size(min = 5, message = "Naziv mora imati bar 5 karaktera")
    private String naziv;

    @NotEmpty
    @Size(min = 10, message = "Opis mora imati bar 10 karaktera")
    private String opis;

    private String photoUrl;

    @NotNull
    private int cenaObilaska;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime vremeObilaska;

    private String zemljaPorekla;

    private List<RecenzijaDTO> recenzije;
}
