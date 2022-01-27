package com.databake.ncmblueprint.machine;

import com.databake.ncmblueprint.machinepart.MachinePart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Table(name = "machines")
@Entity(name = "Machine")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Machine {

    @Id
    @SequenceGenerator(
            name = "machine_sequence",
            sequenceName = "machine_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "machine_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "machine_no",
            columnDefinition = "TEXT"
    )
    private String machineNo;

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
            name = "drawing_no"
    )
    private String drawingNo;

    @Column(
            name = "drawn"
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
            name = "tags_id"
    )
    private String tagIds;

    @Column(
            name = "image_path",
            columnDefinition = "TEXT"
    )
    private String imagePath;

    @Column(
            name = "machine_type_id"
    )
    private Integer type;

    @Type(type = "list-array")
    @Column(
            name = "drawing_image_path"
    )
    private List<String> drawingImagePath;

    @Column(
            name = "label_image_path",
            columnDefinition = "TEXT"
    )
    private String labelImagePath;

    @Column(
            name = "folder_path",
            columnDefinition = "TEXT"
    )
    private String folderPath;

    @Transient
    private Integer materialCount;

    @Transient
    private Integer cost;

    @JsonIgnore
    @OneToMany(
            cascade = {CascadeType.PERSIST},
            mappedBy = "machine",
            fetch = FetchType.LAZY
    )
    private List<MachinePart> machineParts = new ArrayList<>();

    public Machine() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
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

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getDrawingImagePath() {
        return drawingImagePath;
    }

    public void setDrawingImagePath(List<String> drawingImagePath) {
        this.drawingImagePath = drawingImagePath;
    }

    public String getLabelImagePath() {
        return labelImagePath;
    }

    public void setLabelImagePath(String labelImagePath) {
        this.labelImagePath = labelImagePath;
    }

    public Integer getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @JsonIgnoreProperties
    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @JsonIgnoreProperties
    public List<MachinePart> getMachineParts() {
        return machineParts;
    }

    public void addMachinePart(MachinePart machinePart) {
        if (!machineParts.contains(machinePart)) {
            machineParts.add(machinePart);
        }
    }

    public void removeMachinePart(MachinePart machinePart) {
        machineParts.remove(machinePart);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", machineNo='" + machineNo + '\'' +
                ", expressNo='" + expressNo + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + price +
                ", drawingNo='" + drawingNo + '\'' +
                ", drawn='" + drawn + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", tagIds='" + tagIds + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}