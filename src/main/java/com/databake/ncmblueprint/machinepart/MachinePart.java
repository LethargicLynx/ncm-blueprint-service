package com.databake.ncmblueprint.machinepart;

import com.databake.ncmblueprint.machine.Machine;
import com.databake.ncmblueprint.part.Part;

import javax.persistence.*;

@Entity(name = "MachinePart")
@Table(name = "machine_part")
public class MachinePart {

    @EmbeddedId
    private MachinePartId machinePartId;

    @ManyToOne
    @MapsId("machineId")
    @JoinColumn(
            name = "machine_id",
            foreignKey = @ForeignKey(
                    name = "machinepart_machine_id_fk"
            )
    )
    private Machine machine;

    @ManyToOne
    @MapsId("partId")
    @JoinColumn(
            name = "part_id",
            foreignKey = @ForeignKey(
                    name = "machinepart_part_id_fk"
            )
    )
    private Part part;

    @Column(name = "quantity")
    private Integer quantity;

    public MachinePart(MachinePartId machinePartId, Machine machine, Part part, Integer quantity) {
        this.machinePartId = machinePartId;
        this.machine = machine;
        this.part = part;
        this.quantity = quantity;
    }

    public MachinePart() {
    }

    public MachinePartId getMachinePartId() {
        return machinePartId;
    }

    public void setMachinePartId(MachinePartId machinePartId) {
        this.machinePartId = machinePartId;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
