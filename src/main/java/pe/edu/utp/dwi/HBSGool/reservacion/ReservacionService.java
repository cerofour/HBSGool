package pe.edu.utp.dwi.HBSGool.reservacion;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.exception.ReservationNotFoundException;
import pe.edu.utp.dwi.HBSGool.exception.ReservationOverlapException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ReservacionService {

    private final ReservacionRepository repository;

    public Page<ReservacionDto> listReservaciones(
            Integer usuarioId,
            Short canchaId,
            String estado,
            String dni,
            Pageable pageable
    ) {
        Page<ReservacionEntity> page;

        if (usuarioId != null) {
            page = repository.findByUsuarioId(usuarioId, pageable);
        } else if (canchaId != null) {
            page = repository.findByCanchaId(canchaId, pageable);
        } else if (estado != null) {
            page = repository.findByEstadoReservacion(estado, pageable);
        } else if (dni != null) {
            page = repository.findByDni(dni, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public ReservacionDto getById(Integer id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    public ReservacionDto createReservation(ReservacionDto dto) {

        boolean existsOverlapping = repository.existsOverlappingReservation(
                dto.getCanchaId(),
                dto.getTiempoInicio(),
                dto.getTiempoInicio().plus(stringToDuration(dto.getDuracion()))
        );

        if(existsOverlapping) throw new ReservationOverlapException("La cancha ya está reservada dentro de ese horario");

        ReservacionEntity reservation = ReservacionEntity.builder()
                .usuarioId(dto.getUsuarioId())
                .canchaId(dto.getCanchaId())
                .cajeroId(dto.getCajeroId())
                .tiempoInicio(dto.getTiempoInicio())
                .dni(dto.getDni())
                .duracion(stringToDuration(dto.getDuracion()))
                .precioTotal(dto.getPrecioTotal())
                .estadoReservacion(dto.getEstadoReservacion())
                .build();

        return toDto(repository.save(reservation));
    }

    public void cancelReservation(Integer reservationId) {
        ReservacionEntity reservacion = this.repository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservación no encontrada."));

        reservacion.setEstadoReservacion("CANCELADO");
    }

    private ReservacionDto toDto(ReservacionEntity e) {
        return new ReservacionDto(
                e.getIdReservacion(),
                e.getUsuarioId(),
                e.getCanchaId(),
                e.getCajeroId(),
                e.getTiempoInicio(),
                e.getDni(),
                formatDuration(e.getDuracion()),
                e.getPrecioTotal(),
                e.getEstadoReservacion()
        );
    }

    static public Duration stringToDuration(String duration) {
        if (duration == null || duration.isBlank()) return Duration.ZERO;

        String normalized = duration.toLowerCase()
                .replaceAll("(\\d+)\\s*(hours|hour|h)", "PT$1H")
                .replaceAll("(\\d+)\\s*(minutes|minute|min|m)", "$1M").trim();

        normalized = normalized.replaceAll(" ", "");

        if (!normalized.startsWith("P")) normalized = "PT" + normalized;

        System.out.println(normalized);

        return Duration.parse(normalized);
    }

    static public String formatDuration(Duration duration) {
        if (duration == null) {
            return null;
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        if (hours > 0 && minutes > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " " +
                    minutes + " minute" + (minutes > 1 ? "s" : "");
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "");
        } else {
            return minutes + " minute" + (minutes > 1 ? "s" : "");
        }
    }
}
