package com.databake.ncmblueprint.role;

import com.databake.ncmblueprint.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl {

    @Autowired
    private RoleDao roleDao;

    public Role findRoleByLevel(Integer level) {
        Optional<Role> optionalRole = roleDao.findByRoleLevel(level);

        if(optionalRole.isEmpty()) {
            throw new NotFoundException(
                    "role with level " + level + " not found");
        }

        return optionalRole.get();
    }

    public List<Role> getAllRoles() {
        return roleDao.findByIdNot(1);
    }

    public Role saveRole(RoleDto roleDto) throws SQLIntegrityConstraintViolationException {
        Optional<Role> optionalRole = roleDao.findByName(roleDto.getRoleName());
        if(optionalRole.isPresent()) {
            throw new SQLIntegrityConstraintViolationException("duplicate role with name " + roleDto.getRoleName() );
        }

        Role role = new Role();
        role.setRoleLevel(roleDao.findAll().size()+1);
        role.setName(roleDto.getRoleName());
        role.setMachinePermission(roleDto.getMachinePermissions());
        role.setMaterialPermission(roleDto.getMaterialPermissions());
        role.setPartPermission(roleDto.getPartPermissions());
        role.setSummaryPermission(roleDto.getSummaryPermissions());
        role.setDescription(roleDto.getDescription());

        return roleDao.save(role);
    }

    public Role updateRole(RoleDto roleDto, Integer roleId) {
        Optional<Role> optionalRole = roleDao.findById(roleId);

        if(optionalRole.isEmpty()) {
            throw new NotFoundException(
                    "role with level " + roleId + " not found");
        }
        Role role = optionalRole.get();
        role.setName(roleDto.getRoleName());
        role.setMachinePermission(roleDto.getMachinePermissions());
        role.setMaterialPermission(roleDto.getMaterialPermissions());
        role.setPartPermission(roleDto.getPartPermissions());
        role.setSummaryPermission(roleDto.getSummaryPermissions());
        role.setDescription(roleDto.getDescription());

        return roleDao.save(role);
    }

}
