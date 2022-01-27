package com.databake.ncmblueprint.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    Optional<Role> findByRoleLevel(Integer level);

    List<Role> findByIdNot(Integer id);
}
