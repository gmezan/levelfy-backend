package com.uc.backend.repository;

import com.uc.backend.model.CommentForum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentForumRepository extends JpaRepository<CommentForum, Integer> {

    /*
    public List<CommentForum> findByClase (Service service);
    public List<CommentForum> findByClase_Idclase (int  idClase);

    public List<CommentForum> findComentarioForosByClase_IdclaseAndUsuario_Idusuario  (int idClase, int idUsuario);
*/

}
