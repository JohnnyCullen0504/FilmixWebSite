package com.hhu.filmix.repository;

import com.hhu.filmix.entity.User;

import java.util.Optional;

public interface UserRepository {
    public Optional<User> findUserById(Long userId);
    public Optional<User> findUserByEmail(String email);
    public Long insertUser(User user);
    public void updateUser(User user);


}
