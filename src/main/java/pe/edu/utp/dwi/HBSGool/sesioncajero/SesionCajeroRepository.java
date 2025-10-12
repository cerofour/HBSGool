package pe.edu.utp.dwi.HBSGool.sesioncajero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SesionCajeroRepository extends JpaRepository<SesionCajeroEntity, Integer> {


    Page<SesionCajeroEntity> findAll(Pageable pageable);

    Page<SesionCajeroEntity> findByCajeroId(short cajeroId, Pageable pageable);

    Optional<SesionCajeroEntity> findFirstByCajeroIdOrderByFechaAperturaDescIdSesionCajeroDesc(short cajeroId);



}
