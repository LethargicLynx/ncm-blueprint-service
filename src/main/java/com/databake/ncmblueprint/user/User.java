package com.databake.ncmblueprint.user;

import com.databake.ncmblueprint.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_username_unique", columnNames = "username")
        })
@Entity(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "user_id")
    private String userId;

    @JsonIgnore
    @Column
    private String lastname;

    @Column
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private String email;

    @ManyToOne
    @JoinColumn(
            name = "role_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_role_fk"
            )
    )
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
