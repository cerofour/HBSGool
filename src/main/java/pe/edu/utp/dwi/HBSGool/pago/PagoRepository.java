package pe.edu.utp.dwi.HBSGool.pago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<PagoEntity, Integer> {

    @Query(value = """
            SELECT COALESCE(SUM(cantidadDinero), 0)
                    FROM pago
                    WHERE estadoPago = 'CONFIRMADO'
                      AND medioPago = 'EFECTIVO'
                      AND sesionCajeroId = :sesionCajeroId
            """, nativeQuery = true)
    double getTotalCashPaymentsConfirmedByCashierSessionId(Integer sesionCajeroId);

    // Buscar por reservación
    Page<PagoEntity> findByReservacionId(Integer reservacionId, Pageable pageable);

    List<PagoEntity> findByReservacionId(Integer reservacionId);

    // Buscar por sesión de cajero
    Page<PagoEntity> findBySesionCajeroId(Integer sesionCajeroId, Pageable pageable);

    // Buscar por medio de pago
    Page<PagoEntity> findByMedioPago(String medioPago, Pageable pageable);

    // Buscar por fecha (pagos desde una fecha)
    Page<PagoEntity> findByFechaAfter(LocalDateTime fecha, Pageable pageable);

    // Buscar por fecha (pagos antes de una fecha)
    Page<PagoEntity> findByFechaBefore(LocalDateTime fecha, Pageable pageable);

    // Buscar por rango de fechas
    Page<PagoEntity> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    // Buscar por rango de montos
    Page<PagoEntity> findByCantidadDineroBetween(BigDecimal min, BigDecimal max, Pageable pageable);
}
