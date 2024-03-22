package com.system.po;

import java.sql.Date;

public class Resource {
    private Integer id;
    /**
     *  1 = 教师
     *  0 = 学生
     */
    private Integer isTeacher;
    private Integer userId;
    private Integer courseId;
    private String photos;
    private Date gmtCreate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Integer isTeacher) {
        this.isTeacher = isTeacher;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", isTeacher=" + isTeacher +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", photos='" + photos + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}
