package com.dibya.programming.repository;

import com.dibya.programming.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findUserByUserId(Long id);


    Optional<UserInfo> findByEmailOrPhoneNumber(String email, String phoneNumber);

    UserInfo findByEmail(String email);

    UserInfo findByPhoneNumber(String phoneNumber);
}
