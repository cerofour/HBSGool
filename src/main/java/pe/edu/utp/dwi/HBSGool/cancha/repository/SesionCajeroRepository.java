package pe.edu.utp.dwi.HBSGool.cancha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.dwi.HBSGool.cancha.model.SesionCajeroEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SesionCajeroRepository extends JpaRepository<SesionCajeroEntity, Integer> {
    List<SesionCajeroEntity> findByCajeroId(Integer cajeroId);
    List<SesionCajeroEntity> findByCajeroIdAndFechaAperturaBetween(Integer cajeroId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
