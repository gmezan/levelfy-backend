package com.uc.backend.service.model;

import com.uc.backend.entity.CommentForum;
import com.uc.backend.entity.User;
import com.uc.backend.repository.CommentForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<CommentForum> listAll(com.uc.backend.entity.Service service,
                                      User user) {
        return commentForumRepository.findByService_IdServiceAndUser_IdUser(service.getIdService(),
                user.getIdUser());
    }

    public CommentForum create(CommentForum commentForum) {
        return commentForumRepository.save(commentForum);
    }

    public void delete(CommentForum commentForum) {
        commentForumRepository.delete(commentForum);
    }

    public Optional<CommentForum> getById(Integer id, User user) {
        return commentForumRepository.findCommentForumByIdCommentAndUser_IdUser(id, user.getIdUser());
    }

}
