package com.uc.backend.service.general;

import com.uc.backend.entity.User;
import com.uc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserRepository  userRepository;

    public Optional<User> getCurrentUser() {
       return userRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication()
               .getPrincipal());
    }


    public Optional<User> getByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


}
