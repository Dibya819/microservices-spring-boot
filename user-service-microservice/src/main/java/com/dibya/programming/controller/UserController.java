package com.dibya.programming.controller;

import com.dibya.programming.dtos.UpdateRequestDto;
import com.dibya.programming.dtos.UserRequestDTO;
import com.dibya.programming.dtos.UserResponseDTO;
import com.dibya.programming.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
    this.userService=userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
    UserResponseDTO responseDTO= userService.createUser(userRequestDTO);
     return new  ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO>findUserById(@PathVariable("id") Long id){
        UserResponseDTO responseDTO=userService.getUserById(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/users")
    public  ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO>userResponseDTOS=userService.findAllUsers();
        return new ResponseEntity<>(userResponseDTOS,HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id, @RequestBody @Valid UpdateRequestDto requestDto){
        UserResponseDTO userResponseDTO=userService.updateUser(id, requestDto);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
    }
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
