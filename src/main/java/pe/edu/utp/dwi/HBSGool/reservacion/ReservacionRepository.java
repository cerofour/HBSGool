package pe.edu.utp.dwi.HBSGool.reservacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservacionRepository extends JpaRepository<ReservacionEntity, Integer> {

    // Buscar por usuario
    Page<ReservacionEntity> findByUsuarioId(Integer usuarioId, Pageable pageable);

    // Buscar por cancha
    Page<ReservacionEntity> findByCanchaId(Short canchaId, Pageable pageable);

    // Buscar por estado
    Page<ReservacionEntity> findByEstadoReservacion(String estado, Pageable pageable);

    // Buscar por DNI
    Page<ReservacionEntity> findByDni(String dni, Pageable pageable);

    // Buscar por fecha (reservas desde una fecha)
    Page<ReservacionEntity> findByTiempoInicioAfter(LocalDateTime fecha, Pageable pageable);

    // Buscar por fecha (reservas antes de una fecha)
    Page<ReservacionEntity> findByTiempoInicioBefore(LocalDateTime fecha, Pageable pageable);

    // Buscar por rango de fechas
    Page<ReservacionEntity> findByTiempoInicioBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    @Query(value = """
            SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
            FROM Reservacion
            WHERE canchaId = :canchaId
                AND tiempoInicio < :endTime
                AND (tiempoInicio + duracion) > :startTime
            """, nativeQuery = true)
    boolean existsOverlappingReservation(
            @Param(value = "canchaId") short canchaId,
            @Param(value = "startTime") LocalDateTime startTime,
            @Param(value = "endTime") LocalDateTime endTime);
}
