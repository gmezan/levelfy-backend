package com.uc.backend.repository;

import com.uc.backend.entity.CommentForum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentForumRepository extends JpaRepository<CommentForum, Integer> {

    /*
    public List<CommentForum> findByClase (Service service);
    public List<CommentForum> findByClase_Idclase (int  idClase);

    public List<CommentForum> findComentarioForosByClase_IdclaseAndUsuario_Idusuario  (int idClase, int idUsuario);
*/

    List<CommentForum> findByService_IdServiceAndUser_IdUser(int service_idService, int user_idUser);
}
