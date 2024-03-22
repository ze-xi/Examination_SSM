package com.system.po;

public class ResourceCustom extends Resource {
    private String username;
    private String courseName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return super.toString()+"ResourceCustom{" +
                "username='" + username + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
