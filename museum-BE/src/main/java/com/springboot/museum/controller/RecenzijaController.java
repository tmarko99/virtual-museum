package com.springboot.museum.controller;

import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.RecenzijaDTO;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.RecenzijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class RecenzijaController {

    @Autowired
    private RecenzijaService recenzijaService;

    @GetMapping("/eksponat/{eksponatId}/recenzije")
    public List<RecenzijaDTO> getAllRecenzijeByEksponatId(@PathVariable("eksponatId") Long id){
        return recenzijaService.getAllRecenzijeByEksponatId(id);
    }


    @PostMapping("/eksponat/{eksponatId}/recenzije")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RecenzijaDTO> createRecenzija(@Valid @RequestBody RecenzijaDTO recenzijaDTO,
                                                        @PathVariable("eksponatId") Long id,
                                                        @AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(recenzijaService.createRecenzija(recenzijaDTO, id, currentUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/eksponat/{eksponatId}/recenzije/{recenzijaId}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse> deleteRecenzija(@PathVariable("eksponatId") Long eksponatId,
                                                       @PathVariable("recenzijaId") Long recenzijaId,
                                                       @AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(recenzijaService.deleteRecenzija(eksponatId, recenzijaId, currentUser), HttpStatus.OK);
    }
}
