package com.example.wi;

public class SupplementModel {

    String SupName,SupVitamin,SupIngredient,SupDosage,SupId,SupImage;

    public SupplementModel() {
    }

    public SupplementModel(String supName, String supVitamin, String supIngredient, String supDosage, String supId, String supImage) {
        SupName = supName;
        SupVitamin = supVitamin;
        SupIngredient = supIngredient;
        SupDosage = supDosage;
        SupId = supId;
        SupImage = supImage;
    }

    public String getSupName() {
        return SupName;
    }

    public void setSupName(String supName) {
        SupName = supName;
    }

    public String getSupVitamin() {
        return SupVitamin;
    }

    public void setSupVitamin(String supVitamin) {
        SupVitamin = supVitamin;
    }

    public String getSupIngredient() {
        return SupIngredient;
    }

    public void setSupIngredient(String supIngredient) {
        SupIngredient = supIngredient;
    }

    public String getSupDosage() {
        return SupDosage;
    }

    public void setSupDosage(String supDosage) {
        SupDosage = supDosage;
    }

    public String getSupId() {
        return SupId;
    }

    public void setSupId(String supId) {
        SupId = supId;
    }

    public String getSupImage() {
        return SupImage;
    }

    public void setSupImage(String supImage) {
        SupImage = supImage;
    }
}
