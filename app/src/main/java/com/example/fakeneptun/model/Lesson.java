package com.example.fakeneptun.model;

public class Lesson {
    private String id;
    private String name;
    private int duration;
    private int capacity;
    public Lesson() {}

    public Lesson(String id, String name, int duration, int capacity) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
