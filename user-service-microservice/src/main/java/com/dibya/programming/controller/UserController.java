package com.dibya.programming.controller;

import com.dibya.programming.dtos.*;
import com.dibya.programming.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
    this.userService=userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO>findUserById(@PathVariable("id") Long id, @RequestHeader("X-User-Id") Long userId,
                                                       @RequestHeader("X-User-Role") String role){
            if ("DRIVER".equals(role) && !userId.equals(id)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view your own details");
            }
        UserResponseDTO responseDTO=userService.getUserById(id);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/FindAllUsers")
    public  ResponseEntity<List<UserResponseDTO>> findAllUsers(@RequestHeader("X-User-Role") String role){
        if (!"ADMIN".equals(role) && !"TRAFFIC_OFFICER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        List<UserResponseDTO>userResponseDTOS=userService.findAllUsers();
        return new ResponseEntity<>(userResponseDTOS,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") Long id,
                                                      @RequestBody @Valid UpdateRequestDto requestDto,
                                                      @RequestHeader("X-User-Id") Long userId,
                                                      @RequestHeader("X-User-Role") String role){
        if("ADMIN".equals(role)){

        }else if("TRAFFIC_OFFICER".equals(role)){
           if(requestDto.getRole() != null &&"ADMIN".equals(requestDto.getRole())){
               throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign ADMIN role");
           } 
        } else if ("DRIVER".equals(role)) {
            if(!userId.equals(id)){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only change your details");
            } if (requestDto.getRole() != null && !"DRIVER".equals(requestDto.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Drivers cannot change their role");
            }
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unknown role");
        }
        UserResponseDTO userResponseDTO=userService.updateUser(id, requestDto);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id,
                                           @RequestHeader("X-User-Role") String role){
        if (!"ADMIN".equals(role)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can delete users");
        }
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
