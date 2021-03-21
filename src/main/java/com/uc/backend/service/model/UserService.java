package com.uc.backend.service.model;

import com.uc.backend.dto.ServiceTeachDto;
import com.uc.backend.entity.User;
import com.uc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository  userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<ServiceTeachDto> getServiceListByTeach() {
        return this.userRepository.getServiceListByTeach();
    }

    public Optional<User> getCurrentUser() {
       return userRepository.findByEmail((String) SecurityContextHolder.getContext().getAuthentication()
               .getPrincipal());
    }


    public Optional<User> getByEmail(String email) {
        return  userRepository.findByEmail(email);
    }

    public User getByEmailOrThrow(String email) throws IllegalArgumentException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return optionalUser.get();
        else throw new IllegalArgumentException("Error retrieving User");
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(int idUser) {
        return userRepository.findById(idUser);
    }
}
