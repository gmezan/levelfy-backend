package com.uc.backend.service.model;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.Enrollment;
import com.uc.backend.entity.Role;
import com.uc.backend.entity.User;
import com.uc.backend.enums.RoleName;
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

    private final Role ADMIN_ROLE = new Role(4, RoleName.ROLE_ADMIN);
    CommentForumRepository commentForumRepository;

    @Autowired
    public ForumService(CommentForumRepository commentForumRepository) {
        this.commentForumRepository = commentForumRepository;
    }

    public List<CommentForum> listAllByService(com.uc.backend.entity.Service service) {
        return commentForumRepository.findByService_IdServiceOrderByDateTimeAsc(service.getIdService());
    }

    public List<CommentForum> listAllByService_ServiceId(int serviceId) {
        return commentForumRepository.findByService_IdServiceOrderByDateTimeAsc(serviceId);
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

    public Optional<CommentForum> getById(int commentForumId, User user) {

        if (user.getRole().contains(ADMIN_ROLE))
            return commentForumRepository.findById(commentForumId);

        return commentForumRepository.findCommentForumByIdCommentAndUser_IdUser(
                commentForumId, user.getIdUser());
    }

    public CommentForum update(CommentForum oldCommentForum, CommentForum newCommentForum) {
        oldCommentForum.setComment(newCommentForum.getComment());
        return commentForumRepository.save(oldCommentForum);
    }

    public Optional<CommentForum> getById(CommentForum commentForum) {
        return commentForumRepository.findById(
                commentForum.getIdComment());
    }


    public CommentForum save(CommentForum commentForum) {
        return commentForumRepository.save(commentForum);
    }
}
