package com.springboot.museum.service.impl;

import com.springboot.museum.entity.Role;
import com.springboot.museum.entity.User;
import com.springboot.museum.exception.BadRequestException;
import com.springboot.museum.payload.ApiResponse;
import com.springboot.museum.payload.SignUpDto;
import com.springboot.museum.payload.UserProfile;
import com.springboot.museum.repository.RoleRepository;
import com.springboot.museum.repository.UserRepository;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createUser(SignUpDto newUser) {
        User user = new User();
        if(userRepository.existsByEmail(newUser.getEmail())){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email adresa je vec zauzeta");
            throw new BadRequestException(apiResponse);
        }

        Optional<Role> role_user = roleRepository.findByName("ROLE_USER");

        user.setRoles(Set.of(role_user.get()));
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setIme(newUser.getIme());
        user.setPrezime(newUser.getPrezime());
        user.setAdresa(newUser.getAdresa());
        user.setTelefon(newUser.getTelefon());

        userRepository.save(user);

        return "Korisnik je uspesno kreiran";
    }

    @Override
    public UserProfile getUserProfile(UserPrincipal currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail());

        return new UserProfile(user.getId(), user.getEmail(), user.getIme(),
                user.getPrezime(), user.getAdresa(), user.getTelefon());
    }

    @Override
    public UserProfile updateUser(SignUpDto newUser, UserPrincipal currentUser) {
        User user = userRepository.findByEmail(currentUser.getEmail());


        if(user.getId().equals(currentUser.getId())){
            if(newUser.getEmail() == null || newUser.getEmail() == ""){
                user.setEmail(user.getEmail());
            }else{
                user.setEmail(newUser.getEmail());
            }
            user.setIme(newUser.getIme());
            user.setPrezime(newUser.getPrezime());
            user.setAdresa(newUser.getAdresa());
            user.setTelefon(newUser.getTelefon());
            if(newUser.getPassword() == null || newUser.getPassword() == ""){
                user.setPassword(user.getPassword());
            }else{
                user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            }
            userRepository.save(user);
        }

        return new UserProfile(user.getId(), user.getEmail(), user.getIme(),
                user.getPrezime(), user.getAdresa(), user.getTelefon());
    }
}
