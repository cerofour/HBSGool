package pe.edu.utp.dwi.HBSGool.cajero;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CajeroRepository extends JpaRepository<CajeroEntity, Short> {
	Optional<CajeroEntity> findByUsuarioId(Integer userId);
}
