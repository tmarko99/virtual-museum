package com.springboot.museum.service.impl;

import com.springboot.museum.entity.Eksponat;
import com.springboot.museum.entity.Obilazak;
import com.springboot.museum.entity.Status;
import com.springboot.museum.entity.User;
import com.springboot.museum.exception.AppException;
import com.springboot.museum.exception.BadRequestException;
import com.springboot.museum.exception.ResourceNotFoundException;
import com.springboot.museum.exception.UnauthorizedException;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.ObilazakDTO;
import com.springboot.museum.payload.UpdateOcene;
import com.springboot.museum.repository.EksponatRepository;
import com.springboot.museum.repository.ObilazakRepository;
import com.springboot.museum.repository.UserRepository;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.ObilazakService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObilazakServiceImpl implements ObilazakService {

    @Autowired
    private ObilazakRepository obilazakRepository;

    @Autowired
    private EksponatRepository eksponatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse createObilazak(long id, UserPrincipal currentUser) {
        Eksponat eksponat = eksponatRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", id));

        User user = userRepository.findById(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", currentUser.getUser().getId()));

        Obilazak obilazak = new Obilazak();

        ApiResponse apiResponse;

        List<Obilazak> obilasciUsera = obilazakRepository.findByUser_Id(user.getId());
        int brojObilazaka = (int) obilasciUsera.stream().filter(obilazak1 -> obilazak1.getEksponat().getId().equals(eksponat.getId())).count();

        if(brojObilazaka > 0){
            apiResponse = new ApiResponse(Boolean.FALSE, "Vec ste dodali obilazak u planer");
            throw new BadRequestException(apiResponse);
        } else {
            obilazak.setNaziv(eksponat.getNaziv());
            obilazak.setOpisEksponata(eksponat.getOpis());
            obilazak.setStatus(Status.TEKUCI);
            obilazak.setVremeObilaska(eksponat.getVremeObilaska());
            obilazak.setEksponat(eksponat);
            obilazak.setUser(user);
        }

        obilazakRepository.save(obilazak);

        apiResponse = new ApiResponse(Boolean.TRUE, "Obilazak uspesno kreiran");

        return apiResponse;
    }

    @Override
    public List<Obilazak> findAll(UserPrincipal currentUser) {
        return obilazakRepository.findAll();
    }

    @Override
    public ApiResponse changeStatusToZavrsen(long id) {
        Obilazak obilazak = obilazakRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Obilazak", "id", id));

        LocalDateTime currentTime = LocalDateTime.now();

        ApiResponse apiResponse = null;

        if(obilazak.getVremeObilaska().isAfter(currentTime)){
            apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete zavrsiti obilazan pre nego on pocne");
            throw new BadRequestException(apiResponse);
        }
        else if(obilazak.getStatus().equals(Status.OTKAZAN)){
            apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete zavrsiti obilazak koje je u statusu otkazan");
            throw new BadRequestException(apiResponse);
        }
        else if(obilazak.getStatus().equals(Status.ZAVRSEN)){
            apiResponse = new ApiResponse(Boolean.FALSE, "Obilazak je vec u statusu zavrsen");
            throw new BadRequestException(apiResponse);
        }
        else{
            obilazak.setStatus(Status.ZAVRSEN);
            obilazakRepository.save(obilazak);
            apiResponse = new ApiResponse(Boolean.TRUE, "Status obilaska je promenjen na ZAVRSEN");
        }

        return apiResponse;
    }

    @Override
    public void changeStatusToOtkazan(long id) {
        Obilazak obilazak = obilazakRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Obilazak", "id", id));

        LocalDateTime currentTime = LocalDateTime.now();

        if(obilazak.getVremeObilaska().isBefore(currentTime)){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete otkazati obilazak koji je vec bio");
            throw new BadRequestException(apiResponse);
        }
        else if(obilazak.getStatus().equals(Status.ZAVRSEN)){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete otkazati obilazak koje je u statusu zavrsen");
            throw new BadRequestException(apiResponse);
        }
        else if(obilazak.getStatus().equals(Status.OTKAZAN)){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Obilazak je vec u statusu otkazan");
            throw new BadRequestException(apiResponse);
        }
        else{
            obilazak.setStatus(Status.OTKAZAN);
            obilazakRepository.save(obilazak);
        }
    }

    @Override
    public void oceniObilazak(int ocena, long id) {
        Obilazak obilazak = obilazakRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Obilazak", "id", id));


        if(obilazak.getStatus().equals(Status.ZAVRSEN)){
            obilazak.setOcena(ocena);
            obilazakRepository.save(obilazak);
        }
        else{
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Problem prilikom dodavanja ocene");
            throw new BadRequestException(apiResponse);
        }
    }

    @Override
    public List<ObilazakDTO> findAllByUserId(UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", currentUser.getId()));

        List<ObilazakDTO> obilasci = obilazakRepository.findByUser_Id(user.getId()).stream()
                .map(obilazak -> mapToDTO(obilazak)).collect(Collectors.toList());

        return obilasci;
    }

    @Override
    public String obrisiObilazak(long eksponatId, long obilazakId, UserPrincipal currentUser) {
        Eksponat eksponat = eksponatRepository.findById(eksponatId).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", eksponatId));

        Obilazak obilazak = obilazakRepository.findById(obilazakId).orElseThrow(() ->
                new ResourceNotFoundException("Obilazak", "id", obilazakId));

        if(!eksponat.getId().equals(obilazak.getEksponat().getId())){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Obilazak ne pripada datom eksponatu");
            throw new BadRequestException(apiResponse);
        }

        if(!obilazak.getUser().getId().equals(currentUser.getId())){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete obrisati obilazak koje niste vi kreirali");
            throw new BadRequestException(apiResponse);
        }

        if(!obilazak.getStatus().equals(Status.ZAVRSEN)){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete obrisati obilazak koje nije u stanju zavrsen");
            throw new BadRequestException(apiResponse);
        }

        obilazakRepository.delete(obilazak);

        return "Obilazak je uspesno uklonjen!";
    }

    private ObilazakDTO mapToDTO(Obilazak obilazak){
        return modelMapper.map(obilazak, ObilazakDTO.class);
    }

}
