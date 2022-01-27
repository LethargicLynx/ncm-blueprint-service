package com.databake.ncmblueprint.partrelation;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "part_relation")
@Entity(name = "PartRelation")
public class PartRelation {

    @Id
    @SequenceGenerator(
            name = "part_relation_sequence",
            sequenceName = "part_relation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "part_relation_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(name = "assembly_part")
    private Integer assemblyPartId;

    @Column(name = "sub_part")
    private Integer subPartId;

    @Column(name = "part")
    private Integer partId;

    @Column(name = "quantity")
    private Integer quantity;

    public PartRelation(Integer assemblyPartId, Integer subPartId, Integer partId, Integer quantity) {
        this.assemblyPartId = assemblyPartId;
        this.subPartId = subPartId;
        this.partId = partId;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssemblyPartId() {
        return assemblyPartId;
    }

    public void setAssemblyPartId(Integer assemblyPartId) {
        this.assemblyPartId = assemblyPartId;
    }

    public Integer getSubPartId() {
        return subPartId;
    }

    public void setSubPartId(Integer subPartId) {
        this.subPartId = subPartId;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
