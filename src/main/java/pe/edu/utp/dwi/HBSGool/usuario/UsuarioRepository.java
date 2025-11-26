package pe.edu.utp.dwi.HBSGool.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
	Page<UsuarioEntity> findAll(Pageable pageable);
	Optional<UsuarioEntity> findByEmail(String email);
}
