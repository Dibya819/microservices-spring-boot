package com.dibya.programming.service;

import com.dibya.programming.dtos.*;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO updateUser(Long id, UpdateRequestDto requestDTO);
    void deleteUser(Long id);
    LoginResponseDTO login(LoginRequestDto requestDto);

}
