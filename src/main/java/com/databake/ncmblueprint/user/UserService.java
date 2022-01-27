package com.databake.ncmblueprint.user;

import java.util.List;

public interface UserService {
    User save(UserDto user);
    List<User> findAll();
    User findOne(String username);
}
