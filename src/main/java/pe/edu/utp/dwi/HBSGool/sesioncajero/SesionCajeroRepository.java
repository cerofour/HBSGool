package pe.edu.utp.dwi.HBSGool.sesioncajero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SesionCajeroRepository extends JpaRepository<SesionCajeroEntity, Integer> {

    Optional<SesionCajeroEntity> findById(Integer id);

    Page<SesionCajeroEntity> findAll(Pageable pageable);

    Optional<SesionCajeroEntity> findFirstByCajero_CashierIdOrderByFechaAperturaDescIdSesionCajeroDesc(Short cajeroId);

    Page<SesionCajeroEntity> findByCajero_CashierId(Short idCajero, Pageable pageable);
    Page<SesionCajeroEntity> findByCajero_CashierIdAndFechaAperturaBetween(Short idCajero, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<SesionCajeroEntity> findByCajero_CashierIdAndFechaAperturaAfter(Short idCajero, LocalDateTime inicio, Pageable pageable);
    Page<SesionCajeroEntity> findByCajero_CashierIdAndFechaAperturaBefore(Short idCajero, LocalDateTime fin, Pageable pageable);

    Page<SesionCajeroEntity> findByFechaAperturaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<SesionCajeroEntity> findByFechaAperturaAfter(LocalDateTime inicio, Pageable pageable);
    Page<SesionCajeroEntity> findByFechaAperturaBefore(LocalDateTime fin, Pageable pageable);

}
