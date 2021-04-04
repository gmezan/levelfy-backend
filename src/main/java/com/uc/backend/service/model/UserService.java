package com.uc.backend.service.model;

import com.uc.backend.dto.ServiceTeachDto;
import com.uc.backend.dto.CourseInfoDto;
import com.uc.backend.dto.TeacherCoursesInfoDto;
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

    public List<TeacherCoursesInfoDto> getServiceListByTeach(String s) {
        Map<Integer, TeacherCoursesInfoDto> map = new HashMap<>();
        List<ServiceTeachDto> list = this.userRepository.getServiceListByTeach(s);

        list.forEach(serviceTeachDto -> {

            CourseInfoDto courseInfoDto = new CourseInfoDto(serviceTeachDto);

            if (!map.containsKey(serviceTeachDto.getUserId()))
                map.put(serviceTeachDto.getUserId(), new TeacherCoursesInfoDto(serviceTeachDto, courseInfoDto));
            else if (!map.get(serviceTeachDto.getUserId()).getCourseInfoDtoList().contains(courseInfoDto))
                map.get(serviceTeachDto.getUserId()).getCourseInfoDtoList().add(courseInfoDto);

        });

        return new ArrayList<TeacherCoursesInfoDto>(map.values());
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

    public User completeRegistration(User oldUser, User completedUser) {
        oldUser.setName(completedUser.getName());
        oldUser.setUniversity(completedUser.getUniversity());
        oldUser.setBirthday(completedUser.getBirthday());
        oldUser.setGender(completedUser.getGender());
        oldUser.setPhone(completedUser.getPhone());
        return userRepository.save(oldUser);
    }
}
