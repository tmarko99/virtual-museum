package com.springboot.museum.service;

import com.springboot.museum.entity.Eksponat;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.EksponatDTO;
import com.springboot.museum.payload.PagedResponse;
import com.springboot.museum.security.UserPrincipal;

import java.util.List;

public interface EksponatService {

    EksponatDTO createEksponat(EksponatDTO eksponatDTO);
    List<EksponatDTO> findAll();
    PagedResponse<EksponatDTO> findAll(int pageNumber, int pageSize, String sortField, String sortDir);
    EksponatDTO findById(Long id);
    EksponatDTO updateEksponat(EksponatDTO newEksponat, Long id, UserPrincipal currentUser);
    ApiResponse deleteEksponat(Long id);
}
