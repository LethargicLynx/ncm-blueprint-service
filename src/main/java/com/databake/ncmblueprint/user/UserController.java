package com.databake.ncmblueprint.user;

import com.databake.ncmblueprint.jwt.AuthToken;
import com.databake.ncmblueprint.jwt.LoginUser;
import com.databake.ncmblueprint.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }


    @GetMapping("/user")
    public User getUserByUsername(
            Authentication authentication
    ) {
        return  userServiceImpl.findOne(authentication.getName());
    }

    @PutMapping("/user")
    public User updateUserInfoByJwt(Authentication authentication,
                                    @RequestBody UserDto userDto) {
        return userServiceImpl.updateUserByJwt(authentication,userDto);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public User updateUserInfoByAdmin(@PathVariable("id") Integer id,
                                      @RequestBody UserDto userDto) {
        return userServiceImpl.updateUserByAdmin(id,userDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userServiceImpl.findAll();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public  User saveUser(@RequestBody UserDto userDto) {
        return userServiceImpl.save(userDto);
    }


}
