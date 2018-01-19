package ru.akbarsdigital.restapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.akbarsdigital.restapi.configurations.root.security.model.UserDetailsImpl;
import ru.akbarsdigital.restapi.entity.User;
import ru.akbarsdigital.restapi.service.UserService;
import ru.akbarsdigital.restapi.web.dto.*;

@RestController
@RequestMapping("/rest")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto user){
        return new ResponseEntity<>(new TokenDto(userService.authentication(user)), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationDto user) {
        userService.registrationNewUser(user);
        return new ResponseEntity<>("Sms with code for confirm account sent to your phone ", HttpStatus.OK);
    }

    @PostMapping("/registration/confirm")
    public ResponseEntity<String> confirm(@RequestBody ConfirmDto user) {
        userService.confirmAccount(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> profile(@AuthenticationPrincipal UserDetailsImpl user){
        User result = userService.getUser(user.getEmail());
        return new ResponseEntity<>(ProfileDto.builder()
                .name(result.getName())
                .surname(result.getSurname())
                .patronymic(result.getPatronymic())
                .email(result.getEmail())
                .phone(result.getPhone())
                .avatar(result.getAvatar())
                .build(), HttpStatus.OK);
    }

    @PostMapping("/profile/edit")
    public ResponseEntity<String> editProfile(@RequestBody ProfileDto profile,
                                              @AuthenticationPrincipal UserDetailsImpl user){
        userService.editProfile(profile, user);
        return new ResponseEntity<>("newToken", HttpStatus.OK);
    }
}
