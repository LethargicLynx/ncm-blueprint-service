package com.databake.ncmblueprint.type;

import javax.persistence.*;

import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "machine_types")
@Entity(name = "MachineType")
public class MachineType {

    @Id
    @SequenceGenerator(
            name = "machine_type_sequence",
            sequenceName = "machine_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "machine_type_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "name",
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "created",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Timestamp created;

    @Column(
            name = "updated",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Timestamp updated;

    @Transient
    private Integer machineCount;

    public MachineType(String name, String description, Timestamp created, Timestamp updated) {
        this.name = name;
        this.description = description;
        this.created = created;
        this.updated = updated;
    }

    public MachineType() {
    }

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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public Integer getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(Integer machineCount) {
        this.machineCount = machineCount;
    }
}
