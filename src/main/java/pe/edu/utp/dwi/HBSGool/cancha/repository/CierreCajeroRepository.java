package pe.edu.utp.dwi.HBSGool.cancha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.dwi.HBSGool.cancha.model.CierreCajeroEntity;

@Repository
public interface CierreCajeroRepository extends JpaRepository<CierreCajeroEntity, Integer> {
    CierreCajeroEntity findBySesionCajeroId(Integer sesionCajeroId);
}
