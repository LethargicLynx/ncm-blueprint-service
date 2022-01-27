package com.databake.ncmblueprint.material;

import com.databake.ncmblueprint.partmaterial.PartMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "materials")
@Entity(name = "Material")
public class Material {

    @Id
    @SequenceGenerator(
            name = "material_sequence",
            sequenceName = "material_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy =  SEQUENCE,
            generator = "material_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "express_no",
            columnDefinition = "TEXT"
    )
    private String expressNo;

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
            name = "price"
    )
    private Integer price;

    @Column(
            name = "created",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Timestamp created;

    @Column(
            name = "created",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Timestamp updated;

    @Column(
            name = "image_path",
            columnDefinition = "TEXT"
    )
    private String imagePath;

    @Column(
            name = "unit",
            columnDefinition = "TEXT"
    )
    private String unit;

    @Transient
    private Integer quantity;

    @JsonIgnore
    @OneToMany(
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
            mappedBy = "material",
            fetch = FetchType.LAZY
    )
    private List<PartMaterial> partMaterials = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<PartMaterial> getPartMaterials() {
        return partMaterials;
    }

    public void addPartMaterial(PartMaterial partMaterial) {
        if (!partMaterials.contains(partMaterial)) {
            partMaterials.add(partMaterial);
        }
    }

    public void removePartMaterial(PartMaterial partMaterial) {
        partMaterials.remove(partMaterial);
    }
}
