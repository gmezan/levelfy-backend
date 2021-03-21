package com.uc.backend.dto;

public class UserInfoDto {
    private Integer userId;
    private String fullName;
    private String photo;

    public UserInfoDto(Integer userId, String fullName, String photo) {
        this.userId = userId;
        this.fullName = fullName;
        this.photo = photo;
    }

    public UserInfoDto(ServiceTeachDto serviceTeachDto) {
        this.userId = serviceTeachDto.getUserId();
        this.fullName = serviceTeachDto.getFullName();
        this.photo = serviceTeachDto.getPhoto();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
