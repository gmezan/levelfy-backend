package com.uc.backend.service.general;

import com.uc.backend.entity.Role;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.ServiceSession;
import com.uc.backend.enums.RoleName;
import com.uc.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class ServiceService {

    RoleRepository roleRepository;
    ServiceRepository serviceRepository;
    CourseRepository courseRepository;
    ServiceSessionRepository serviceSessionRepository;
    UserRepository userRepository;

    @Autowired
    public ServiceService(RoleRepository roleRepository, CourseRepository courseRepository,
                          ServiceRepository serviceRepository, ServiceSessionRepository serviceSessionRepository,
                          UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.serviceRepository = serviceRepository;
        this.serviceSessionRepository = serviceSessionRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<Service> createService(Service service) {
        return serviceRepository.findById(service.getIdService())
                .map(value -> new ResponseEntity<>(value, HttpStatus.BAD_REQUEST))
                .orElseGet(() ->
                        courseRepository.findById(service.getCourse().getCourseId())
                            .map(course ->
                                    userRepository.findByIdUserAndRoleContains(service.getTeacher().getIdUser(), roleRepository.findByName(RoleName.teach).orElse(null))
                                        .map(teacher-> {
                                            service.setTeacher(teacher);
                                            service.setCourse(course);
                                            List<ServiceSession> serviceSessionList = service.getServiceSessionList();
                                            Service newService = serviceRepository.save(service);
                                            serviceSessionList.forEach(sl->sl.setService(newService));
                                            serviceSessionRepository.saveAll(serviceSessionList);
                                            return new ResponseEntity<>(newService, HttpStatus.OK);
                                            })
                                            .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                            )
                                .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                );
    }
}
