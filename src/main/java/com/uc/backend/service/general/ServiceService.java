package com.uc.backend.service.general;

import com.uc.backend.entity.Role;
import com.uc.backend.entity.Service;
import com.uc.backend.entity.ServiceAgenda;
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
    ServiceAgendaRepository serviceAgendaRepository;

    @Autowired
    public ServiceService(RoleRepository roleRepository, CourseRepository courseRepository,
                          ServiceRepository serviceRepository, ServiceSessionRepository serviceSessionRepository,
                          UserRepository userRepository, ServiceAgendaRepository serviceAgendaRepository) {
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        this.serviceRepository = serviceRepository;
        this.serviceSessionRepository = serviceSessionRepository;
        this.userRepository = userRepository;
        this.serviceAgendaRepository = serviceAgendaRepository;
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
                                            List<ServiceAgenda> serviceAgendaList = service.getServiceAgendaList();
                                            Service newService = serviceRepository.save(service);
                                            serviceSessionList.forEach(sl->sl.setService(newService));
                                            serviceAgendaList.forEach(sl->sl.setService(newService));
                                            serviceSessionRepository.saveAll(serviceSessionList);
                                            serviceAgendaRepository.saveAll(serviceAgendaList);
                                            return new ResponseEntity<>(newService, HttpStatus.OK);
                                            })
                                            .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                            )
                                .orElseGet(()->new ResponseEntity<>(null, HttpStatus.BAD_REQUEST))
                );
    }

    public ResponseEntity deleteService(int id) {
        return serviceRepository.findById(id)
                .map(value -> {
                    serviceAgendaRepository.deleteAll(value.getServiceAgendaList());
                    serviceSessionRepository.deleteAll(value.getServiceSessionList());
                    serviceRepository.delete(value);
                    return new ResponseEntity(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity(HttpStatus.BAD_REQUEST));
    }

    public ResponseEntity<Service> updateService(Service service) {
        return serviceRepository.findById(service.getIdService())
                .map(value -> {
                    // TODO: Update only updatable attributes
                    service.setCourse(value.getCourse());
                    service.setTeacher(value.getTeacher());
                    service.setServiceType(value.getServiceType());
                    serviceAgendaRepository.saveAll(service.getServiceAgendaList());
                    return new ResponseEntity<>(serviceRepository.save(service), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }
}
