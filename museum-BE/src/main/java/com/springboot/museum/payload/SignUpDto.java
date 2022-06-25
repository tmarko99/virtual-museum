package com.springboot.museum.payload;

import com.springboot.museum.entity.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignUpDto {
    @NotEmpty
    @Size(min = 2, message = "Ime mora imati bar 2 karaktera")
    private String ime;
    @NotEmpty
    private String prezime;
    private String telefon;
    private String adresa;
    @NotEmpty
    @Email(message = "Email mora biti u ispravnom formatu")
    private String email;
    private String password;
//    private Set<String> roles;
}
