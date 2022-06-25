package com.springboot.museum.controller;

import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.EksponatDTO;
import com.springboot.museum.payload.PagedResponse;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.EksponatService;
import com.springboot.museum.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/eksponati")
public class EksponatController {

    @Autowired
    private EksponatService eksponatService;

    @GetMapping
    public PagedResponse findAll(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                              @RequestParam(value = "sortField", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortField,
                                              @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){
        return eksponatService.findAll(pageNumber, pageSize, sortField, sortDir);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EksponatDTO> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(eksponatService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EksponatDTO> createEksponat(@Valid @RequestBody EksponatDTO eksponatDTO){
        return new ResponseEntity<>(eksponatService.createEksponat(eksponatDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EksponatDTO> updateEksponat(@Valid @RequestBody EksponatDTO eksponatDTO,
                                                      @PathVariable("id") long id,
                                                      @AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(eksponatService.updateEksponat(eksponatDTO, id, currentUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEksponat(@PathVariable("id") Long id){
        return new ResponseEntity<>(eksponatService.deleteEksponat(id), HttpStatus.OK);
    }
}
