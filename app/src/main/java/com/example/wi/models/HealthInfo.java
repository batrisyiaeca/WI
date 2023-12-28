package com.example.wi.models;

import java.util.Date;

public class HealthInfo {
    private String healthInfoId;
    private String userId; // Add this field to associate the health info with a user
    private String weight;
    private String height;
    private String bloodPressure;
    private String heartRate;
    private Date date;
    private String healthInfoImage; // This can be a URL or any representation of the image

    // Constructor with parameters
    public HealthInfo(String healthInfoId, String userId, String weight, String height,
                      String bloodPressure, String heartRate, String additionalInfo, Date date,
                      String healthInfoImage) {
        this.healthInfoId = healthInfoId;
        this.userId = userId;
        this.weight = weight;
        this.height = height;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        // Add any additional health info fields you need
        this.date = date;
        this.healthInfoImage = healthInfoImage;
    }

    // Add getters and setters for all fields

    public String getHealthInfoId() {
        return healthInfoId;
    }

    public void setHealthInfoId(String healthInfoId) {
        this.healthInfoId = healthInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    // Add other getters and setters

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHealthInfoImage() {
        return healthInfoImage;
    }

    public void setHealthInfoImage(String healthInfoImage) {
        this.healthInfoImage = healthInfoImage;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }
}
