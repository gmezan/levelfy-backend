package com.uc.backend.service.model;

import com.uc.backend.entity.CourseSuggestion;
import com.uc.backend.repository.CourseSuggestionRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CourseSuggestionService {

    CourseSuggestionRepository courseSuggestionRepository;

    public CourseSuggestionService(CourseSuggestionRepository courseSuggestionRepository) {
        this.courseSuggestionRepository = courseSuggestionRepository;
    }

    public List<CourseSuggestion> getAll() {
        return courseSuggestionRepository.findAll();
    }

    public Optional<CourseSuggestion> getById(Integer id) {
        return courseSuggestionRepository.findById(id);
    }

    public CourseSuggestion save(CourseSuggestion courseSuggestion) {
        return courseSuggestionRepository.save(courseSuggestion);
    }

    public void delete(CourseSuggestion courseSuggestion) {
        courseSuggestionRepository.delete(courseSuggestion);
    }

}
