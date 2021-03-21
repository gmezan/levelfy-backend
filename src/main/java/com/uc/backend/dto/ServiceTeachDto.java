package com.uc.backend.dto;

import com.uc.backend.enums.UniversityName;

public interface ServiceTeachDto {
    Integer getUserId();
    String getFullName();
    String getPhoto();
    String getCourseName();
    UniversityName getUniversity();
    String getCourseId();

}
