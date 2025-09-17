package com.dibya.programming.service;

import com.dibya.programming.dtos.UpdateRequestDto;
import com.dibya.programming.dtos.UserRequestDTO;
import com.dibya.programming.dtos.UserResponseDTO;
import com.dibya.programming.enums.Role;
import com.dibya.programming.globalexceptionhandling.DuplicateUserException;
import com.dibya.programming.globalexceptionhandling.EmailAlreadyExistException;
import com.dibya.programming.globalexceptionhandling.UserNotFoundException;
import com.dibya.programming.model.User;
import com.dibya.programming.repository.UserRepository;
import com.dibya.programming.utils.DtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService{

      private final UserRepository userRepository;
      private final BCryptPasswordEncoder passwordEncoder;

      @Autowired
     public UserServiceImplementation(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder){
         this.userRepository=userRepository;
         this.passwordEncoder=passwordEncoder;
     }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
          List<String> errors=new ArrayList<>();
          if(userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()){
              errors.add("Email already Exists: "+userRequestDTO.getEmail());
          }
        if(userRepository.findByPhoneNumber(userRequestDTO.getPhoneNumber()).isPresent()){
            errors.add("Phone number already Exists: "+userRequestDTO.getPhoneNumber());
        }
        if(!errors.isEmpty()){
            throw new DuplicateUserException(errors);
        }
        User user=mapToEntity(userRequestDTO);
        User savedUser=userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
        return mapToDTO(user);
    }

    @Override
    public List<UserResponseDTO>findAllUsers(){
          List<User> user=userRepository.findAll();
          return user.stream().map(this::mapToDTO).toList();
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateRequestDto requestDTO){
        DtoUtils.trimStrings(requestDTO);
          User existingUser=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
          List<String> errors=new ArrayList<>();
          if(requestDTO.getEmail()!=null && !requestDTO.getEmail().equals(existingUser.getEmail())
          && userRepository.findByEmail(requestDTO.getEmail()).isPresent()){
              errors.add("Email Already Exists: "+requestDTO.getEmail());
          }
          if(requestDTO.getPhoneNumber()!=null && !requestDTO.getPhoneNumber().equals(existingUser.getPhoneNumber())
                && userRepository.findByPhoneNumber(requestDTO.getPhoneNumber()).isPresent()){
            errors.add("Phone Number Already Exists: "+requestDTO.getPhoneNumber());
          }
          if(!errors.isEmpty()){
              throw new DuplicateUserException(errors);
          }
          mergeUserData(existingUser,requestDTO);
          User savedUser=userRepository.save(existingUser);
          return mapToDTO(savedUser);

    }

    @Override
    public void deleteUser(Long id) {
          User user=userRepository.findById(id).orElseThrow(()->new UserNotFoundException(id));
          userRepository.delete(user);
    }


    private void mergeUserData(User existingUser, UpdateRequestDto dto) {
        if(dto.getName() != null) existingUser.setName(dto.getName());
        if(dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if(dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());
        if(dto.getAddress() != null) existingUser.setAddress(dto.getAddress());
        if(dto.getState() != null) existingUser.setState(dto.getState());
        if(dto.getPincode() != null) existingUser.setPincode(dto.getPincode());
        if(dto.getRole() != null) existingUser.setRole(Role.valueOf(dto.getRole()));
        if(dto.getPassword() != null) existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    private User mapToEntity(UserRequestDTO userRequestDTO){
        Role role=Role.valueOf(userRequestDTO.getRole());
          return User.builder()
                  .name(userRequestDTO.getName())
                  .email(userRequestDTO.getEmail())
                  .role(role)
                  .state(userRequestDTO.getState())
                  .address(userRequestDTO.getAddress())
                  .phoneNumber(userRequestDTO.getPhoneNumber())
                  .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                  .pincode(userRequestDTO.getPincode())
                  .build();
    }
    private UserResponseDTO mapToDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .state(user.getState())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .pincode(user.getPincode())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
