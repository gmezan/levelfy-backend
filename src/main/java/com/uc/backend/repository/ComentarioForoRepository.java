package com.uc.backend.repository;

import com.uc.backend.entity.Clase;
import com.uc.backend.entity.ComentarioForo;
import com.uc.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioForoRepository extends JpaRepository<ComentarioForo, Integer> {

    public List<ComentarioForo> findByClase (Clase clase);
    public List<ComentarioForo> findByClase_Idclase (int  idClase);

    public List<ComentarioForo> findComentarioForosByClase_IdclaseAndUsuario_Idusuario  (int idClase, int idUsuario);


}
