package com.databake.ncmblueprint.machinepart;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MachinePartId implements Serializable {

    @Column(name = "machine_id")
    private Integer machineId;

    @Column(name = "part_id")
    private Integer partId;

    public MachinePartId(Integer machineId, Integer partId) {
        this.machineId = machineId;
        this.partId = partId;
    }

    public MachinePartId() {
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachinePartId that = (MachinePartId) o;
        return Objects.equals(machineId, that.machineId) && Objects.equals(partId, that.partId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineId, partId);
    }
}
