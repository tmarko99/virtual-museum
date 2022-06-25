package com.springboot.museum.service.impl;

import com.springboot.museum.entity.*;
import com.springboot.museum.exception.BadRequestException;
import com.springboot.museum.exception.ResourceNotFoundException;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.RecenzijaDTO;
import com.springboot.museum.repository.EksponatRepository;
import com.springboot.museum.repository.RecenzijaRepository;
import com.springboot.museum.repository.UserRepository;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.RecenzijaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecenzijaServiceImpl implements RecenzijaService {

    @Autowired
    private RecenzijaRepository recenzijaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EksponatRepository eksponatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RecenzijaDTO createRecenzija(RecenzijaDTO recenzijaDTO, long id, UserPrincipal currentUser) {
        Eksponat eksponat = eksponatRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", id));

        User user = userRepository.findById(currentUser.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", currentUser.getUser().getId()));

        List<Recenzija> recenzijeKorisnika = recenzijaRepository.findByUser_Id(user.getId());

        //long brojRecenzija = recenzijeKorisnika.stream().filter(recenzija -> recenzija.getEksponat().getId().equals(user.getRecenzije().get().getId())).count();
        boolean provera = false;

        for(Recenzija recenzija : eksponat.getRecenzije()){
            for(Recenzija recenzijeUsera : user.getRecenzije()){
                if (recenzija.getEksponat().getId().equals(recenzijeUsera.getEksponat().getId())) {
                    provera = true;
                    break;
                }
            }
        }

        if(provera){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Vec ste kreirali recenziju za ovaj eksponat!");
            throw new BadRequestException(apiResponse);
        }

        Recenzija savedRecenzija = null;

        if(user.getObilasci().stream().noneMatch(obilazak ->
                (obilazak.getEksponat().getId().equals(eksponat.getId())))){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete ostaviti recenzuju za eksponat koji niste pogledali");
            throw new BadRequestException(apiResponse);
        }

        if(user.getObilasci().stream().anyMatch(obilazak -> !(obilazak.getStatus().equals(Status.ZAVRSEN)) && obilazak.getEksponat().getId().equals(eksponat.getId()))){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete ostaviti recenzuju dok ne bude u stanju ZAVRSEN-a");
            throw new BadRequestException(apiResponse);
        }

        if(user.getObilasci().stream().anyMatch(obilazak -> obilazak.getUser().getId().equals(user.getId())
                && obilazak.getEksponat().getId().equals(eksponat.getId()) &&
                obilazak.getStatus().equals(Status.ZAVRSEN))){
            Recenzija recenzija = mapToEntity(recenzijaDTO);
            recenzija.setEksponat(eksponat);
            recenzija.setUser(user);

            savedRecenzija = recenzijaRepository.save(recenzija);

        }

        return mapToDTO(savedRecenzija);
    }

    @Override
    public List<RecenzijaDTO> getAllRecenzijeByEksponatId(long id) {
        Eksponat eksponat = eksponatRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", id));

        List<Recenzija> recenzije = recenzijaRepository.findByEksponat_Id(eksponat.getId());

        return recenzije.stream().map(recenzija -> mapToDTO(recenzija)).collect(Collectors.toList());
    }

    @Override
    public ApiResponse deleteRecenzija(long eksponatId, long recenzijaId, UserPrincipal currentUser) {
        Eksponat eksponat = eksponatRepository.findById(eksponatId).orElseThrow(() ->
                new ResourceNotFoundException("Eksponat", "id", eksponatId));

        Recenzija recenzija = recenzijaRepository.findById(recenzijaId).orElseThrow(() ->
                new ResourceNotFoundException("Recenzija", "id", recenzijaId));

        ApiResponse apiResponse;

        if(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ||
                recenzija.getUser().getId().equals(currentUser.getId())){
            apiResponse = new ApiResponse(Boolean.TRUE, "Recenzija uspesno obrisana");
            recenzijaRepository.delete(recenzija);
        }
        else if(!recenzija.getEksponat().getId().equals(eksponat.getId())){
            apiResponse = new ApiResponse(Boolean.FALSE, "Recenzija ne pripada datom eksponatu");
            throw new BadRequestException(apiResponse);
        }
        else{
            apiResponse = new ApiResponse(Boolean.FALSE, "Ne mozete obrisati recenzuju koju niste kreirali");
            throw new BadRequestException(apiResponse);
        }

        return apiResponse;
    }

    private Recenzija mapToEntity(RecenzijaDTO recenzijaDTO){
        return modelMapper.map(recenzijaDTO, Recenzija.class);
    }

    private RecenzijaDTO mapToDTO(Recenzija recenzija){
        return modelMapper.map(recenzija, RecenzijaDTO.class);
    }
}
