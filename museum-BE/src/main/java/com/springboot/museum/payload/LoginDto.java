package com.springboot.museum.payload;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
