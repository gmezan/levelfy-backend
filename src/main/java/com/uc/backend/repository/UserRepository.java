package com.uc.backend.repository;

import com.uc.backend.entity.Role;
import com.uc.backend.entity.User;
import com.uc.backend.enums.UniversityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    /*
    public User findByCorreo(String correo);

    Optional<User> findByIdusuarioAndRol_IdrolAndActivoIsTrue(int id, int rol);
    List<User> findAllByRol_IdrolAndActivoIsTrue(int rol);

    @Query(value="SELECT COUNT(correo) FROM universityclass.usuario where idinvitante=?1  ",nativeQuery = true)
    String findInvitados(int id);
    @Query(value="SELECT  COUNT(DISTINCT u.correo) FROM clase_enroll c inner join usuario u on  c.idalumno = u.idusuario where u.idinvitante =?1 and c.pago_confirmado=1 group by  u.idinvitante ",nativeQuery = true)
    String findCantidadInvitadosMatriculados(int id);
*/

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findUsersByUniversity(UniversityName university);

}
