package com.example.wi;

public class ExerciseModel {

    String ExName,ExRepetition,ExDuration,ExSteps,ExId,ExImage;

    public ExerciseModel() {
    }

    public ExerciseModel(String exName, String exRepetition, String exDuration, String exSteps, String exId, String exImage) {
        ExName = exName;
        ExRepetition = exRepetition;
        ExDuration = exDuration;
        ExSteps = exSteps;
        ExId = exId;
        ExImage = exImage;
    }

    public String getExName() {
        return ExName;
    }

    public void setExName(String exName) {
        ExName = exName;
    }

    public String getExRepetition() {
        return ExRepetition;
    }

    public void setExRepetition(String exRepetition) {
        ExRepetition = exRepetition;
    }

    public String getExDuration() {
        return ExDuration;
    }

    public void setExDuration(String exDuration) {
        ExDuration = exDuration;
    }

    public String getExSteps() {
        return ExSteps;
    }

    public void setExSteps(String exSteps) {
        ExSteps = exSteps;
    }

    public String getExId() {
        return ExId;
    }

    public void setExId(String exId) {
        ExId = exId;
    }

    public String getExImage() {
        return ExImage;
    }

    public void setExImage(String exImage) {
        ExImage = exImage;
    }
}
