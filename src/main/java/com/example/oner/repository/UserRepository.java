package com.example.oner.repository;

import com.example.oner.entity.User;
import com.example.oner.error.errorcode.ErrorCode;
import com.example.oner.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    default User findByIdOrElseThrow(Long id){
        return findById(id).
                orElseThrow(()->
                        new CustomException(ErrorCode.USER_NOT_FOUND));
    }



}