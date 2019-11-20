package com.example.localtasker.models;

import java.io.Serializable;

public class TaskModel implements Serializable {
    public static final String STRING_TASK_ID_REF = "taskId";
    public static final String STRING_TASK_ASSIGNED_TO_REF = "taskAssignedTo";
    public static final String STRING_TASK_STATUS_REF = "taskStatus";
    public static final String STRING_TASK_REVIEW_BY_SELLER_MESSAGE_REF = "taskReviewBySeller";

    private String taskId, taskStatus, taskUploadedBy, taskAssignedTo, taskReviewBySeller, taskCategory, taskCatName, taskUploadedOn, taskTitle, taskDescription, taskLocation, taskLatLng, taskDueDate, taskBudget;


    public TaskModel() {
    }

    public TaskModel(String taskId, String taskStatus, String taskUploadedBy, String taskAssignedTo, String taskReviewBySeller, String taskCategory, String taskCatName, String taskUploadedOn, String taskTitle, String taskDescription, String taskLocation, String taskLatLng, String taskDueDate, String taskBudget) {
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.taskUploadedBy = taskUploadedBy;
        this.taskAssignedTo = taskAssignedTo;
        this.taskReviewBySeller = taskReviewBySeller;
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

    public String getTaskReviewBySeller() {
        return taskReviewBySeller;
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
