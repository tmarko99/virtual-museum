package com.springboot.museum.service;

import com.springboot.museum.entity.Obilazak;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.ObilazakDTO;
import com.springboot.museum.payload.UpdateOcene;
import com.springboot.museum.security.UserPrincipal;

import java.util.List;

public interface ObilazakService {
    ApiResponse createObilazak(long id, UserPrincipal currentUser);
    List<Obilazak> findAll(UserPrincipal currentUser);
    ApiResponse changeStatusToZavrsen(long id);
    void changeStatusToOtkazan(long id);
    void oceniObilazak(int ocena, long id);
    List<ObilazakDTO> findAllByUserId(UserPrincipal currentUser);
    String obrisiObilazak(long eksponatId, long obilazakId, UserPrincipal currentUser);
}
