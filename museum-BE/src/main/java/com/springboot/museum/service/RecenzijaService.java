package com.springboot.museum.service;

import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.RecenzijaDTO;
import com.springboot.museum.security.UserPrincipal;

import java.util.List;

public interface RecenzijaService {
    RecenzijaDTO createRecenzija(RecenzijaDTO recenzijaDTO, long id, UserPrincipal currentUser);
    List<RecenzijaDTO> getAllRecenzijeByEksponatId(long id);
    ApiResponse deleteRecenzija(long eksponatId, long recenzijaId, UserPrincipal currentUser);

}
