package com.uc.backend.service.model;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.User;
import com.uc.backend.repository.CommentForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ForumService {

    CommentForumRepository commentForumRepository;

    @Autowired
    public ForumService(CommentForumRepository commentForumRepository) {
        this.commentForumRepository = commentForumRepository;
    }

    public List<CommentForum> listAllByService(com.uc.backend.entity.Service service) {
        return commentForumRepository.findByService_IdServiceOrderByDateTimeDesc(service.getIdService());
    }

    public CommentForum create(CommentForum commentForum, com.uc.backend.entity.Service service, User user ) {

        ZoneId zoneId = ZoneId.of("GMT-5");

        commentForum.setUser(user);
        commentForum.setDateTime(LocalDateTime.now(zoneId));
        commentForum.setService(service);
        return commentForumRepository.save(commentForum);
    }

    public void delete(CommentForum commentForum) {
        commentForumRepository.delete(commentForum);
    }

    public Optional<CommentForum> getById(CommentForum commentForum, User user) {
        return commentForumRepository.findCommentForumByIdCommentAndUser_IdUser(
                commentForum.getIdComment(), user.getIdUser());
    }

}
