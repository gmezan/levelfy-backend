package com.uc.backend.service.model;

import com.uc.backend.entity.Role;
import com.uc.backend.enums.RoleName;
import com.uc.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Component
@Transactional
public class RoleService {

    RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getByName(RoleName name) {
        return  roleRepository.findByName(name);
    }

    public boolean existsByName(RoleName name) {
        return roleRepository.existsByName(name);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

}
