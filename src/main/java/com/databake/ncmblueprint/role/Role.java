package com.databake.ncmblueprint.role;

import com.databake.ncmblueprint.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "roles")
@Entity(name = "Role")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "role_level")
    private Integer roleLevel;

    @Type(type = "list-array")
    @Column(name = "machine_permission")
    private List<String> machinePermission;

    @Type(type = "list-array")
    @Column(name = "part_permission")
    private  List<String> partPermission;

    @Type(type = "list-array")
    @Column(name = "material_permission")
    private  List<String> materialPermission;

    @Type(type = "list-array")
    @Column(name = "summary_permission")
    private  List<String> summaryPermission;

    @JsonIgnore
    @OneToMany(
            mappedBy = "role",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private final List<User> users = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public List<String> getMachinePermission() {
        return machinePermission;
    }

    public void setMachinePermission(List<String> machinePermission) {
        this.machinePermission = machinePermission;
    }

    public List<String> getPartPermission() {
        return partPermission;
    }

    public void setPartPermission(List<String> partPermission) {
        this.partPermission = partPermission;
    }

    public List<String> getMaterialPermission() {
        return materialPermission;
    }

    public void setMaterialPermission(List<String> materialPermission) {
        this.materialPermission = materialPermission;
    }

    public List<String> getSummaryPermission() {
        return summaryPermission;
    }

    public void setSummaryPermission(List<String> summaryPermission) {
        this.summaryPermission = summaryPermission;
    }

    public List<User> getUsers() {
        return users;
    }
}
