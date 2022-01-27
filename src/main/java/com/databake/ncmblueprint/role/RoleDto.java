package com.databake.ncmblueprint.role;

import javax.validation.constraints.NotNull;
import java.util.List;

public class RoleDto {
    @NotNull
    private String roleName;

    private String description;

    @NotNull
    private List<String> machinePermissions;

    @NotNull
    private List<String> partPermissions;

    @NotNull
    private List<String> materialPermissions;

    @NotNull
    private List<String> summaryPermissions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMachinePermissions() {
        return machinePermissions;
    }

    public void setMachinePermissions(List<String> machinePermissions) {
        this.machinePermissions = machinePermissions;
    }

    public List<String> getPartPermissions() {
        return partPermissions;
    }

    public void setPartPermissions(List<String> partPermissions) {
        this.partPermissions = partPermissions;
    }

    public List<String> getMaterialPermissions() {
        return materialPermissions;
    }

    public void setMaterialPermissions(List<String> materialPermissions) {
        this.materialPermissions = materialPermissions;
    }

    public List<String> getSummaryPermissions() {
        return summaryPermissions;
    }

    public void setSummaryPermissions(List<String> summaryPermissions) {
        this.summaryPermissions = summaryPermissions;
    }
}
