package com.springboot.museum.controller;

import com.springboot.museum.entity.Obilazak;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.ObilazakDTO;
import com.springboot.museum.payload.UpdateOcene;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.ObilazakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class ObilazakController {

    @Autowired
    private ObilazakService obilazakService;

    @GetMapping("eksponat/{eksponatId}/obilasci")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Obilazak> pronadjiSveObilaske(@PathVariable("eksponatId") Long id,
                                              @AuthenticationPrincipal UserPrincipal currentUser){
        return obilazakService.findAll(currentUser);
    }

    @GetMapping("eksponat/me/obilasci")
    @PreAuthorize("hasRole('USER')")
    public List<ObilazakDTO> pronadjiSveObilaskeKorisnika(@AuthenticationPrincipal UserPrincipal currentUser){
        return obilazakService.findAllByUserId(currentUser);
    }


    @PostMapping("/eksponat/{eksponatId}/obilasci")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> createObilazak(@PathVariable("eksponatId") Long id,
                                                   @AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(obilazakService.createObilazak(id, currentUser), HttpStatus.CREATED);
    }

    @PutMapping("/obilazak/{id}/zavrsen")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> changeStatusToZavrsen(@PathVariable("id") Long id){
        return new ResponseEntity<>(obilazakService.changeStatusToZavrsen(id), HttpStatus.OK);
    }

    @PutMapping("/obilazak/{id}/otkazan")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatusToOtkazan(@PathVariable("id") Long id){
        obilazakService.changeStatusToOtkazan(id);
    }

    @PatchMapping("/obilazak/{id}/oceni")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void oceniObilazak(@RequestBody int ocena, @PathVariable("id") Long id){
        obilazakService.oceniObilazak(ocena, id);
    }

    @DeleteMapping("/eksponat/{eksponatId}/obilasci/{obilazakId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> obrisiObilazak(@PathVariable("eksponatId") long eksponatId,
                                                 @PathVariable("obilazakId") long obilazakId,
                                                 @AuthenticationPrincipal UserPrincipal currentUser){

        return new ResponseEntity<>(obilazakService.obrisiObilazak(eksponatId, obilazakId, currentUser), HttpStatus.OK);
    }

}
