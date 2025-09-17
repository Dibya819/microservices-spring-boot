package com.dibya.programming.service;

import com.dibya.programming.dtos.UpdateRequestDto;
import com.dibya.programming.dtos.UserRequestDTO;
import com.dibya.programming.dtos.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO updateUser(Long id, UpdateRequestDto requestDTO);
    void deleteUser(Long id);

}
