package com.uc.backend.service.model;

import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.Role;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.User;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.RoleName;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.repository.EnrollmentRepository;
import com.uc.backend.repository.RoleRepository;
import com.uc.backend.repository.ServiceRepository;
import com.uc.backend.repository.UserRepository;
import com.uc.backend.util.CustomConstants;
import com.uc.backend.util.prices.AsesPerPriceDocument;
import com.uc.backend.util.prices.ServicePriceDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class EnrollmentService {

    EnrollmentRepository enrollmentRepository;
    ServiceRepository serviceRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, ServiceRepository serviceRepository,
                                UserRepository userRepository, RoleRepository roleRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void deleteEnrollment(Enrollment enrollment) {
        enrollmentRepository.delete(enrollment);
    }

    public List<Enrollment> getEnrollments(User user, LevelfyServiceType serviceType) {

        int userId = user.getIdUser();

        return ( serviceType == null )? enrollmentRepository.findEnrollmentsByStudent_IdUser(userId)
                : enrollmentRepository.findEnrollmentsByStudent_IdUser_AndService_ServiceType(userId, serviceType);
    }

    public Optional<Enrollment> getEnrollmentById(User user, Integer enrollmentId) {
        return enrollmentRepository.findByIdEnrollmentAndStudent_IdUser(enrollmentId, user.getIdUser());
    }

    public Enrollment createEnrollment(Enrollment enrollment, User user) {

        enrollment.setStudent(user);
        enrollment.setActive(Boolean.TRUE);
        enrollment.setPayed(Boolean.FALSE);

        return serviceRepository.findServiceByIdServiceAndAvailableIsTrue(enrollment.getService().getIdService())
                .map(service ->
                    // Verify if is already enrolled
                    enrollmentRepository.findEnrollmentByService_IdServiceAndStudent_IdUser
                            (
                                    service.getIdService(), user.getIdUser()
                            )
                            .orElseGet(() -> {
                                if (!service.getAvailable())
                                    return null;

                                enrollment.setPrice(service.getPrice());

                                BigDecimal hours =
                                        BigDecimal.valueOf(enrollment.getEnd().getHour() - enrollment.getStart().getHour());

                                if (service.getServiceType().equals(LevelfyServiceType.ASES_PER)) {
                                    CustomConstants.PRICES.get(service.getCourse().getCourseId().getUniversity()).get(LevelfyServiceType.ASES_PER)
                                        .forEach(servicePriceDocument -> {
                                                AsesPerPriceDocument asesPerPriceDocument = (AsesPerPriceDocument)servicePriceDocument;
                                                if (asesPerPriceDocument.clients.equals(enrollment.getNumberOfStudents())) {
                                                    enrollment.setPrice(asesPerPriceDocument.getPrice().multiply(hours));
                                            }
                                    });
                                }

                                // If it is FREE
                                if (service.getPrice().equals(BigDecimal.ZERO))
                                    enrollment.setPayed(Boolean.TRUE);


                                enrollment.setService(service);
                                return enrollmentRepository.save(enrollment);
                            })
                ).orElse(null
        );

    }

    public Enrollment isAlreadyEnrolled(Enrollment enrollment, User user) {
        return enrollmentRepository.
                findEnrollmentByService_CourseAndStudentAndService_ServiceTypeAndService_EvaluationAndService_AvailableIsTrueAndActiveIsTrue(
                        enrollment.getService().getCourse(), user, enrollment.getService().getServiceType(), enrollment.getService().getEvaluation()).orElse(null);
    }


    public Enrollment exists(Enrollment enrollment, User user) {
        return enrollmentRepository.
                findEnrollmentByService_IdServiceAndStudent_IdUser(enrollment.getService().getIdService(),
                        user.getIdUser()).orElse(null);
    }

    public Optional<Enrollment> exists(Integer idService, User user) {
        Role roleClient = roleRepository.findByName(RoleName.ROLE_CLIENT).orElse(null);
        Role roleTeach = roleRepository.findByName(RoleName.ROLE_TEACH).orElse(null);

        if (roleClient==null || roleTeach==null)
            return Optional.empty();

        if (user.getRole().contains(roleClient))
            return enrollmentRepository.
                    findEnrollmentByService_IdServiceAndStudent_IdUser(idService,
                            user.getIdUser());
        else if (user.getRole().contains(roleTeach)) {
            Optional<Service> optionalService =
                    serviceRepository.findServiceByTeacher_IdUserAndIdService(user.getIdUser(), idService);

            if (!optionalService.isPresent()) return Optional.empty();
            Enrollment newEnrollment = new Enrollment();
            newEnrollment.setService(optionalService.get());
            return Optional.of(newEnrollment);
        }
        else return Optional.empty();
    }

    public Optional<Enrollment> existsAndIsActive(int enrollmentId, int userId) {
        return enrollmentRepository.
                findByIdEnrollmentAndStudent_IdUserAndActiveIsTrue(
                        enrollmentId, userId);
    }

    public Optional<Enrollment> exists(int enrollmentId, int userId) {
        return enrollmentRepository.
                findByIdEnrollmentAndStudent_IdUser(
                        enrollmentId, userId);
    }

    public List<Enrollment> getEnrollmentsByService(Service service) {
        return enrollmentRepository.findEnrollmentsByService_IdService(service.getIdService());
    }

    public List<Enrollment> getEnrollmentsByService(int serviceId) {
        return enrollmentRepository.findEnrollmentsByService_IdService(serviceId);
    }
}
