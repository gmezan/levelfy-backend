package com.uc.backend.repository;

import com.uc.backend.entity.SaleCanceled;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleCanceledRepository extends JpaRepository<SaleCanceled, Integer> {

    List<SaleCanceled> findSaleCanceledsByCourse_CourseId_University(UniversityName course_courseId_university);

    List<SaleCanceled> findSaleCanceledsByServiceType(LevelfyServiceType serviceType);

    List<SaleCanceled> findSaleCanceledsByCourse_CourseId_UniversityAndServiceType(UniversityName course_courseId_university, LevelfyServiceType serviceType);
}
