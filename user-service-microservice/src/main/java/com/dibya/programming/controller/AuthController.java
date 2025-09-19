package com.dibya.programming.controller;

import com.dibya.programming.dtos.LoginRequestDto;
import com.dibya.programming.dtos.LoginResponseDTO;
import com.dibya.programming.dtos.UserRequestDTO;
import com.dibya.programming.dtos.UserResponseDTO;
import com.dibya.programming.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserResponseDTO responseDTO= userService.createUser(userRequestDTO);
        return new  ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDto loginRequestDTO) {
        LoginResponseDTO response = userService.login(loginRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
