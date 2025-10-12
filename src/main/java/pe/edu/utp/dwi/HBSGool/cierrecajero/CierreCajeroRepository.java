package pe.edu.utp.dwi.HBSGool.cierrecajero;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CierreCajeroRepository extends JpaRepository<CierreCajeroEntity, Integer> {

    Optional<CierreCajeroEntity> findBySesionCajeroId(int sesionCajeroId);

}
