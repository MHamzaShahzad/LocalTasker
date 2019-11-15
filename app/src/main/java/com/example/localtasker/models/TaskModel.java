package com.example.localtasker.models;

import java.io.Serializable;

public class TaskModel implements Serializable {

    private String taskId, taskStatus, taskUploadedBy, taskAssignedTo, taskReviewOnCompletion, taskCategory, taskCatName, taskUploadedOn, taskTitle, taskDescription, taskLocation, taskLatLng, taskDueDate, taskBudget;


    public TaskModel() {
    }

    public TaskModel(String taskId, String taskStatus, String taskUploadedBy, String taskAssignedTo, String taskReviewOnCompletion, String taskCategory, String taskCatName, String taskUploadedOn, String taskTitle, String taskDescription, String taskLocation, String taskLatLng, String taskDueDate, String taskBudget) {
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.taskUploadedBy = taskUploadedBy;
        this.taskAssignedTo = taskAssignedTo;
        this.taskReviewOnCompletion = taskReviewOnCompletion;
        this.taskCategory = taskCategory;
        this.taskCatName = taskCatName;
        this.taskUploadedOn = taskUploadedOn;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskLocation = taskLocation;
        this.taskLatLng = taskLatLng;
        this.taskDueDate = taskDueDate;
        this.taskBudget = taskBudget;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getTaskUploadedBy() {
        return taskUploadedBy;
    }

    public String getTaskAssignedTo() {
        return taskAssignedTo;
    }

    public String getTaskReviewOnCompletion() {
        return taskReviewOnCompletion;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public String getTaskCatName() {
        return taskCatName;
    }

    public String getTaskUploadedOn() {
        return taskUploadedOn;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskLatLng() {
        return taskLatLng;
    }

    public String getTaskLocation() {
        return taskLocation;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public String getTaskBudget() {
        return taskBudget;
    }
}
