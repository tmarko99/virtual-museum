package com.springboot.museum.controller;

import com.springboot.museum.payload.JwtAuthResponse;
import com.springboot.museum.payload.LoginDto;
import com.springboot.museum.payload.SignUpDto;
import com.springboot.museum.repository.RoleRepository;
import com.springboot.museum.repository.UserRepository;
import com.springboot.museum.security.JwtTokenProvider;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserPrincipal userLogged = (UserPrincipal) authenticate.getPrincipal();

        String token = jwtTokenProvider.generateToken(authenticate);

        return ResponseEntity.ok(new JwtAuthResponse(token, userLogged.getEmail(), userLogged.getAuthorities()));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> kreirajKorisnika(@Valid @RequestBody SignUpDto signUpDto){
        return new ResponseEntity<>(userService.createUser(signUpDto), HttpStatus.CREATED);
    }

}
