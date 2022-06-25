package com.springboot.museum.service.impl;

import com.springboot.museum.entity.Eksponat;
import com.springboot.museum.exception.AccessDeniedException;
import com.springboot.museum.exception.ResourceNotFoundException;
import com.springboot.museum.exception.UnauthorizedException;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.EksponatDTO;
import com.springboot.museum.payload.PagedResponse;
import com.springboot.museum.repository.EksponatRepository;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.EksponatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EksponatServiceImpl implements EksponatService {

    @Autowired
    private EksponatRepository eksponatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EksponatDTO createEksponat(EksponatDTO eksponatDTO) {
        Eksponat eksponat = mapToEntity(eksponatDTO);
        Eksponat savedEksponat = eksponatRepository.save(eksponat);

        return mapToDTO(savedEksponat);
    }

    @Override
    public List<EksponatDTO> findAll() {
        return eksponatRepository.findAll().stream().map(eksponat -> mapToDTO(eksponat)).collect(Collectors.toList());
    }

    @Override
    public PagedResponse<EksponatDTO> findAll(int pageNumber, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Eksponat> eksponati = eksponatRepository.findAll(pageable);

        List<Eksponat> listaEksponata = eksponati.getContent();

        List<EksponatDTO> eksponatiDTO = listaEksponata.stream().map(eksponat -> mapToDTO(eksponat)).collect(Collectors.toList());
        PagedResponse<EksponatDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(eksponatiDTO);
        pagedResponse.setPageNumber(eksponati.getNumber());
        pagedResponse.setPageSize(eksponati.getSize());
        pagedResponse.setTotalElements(eksponati.getTotalElements());
        pagedResponse.setTotalPages(eksponati.getTotalPages());
        pagedResponse.setLast(eksponati.isLast());

        return pagedResponse;
    }

    @Override
    public EksponatDTO findById(Long id) {
        Eksponat eksponat = eksponatRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", id));

        EksponatDTO eksponatDTO = mapToDTO(eksponat);

        return eksponatDTO;
    }

    @Override
    public EksponatDTO updateEksponat(EksponatDTO newEksponat, Long id, UserPrincipal currentUser) {
        Eksponat eksponat = eksponatRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", id));

        if(!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Nemate dozvolu da menjate eksponat");
            throw new AccessDeniedException(apiResponse);
        }

        else{
            eksponat.setNaziv(newEksponat.getNaziv());
            eksponat.setOpis(newEksponat.getOpis());
            eksponat.setPhotoUrl(newEksponat.getPhotoUrl());
            eksponat.setVremeObilaska(newEksponat.getVremeObilaska());
            eksponat.setZemljaPorekla(newEksponat.getZemljaPorekla());
        }

        Eksponat savedEksponat = eksponatRepository.save(eksponat);

        return mapToDTO(savedEksponat);
    }

    @Override
    public ApiResponse deleteEksponat(Long id) {
        eksponatRepository.deleteById(id);

        return new ApiResponse(true, "Eksponat uspesno obrisan!");
    }


    private EksponatDTO mapToDTO(Eksponat eksponat){
        EksponatDTO eksponatDTO = modelMapper.map(eksponat, EksponatDTO.class);

        return eksponatDTO;
    }

    private Eksponat mapToEntity(EksponatDTO eksponatDTO){
        Eksponat eksponat = modelMapper.map(eksponatDTO, Eksponat.class);

        return eksponat;
    }
}
