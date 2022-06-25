package com.springboot.museum.service;

import com.springboot.museum.entity.User;
import com.springboot.museum.payload.SignUpDto;
import com.springboot.museum.payload.UpdateUserPayload;
import com.springboot.museum.payload.UserProfile;
import com.springboot.museum.security.UserPrincipal;

public interface UserService {
    String createUser(SignUpDto user);
    UserProfile getUserProfile(UserPrincipal currentUser);
    //UserProfile getUserProfile(String email);
    UserProfile updateUser(SignUpDto newUser, UserPrincipal currentUser);
}
