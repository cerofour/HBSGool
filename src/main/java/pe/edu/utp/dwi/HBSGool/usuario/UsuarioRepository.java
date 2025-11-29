package pe.edu.utp.dwi.HBSGool.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
	Page<UsuarioEntity> findAll(Pageable pageable);
	Optional<UsuarioEntity> findByEmail(String email);
    Page<UsuarioEntity> findByName(String name, Pageable pageable);
    Page<UsuarioEntity> findByDni(String dni, Pageable pageable);
    Page<UsuarioEntity> findByActive(Boolean active, Pageable pageable);
}
