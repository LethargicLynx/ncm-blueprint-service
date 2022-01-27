package com.databake.ncmblueprint.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findByRoleIdNot(Integer roleId);
}
