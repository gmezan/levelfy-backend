package com.uc.backend.repository;

import com.uc.backend.entity.Rol;
import com.uc.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByCorreo(String correo);

    Optional<Usuario> findByIdusuarioAndRol_IdrolAndActivoIsTrue(int id, int rol);
List<Usuario> findAllByRol_IdrolAndActivoIsTrue(int rol);

    @Query(value="SELECT COUNT(correo) FROM universityclass.usuario where idinvitante=?1  ",nativeQuery = true)
    String findInvitados(int id);
    @Query(value="SELECT  COUNT(DISTINCT u.correo) FROM clase_enroll c inner join usuario u on  c.idalumno = u.idusuario where u.idinvitante =?1 and c.pago_confirmado=1 group by  u.idinvitante ",nativeQuery = true)
    String findCantidadInvitadosMatriculados(int id);

}
