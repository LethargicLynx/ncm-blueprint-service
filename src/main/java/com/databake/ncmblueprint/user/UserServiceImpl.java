package com.databake.ncmblueprint.user;

import com.databake.ncmblueprint.exception.NotFoundException;
import com.databake.ncmblueprint.role.Role;
import com.databake.ncmblueprint.role.RoleDao;
import com.databake.ncmblueprint.security.UserPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService,UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    public List<User> findAll() {
        return userDao.findByRoleIdNot(1);
    }

    @Override
    public User findOne(String username) {
        Optional<User> optionalUser = userDao.findByUsername(username);
        if(optionalUser.isEmpty()) {
            throw new NotFoundException(
                    "User with username " + username + " not found");
        }

        return  optionalUser.get();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDao.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return new org.springframework.security.core.userdetails.User(optionalUser.get().getUsername(),
                optionalUser.get().getPassword(), getAuthority(optionalUser.get()));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Optional<Role> optionalRole = roleDao.findByRoleLevel(user.getRole().getRoleLevel());
        if(optionalRole.isEmpty()) {
            throw new NotFoundException(
                    "Role with role id " + user.getRole() + " not found");
        }
        Role role = optionalRole.get();
        Set<UserPermission> permissions = new HashSet<>();

        for(int i = 0;i < role.getMachinePermission().size();i++) {
            if(i == 0 && role.getMachinePermission().get(i).equals("true")) {
                permissions.add(UserPermission.MACHINE_VIEW);
            }

            if(i == 1 && role.getMachinePermission().get(i).equals("true")) {
                permissions.add(UserPermission.MACHINE_CREATE);
            }

            if(i == 2 && role.getMachinePermission().get(i).equals("true")) {
                permissions.add(UserPermission.MACHINE_UPDATE);
            }

            if(i == 3 && role.getMachinePermission().get(i).equals("true")) {
                permissions.add(UserPermission.MACHINE_DELETE);
            }

            if(i == 4 && role.getMachinePermission().get(i).equals("true")) {
                permissions.add(UserPermission.MACHINE_DOWNLOAD);
            }
        }

        for(int i = 0;i < role.getPartPermission().size();i++) {
            if(i == 0 && role.getPartPermission().get(i).equals("true")) {
                permissions.add(UserPermission.PART_VIEW);
            }

            if(i == 1 && role.getPartPermission().get(i).equals("true")) {
                permissions.add(UserPermission.PART_CREATE);
            }

            if(i == 2 && role.getPartPermission().get(i).equals("true")) {
                permissions.add(UserPermission.PART_UPDATE);
            }

            if(i == 3 && role.getPartPermission().get(i).equals("true")) {
                permissions.add(UserPermission.PART_DELETE);
            }

            if(i == 4 && role.getPartPermission().get(i).equals("true")) {
                permissions.add(UserPermission.PART_DOWNLOAD);
            }
        }

        for(int i = 0;i < role.getMaterialPermission().size();i++) {
            if(i == 0 && role.getMaterialPermission().get(i).equals("true")) {
                permissions.add(UserPermission.MATERIAL_VIEW);
            }

            if(i == 1 && role.getMaterialPermission().get(i).equals("true")) {
                permissions.add(UserPermission.MATERIAL_CREATE);
            }

            if(i == 2 && role.getMaterialPermission().get(i).equals("true")) {
                permissions.add(UserPermission.MATERIAL_UPDATE);
            }

            if(i == 3 && role.getMaterialPermission().get(i).equals("true")) {
                permissions.add(UserPermission.MATERIAL_DELETE);
            }

            if(i == 4 && role.getMaterialPermission().get(i).equals("true")) {
                permissions.add(UserPermission.MATERIAL_DOWNLOAD);
            }
        }

        for(int i = 0;i < role.getSummaryPermission().size();i++) {
            if(i == 0 && role.getSummaryPermission().get(i).equals("true")) {
                permissions.add(UserPermission.SUMMARY_VIEW);
            }

            if(i == 1 && role.getSummaryPermission().get(i).equals("true")) {
                permissions.add(UserPermission.SUMMARY_CREATE);
            }


            if(i == 4 && role.getSummaryPermission().get(i).equals("true")) {
                permissions.add(UserPermission.SUMMARY_DOWNLOAD);
            }
        }

        Set<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    @Override
    public User save(UserDto userDto) {
        User newUser = new User();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        newUser.setUsername(userDto.getUsername());
        newUser.setUserId(userDto.getEmployeeId());

        Optional<Role> optionalRole = roleDao.findByRoleLevel(userDto.getRoleId());
        if(optionalRole.isEmpty()) {
            throw new NotFoundException(
                    "Role with role id " + userDto.getRoleId() + " not found");
        }
        newUser.setRole(optionalRole.get());

        return userDao.save(newUser);
    }

    public User updateUserByJwt(Authentication authentication, UserDto userDto) {
        Optional<User> optionalUser = userDao.findByUsername(authentication.getName());
        if(optionalUser.isEmpty()) {
            throw new NotFoundException(
                    "User with username " + authentication.getName() + " not found");
        }

        User user = optionalUser.get();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setUserId(userDto.getEmployeeId());
        if(StringUtils.isNoneEmpty(userDto.getPassword())) {
            user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        }
        user.setEmail(userDto.getEmail());

        return userDao.save(user);
    }

    public User updateUserByAdmin(Integer id, UserDto userDto) {
        Optional<User> optionalUser = userDao.findById(id);
        if(optionalUser.isEmpty()) {
            throw new NotFoundException(
                    "User with id " + id + " not found");
        }

        Optional<Role> optionalRole = roleDao.findById(userDto.getRoleId());
        if(optionalRole.isEmpty()) {
            throw new NotFoundException(
                    "Role with role id " + userDto.getRoleId() + " not found");
        }

        User user = optionalUser.get();
        user.setUsername(userDto.getUsername());
        user.setRole(optionalRole.get());
        user.setName(userDto.getName());
        user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setUserId(userDto.getEmployeeId());

        return userDao.save(user);
    }

}
