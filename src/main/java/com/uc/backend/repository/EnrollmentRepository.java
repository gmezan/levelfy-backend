package com.uc.backend.repository;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // Para los estudiantes:

    /*
    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(Integer iduser, String servicio);

    //findClaseEnrollsByEstudiante_IdusuarioAndActivoIsTrueAndClase_Idclase
    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_IdclaseAndClase_DisponibleIsTrue(int iduser, int idclase);

    List<Enrollment> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrue(int clase_profesor_idusuario, String clase_servicio);

    List<Enrollment> findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(int clase_profesor_idusuario, String clase_servicio, LocalDateTime dt);

    Optional<Enrollment> findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio(int idClaseEnroll, int estudiante_idusuario, String servicio);

    List<Enrollment> findClaseEnrollsByEstudiante_IdusuarioAndClase_ServicioAndClase_DisponibleIsTrueAndActiveIsTrue(int estudiante_idusuario, String clase_servicio);

    Optional<Enrollment> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrue(int clase_idclase, int estudiante_idusuario);
    Optional<Enrollment> findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrueAndPagadoIsTrue(int clase_idclase, int estudiante_idusuario);
    List<Enrollment> findClaseEnrollByClase_Idclase(int idClase);

    Optional<Enrollment> findClaseEnrollByActiveIsTrueAndClase_Profesor_IdusuarioAndIdClaseEnrollAndClase_Idclase(int clase_profesor_idusuario, int idClaseEnroll, int clase_idclase);

    List<Enrollment> findClaseEnrollsByActiveIsTrueAndClase_DisponibleIsFalseAndClase_Servicio(String clase_servicio);

    List<Enrollment> findClaseEnrollsByActiveIsTrueAndClase_Servicio(String clase_servicio);

*/

    List<Enrollment> findEnrollmentsByService_Course_CourseId_University(UniversityName service_course_courseId_university);

    List<Enrollment> findEnrollmentsByService_ServiceType(LevelfyServiceType service_serviceType);

    List<Enrollment> findEnrollmentsByService_ServiceTypeAndService_Course_CourseId_University(LevelfyServiceType service_serviceType, UniversityName service_course_courseId_university);

    Optional<Enrollment> findByIdEnrollmentAndStudent_IdUser(int idEnrollment, int student_idUser);

    Optional<Enrollment> findEnrollmentByService_IdServiceAndStudent_IdUser(int service_idService, int student_idUser);

    // For Enrollment Service
    List<Enrollment> findEnrollmentsByStudent_IdUser(int student_idUser);
    List<Enrollment> findEnrollmentsByStudent_IdUser_AndService_ServiceType(int student_idUser, LevelfyServiceType service_serviceType);


}
