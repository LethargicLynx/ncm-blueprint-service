package com.databake.ncmblueprint.part;

public class UpdatedPartDTO {
    private String partName;

    private String dwgNo;

    private Integer cost;

    private String description;

    private String note;

    private String type;

    private String partIdQuantityList;

    private String materialIdQuantityList;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDwgNo() {
        return dwgNo;
    }

    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartIdQuantityList() {
        return partIdQuantityList;
    }

    public void setPartIdQuantityList(String partIdQuantityList) {
        this.partIdQuantityList = partIdQuantityList;
    }

    public String getMaterialIdQuantityList() {
        return materialIdQuantityList;
    }

    public void setMaterialIdQuantityList(String materialIdQuantityList) {
        this.materialIdQuantityList = materialIdQuantityList;
    }
}
