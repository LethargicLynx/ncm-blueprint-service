package com.databake.ncmblueprint.partmaterial;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PartMaterialId implements Serializable {

    @Column(name = "part_id")
    private Integer partId;

    @Column(name = "material_id")
    private Integer materialId;

    public PartMaterialId(Integer partId, Integer materialId) {
        this.partId = partId;
        this.materialId = materialId;
    }

    public PartMaterialId() {
    }

    public Integer getPartId() {
        return partId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartMaterialId that = (PartMaterialId) o;
        return Objects.equals(partId, that.partId) && Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partId, materialId);
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }
}
