package com.uc.backend.service.model;

import com.uc.backend.dto.ServiceTeachDto;
import com.uc.backend.dto.CourseInfoDto;
import com.uc.backend.dto.UserInfoDto;
import com.uc.backend.entity.User;
import com.uc.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository  userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<UserInfoDto, List<CourseInfoDto>> getServiceListByTeach(String s) {
        Map<UserInfoDto, List<CourseInfoDto>> map = new HashMap<>();
        List<ServiceTeachDto> list = this.userRepository.getServiceListByTeach(s);

        list.forEach(serviceTeachDto -> {
            UserInfoDto userInfoDto = new UserInfoDto(serviceTeachDto);
            CourseInfoDto courseInfoDto = new CourseInfoDto(serviceTeachDto);

            if (!map.containsKey(userInfoDto))
                map.put(userInfoDto, new ArrayList<CourseInfoDto>(){{add(courseInfoDto);}});
            else if (!map.get(userInfoDto).contains(courseInfoDto))
                map.get(userInfoDto).add(courseInfoDto);

        });

        return map;
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
