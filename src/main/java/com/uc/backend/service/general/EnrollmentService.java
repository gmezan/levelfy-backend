package com.uc.backend.service.general;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.User;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class EnrollmentService {

    EnrollmentRepository enrollmentRepository;
    ServiceRepository serviceRepository;
    UserRepository userRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, ServiceRepository serviceRepository,
                                UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public void deleteEnrollment(Enrollment enrollment) {
        enrollmentRepository.delete(enrollment);
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment getEnrollment(Integer id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    public List<Enrollment> getEnrollments(User user, LevelfyServiceType serviceType) {

        int userId = user.getIdUser();

        return ( serviceType == null )? enrollmentRepository.findEnrollmentsByStudent_IdUser(userId)
                : enrollmentRepository.findEnrollmentsByStudent_IdUser_AndService_ServiceType(userId, serviceType);
    }

}
