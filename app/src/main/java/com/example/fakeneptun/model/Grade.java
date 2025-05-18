package com.example.fakeneptun.model;

public class Grade {
    private String id;
    private String lesson;
    private String studentId;
    private double gradeValue;

    public Grade() { }

    public Grade(String lesson, String studentId, double gradeValue) {
        this.lesson = lesson;
        this.studentId = studentId;
        this.gradeValue = gradeValue;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLesson() { return lesson; }
    public void setLesson(String lesson) { this.lesson = lesson; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public double getGradeValue() { return gradeValue; }
    public void setGradeValue(double gradeValue) { this.gradeValue = gradeValue; }

}
