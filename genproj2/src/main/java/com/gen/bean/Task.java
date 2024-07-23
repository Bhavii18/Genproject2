package com.gen.bean;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Task {
    private int taskId;
    private int userId;
    private String project;
    private Date date;
    private Time startTime;
    private Time endTime;
    private BigDecimal duration;
    private String taskCategory;
    private String description;
    private boolean isCompleted;

    // Getters and Setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getProject() { return project; }
    public void setProject(String project) { this.project = project; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }
    
    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
    
    public BigDecimal getDuration() { return duration; }
    public void setDuration(BigDecimal d) { this.duration = d; }
    
    public String getTaskCategory() { return taskCategory; }
    public void setTaskCategory(String taskCategory) { this.taskCategory = taskCategory; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
