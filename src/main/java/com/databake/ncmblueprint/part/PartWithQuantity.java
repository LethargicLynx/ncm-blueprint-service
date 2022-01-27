package com.databake.ncmblueprint.part;

import java.sql.Timestamp;
import java.util.List;

public class PartWithQuantity {

    private Integer id;

    private String partNo;

    private String expressNo;

    private String name;

    private String description;

    private String note;

    private Integer cost;

    private String drawingNo;

    private String drawn;

    private Timestamp created;

    private Timestamp updated;

    private String imagePath;

    private String[] drawingImagePath;

    private String partType;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String[] getDrawingImagePath() {
        return drawingImagePath;
    }

    public void setDrawingImagePath(String[] drawingImagePath) {
        this.drawingImagePath = drawingImagePath;
    }
}
