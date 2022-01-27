package com.databake.ncmblueprint.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @GetMapping("/role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Role getRoleByRoleLevel(
            @RequestParam(value = "level")  Integer level
    ) {
        return  roleServiceImpl.findRoleByLevel(level);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<Role> getAllRoles() {
        return roleServiceImpl.getAllRoles();
    }

    @PostMapping("/role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Role saveRole(@RequestBody RoleDto roleDto) throws SQLIntegrityConstraintViolationException {
        return roleServiceImpl.saveRole(roleDto);
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Role updateRole(@RequestBody RoleDto roleDto, @PathVariable("id") Integer id) {
        return roleServiceImpl.updateRole(roleDto,id);
    }

}
