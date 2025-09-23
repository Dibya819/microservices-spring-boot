package com.dibya.programming.controller;

import com.dibya.programming.dtos.LoginRequestDto;
import com.dibya.programming.dtos.LoginResponseDTO;
import com.dibya.programming.dtos.UserRequestDTO;
import com.dibya.programming.dtos.UserResponseDTO;
import com.dibya.programming.globalexceptionhandling.UserNotFoundByEmailException;
import com.dibya.programming.globalexceptionhandling.UserNotFoundByPhoneNumberException;
import com.dibya.programming.model.User;
import com.dibya.programming.repository.UserRepository;
import com.dibya.programming.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository){
        this.userService=userService;
        this.userRepository = userRepository;
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
    @GetMapping("/id")
    public Long getUserIdByEmailOrPhone(@RequestParam(name = "email",required = false) String email,
                                        @RequestParam(name="phoneNumber",required = false) String phoneNumber) {
        if (email != null) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundByEmailException("Email not found: " + email));
            return user.getId();
        }
        if (phoneNumber != null) {
            User user = userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new UserNotFoundByPhoneNumberException("Phone not found: " + phoneNumber));
            return user.getId();
        }
        throw new IllegalArgumentException("Email or phoneNumber must be provided");
    }
}
