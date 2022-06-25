package com.springboot.museum.controller;

import com.springboot.museum.entity.User;
import com.springboot.museum.payload.SignUpDto;
import com.springboot.museum.payload.UpdateUserPayload;
import com.springboot.museum.payload.UserProfile;
import com.springboot.museum.security.UserPrincipal;
import com.springboot.museum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/myProfile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserProfile> getUserProfile(@AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(userService.getUserProfile(currentUser), HttpStatus.OK);
    }

//    @GetMapping("/myProfile/{email}")
//    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("email") String email){
//        return new ResponseEntity<>(userService.getUserProfile(email), HttpStatus.OK);
//    }

    @PutMapping("/myProfile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserProfile> updateUser(@RequestBody SignUpDto user,
                                           @AuthenticationPrincipal UserPrincipal currentUser){
        return new ResponseEntity<>(userService.updateUser(user, currentUser), HttpStatus.OK);
    }
}
