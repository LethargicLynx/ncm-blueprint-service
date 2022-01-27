package com.databake.ncmblueprint.partmaterial;

import com.databake.ncmblueprint.material.Material;
import com.databake.ncmblueprint.part.Part;

import javax.persistence.*;

@Entity(name = "PartMaterial")
@Table(name = "part_material")
public class PartMaterial {

    @EmbeddedId
    private PartMaterialId partMaterialId;

    @ManyToOne
    @MapsId("partId")
    @JoinColumn(
            name = "part_id",
            foreignKey = @ForeignKey(
                    name = "partmaterial_part_id_fk"
            )
    )
    private Part part;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(
            name = "material_id",
            foreignKey = @ForeignKey(
                    name = "partmaterial_material_id_fk"
            )
    )
    private Material material;

    @Column(name = "quantity")
    private Integer quantity;

    public PartMaterial(PartMaterialId partMaterialId, Part part, Material material, Integer quantity) {
        this.partMaterialId = partMaterialId;
        this.part = part;
        this.material = material;
        this.quantity = quantity;
    }

    public PartMaterial(Part part, Material material, Integer quantity) {
        this.part = part;
        this.material = material;
        this.quantity = quantity;
    }

    public PartMaterial() {
    }

    public PartMaterialId getPartMaterialId() {
        return partMaterialId;
    }

    public void setPartMaterialId(PartMaterialId partMaterialId) {
        this.partMaterialId = partMaterialId;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
