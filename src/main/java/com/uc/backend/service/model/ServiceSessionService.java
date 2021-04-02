package com.uc.backend.service.model;

import com.uc.backend.repository.ServiceSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class ServiceSessionService {

    ServiceSessionRepository serviceSessionRepository;

    @Autowired
    public ServiceSessionService(ServiceSessionRepository serviceSessionRepository) {
        this.serviceSessionRepository = serviceSessionRepository;
    }


}
