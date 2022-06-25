package com.springboot.museum.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPayload {
    private String ime;
    private String prezime;
    private String adresa;
    private String telefon;
}
