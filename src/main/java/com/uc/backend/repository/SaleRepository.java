package com.uc.backend.repository;

import com.uc.backend.entity.Sale;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    /*
    Optional<Sale> findVentaByClaseEnroll_IdClaseEnroll(int idclase);

    List<Sale> findVentasByClaseEnroll_IdClaseEnroll(int id);

    List<Sale> findVentasByClaseEnroll_Clase(Service claseEnroll_service);

     */
    List<Sale> findSalesByEnrollment_Service_Course_CourseId_University(UniversityName enrollment_service_course_courseId_university);
    List<Sale> findSalesByEnrollment_Service_Course_CourseId_UniversityAndEnrollment_Service_ServiceType(UniversityName enrollment_service_course_courseId_university, LevelfyServiceType enrollment_service_serviceType);
    List<Sale> findSalesByEnrollment_Service_ServiceType(LevelfyServiceType enrollment_service_serviceType);

}
