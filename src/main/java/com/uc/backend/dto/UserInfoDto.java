package com.uc.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDto {
    private Integer userId;
    private String fullName;
    private String photo;
    List<CourseInfoDto> courseInfoDtoList;

    public UserInfoDto(Integer userId, String fullName, String photo) {
        this.userId = userId;
        this.fullName = fullName;
        this.photo = photo;
    }

    public UserInfoDto(ServiceTeachDto serviceTeachDto, CourseInfoDto courseInfoDto) {
        this.userId = serviceTeachDto.getUserId();
        this.fullName = serviceTeachDto.getFullName();
        this.photo = serviceTeachDto.getPhoto();
        this.courseInfoDtoList = new ArrayList<CourseInfoDto>(){{add(courseInfoDto);}};
    }

    public List<CourseInfoDto> getCourseInfoDtoList() {
        return courseInfoDtoList;
    }

    public void setCourseInfoDtoList(List<CourseInfoDto> courseInfoDtoList) {
        this.courseInfoDtoList = courseInfoDtoList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
