package com.databake.ncmblueprint.part;

import com.databake.ncmblueprint.machinepart.MachinePart;
import com.databake.ncmblueprint.partmaterial.PartMaterial;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "parts")
@Entity(name = "Part")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Part {

    @Id
    @SequenceGenerator(
            name = "part_sequence",
            sequenceName = "part_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "part_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "part_no",
            columnDefinition = "TEXT"
    )
    private String partNo;

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
            name = "note",
            columnDefinition = "TEXT"
    )
    private String note;

    @Column(
            name = "cost"
    )
    private Integer cost;

    @Column(
            name = "drawing_no",
            columnDefinition = "TEXT"
    )
    private String drawingNo;

    @Column(
            name = "drawn",
            columnDefinition = "TEXT"
    )
    private String drawn;

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

    @Column(
            name = "image_path",
            columnDefinition = "TEXT"
    )
    private String imagePath;

    @Type(type = "list-array")
    @Column(
            name = "drawing_image_path"
    )
    private List<String> drawingImagePath;

    @JsonIgnore
    @Column(
            name = "folder_path",
            columnDefinition = "TEXT")
    private String folderPath;

    @Column(
            name = "label_image_path",
            columnDefinition = "TEXT")
    private  String labelImagePath;

    @Column(
            name = "part_type",
            columnDefinition = "TEXT"
    )
    private String partType;

    @JsonIgnore
    @OneToMany(
            cascade = {CascadeType.PERSIST,CascadeType.REMOVE},
            mappedBy = "part",
            fetch = FetchType.LAZY,
            targetEntity = PartMaterial.class
    )
    @NotFound(action = NotFoundAction.IGNORE)
    private List<PartMaterial> partMaterials = new ArrayList<>();

    @JsonIgnore
    @OneToMany(
            cascade = {CascadeType.PERSIST},
            mappedBy = "part",
            fetch = FetchType.LAZY
    )
    private List<MachinePart> machineParts = new ArrayList<>();

    @Transient
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getDrawn() {
        return drawn;
    }

    public void setDrawn(String drawn) {
        this.drawn = drawn;
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

    public List<String> getDrawingImagePath() {
        return drawingImagePath;
    }

    public void setDrawingImagePath(List<String> drawingImagePath) {
        this.drawingImagePath = drawingImagePath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getLabelImagePath() {
        return labelImagePath;
    }

    public void setLabelImagePath(String labelImagePath) {
        this.labelImagePath = labelImagePath;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<MachinePart> getMachineParts() {
        return machineParts;
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
