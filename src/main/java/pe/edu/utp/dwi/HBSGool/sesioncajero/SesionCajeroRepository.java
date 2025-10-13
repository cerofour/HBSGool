package pe.edu.utp.dwi.HBSGool.sesioncajero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionCajeroRepository extends JpaRepository<SesionCajeroEntity, Integer> {


    Page<SesionCajeroEntity> findAll(Pageable pageable);

    Optional<SesionCajeroEntity> findFirstByCajeroIdOrderByFechaAperturaDescIdSesionCajeroDesc(Short cajeroId);

    Page<SesionCajeroEntity> findByCajeroId(Short idCajero, Pageable pageable);
    Page<SesionCajeroEntity> findByCajeroIdAndFechaAperturaBetween(Short idCajero, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<SesionCajeroEntity> findByCajeroIdAndFechaAperturaAfter(Short idCajero, LocalDateTime inicio, Pageable pageable);
    Page<SesionCajeroEntity> findByCajeroIdAndFechaAperturaBefore(Short idCajero, LocalDateTime fin, Pageable pageable);

    Page<SesionCajeroEntity> findByFechaAperturaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<SesionCajeroEntity> findByFechaAperturaAfter(LocalDateTime inicio, Pageable pageable);
    Page<SesionCajeroEntity> findByFechaAperturaBefore(LocalDateTime fin, Pageable pageable);

}
