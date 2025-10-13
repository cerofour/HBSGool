package pe.edu.utp.dwi.HBSGool.reservacion;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.cancha.model.CanchaEntity;
import pe.edu.utp.dwi.HBSGool.cancha.service.CanchaService;
import pe.edu.utp.dwi.HBSGool.exception.CanchaNotFoundException;
import pe.edu.utp.dwi.HBSGool.exception.ReservationNotFoundException;
import pe.edu.utp.dwi.HBSGool.exception.ReservationOverlapException;
import pe.edu.utp.dwi.HBSGool.exception.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.pago.PagoEntity;
import pe.edu.utp.dwi.HBSGool.pago.PagoService;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsUserRequest;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsUserResult;
import pe.edu.utp.dwi.HBSGool.shared.FileStorageService;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservacionService {

    private final ReservacionRepository repository;
    private final CanchaService canchaService;
    private final AuthService authService;
    private final FileStorageService fileStorageService;
    private final PagoService pagoService;

    public Page<ReservacionDto> listReservaciones(
            Integer usuarioId,
            Integer canchaId,
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

    public Optional<ReservacionEntity> findById(Integer reservationId) {
        return repository.findById(reservationId);
    }

    public ReservacionDto getById(Integer id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public CreateReservationAsUserResult createReservationAsUser(CreateReservationAsUserRequest request, MultipartFile evidencia) {

        UsuarioEntity currentUser = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("No hay un usuario logueado actualmente."));

        CanchaEntity selectedCancha = canchaService.findByCanchaId(request.canchaId())
                .orElseThrow(() -> new CanchaNotFoundException(request.canchaId()));

        Duration reservationDuration = stringToDuration(request.duracion());

        double totalPrice = Math.round(
                selectedCancha.getHourlyPrice() * (reservationDuration.toMinutes() / 60.0) * 100.0
        ) / 100.0;

        boolean existsOverlapping = repository.existsOverlappingReservation(
                request.canchaId(),
                request.tiempoInicio(),
                request.tiempoInicio().plus(reservationDuration)
        );

        if (existsOverlapping)
            throw new ReservationOverlapException("La cancha ya está reservada dentro de ese horario");

        // Guardamos la reservación
        ReservacionEntity reservation = ReservacionEntity.builder()
                .usuarioId(currentUser.getUserId())
                .canchaId(request.canchaId())
                .cajeroId(null)
                .tiempoInicio(request.tiempoInicio())
                .dni(request.dni())
                .duracion(reservationDuration)
                .precioTotal(BigDecimal.valueOf(totalPrice))
                .estadoReservacion("POR CONFIRMAR")
                .build();

        repository.save(reservation);

        // Guardamos el archivo de evidencia
        String evidencePath = fileStorageService.saveEvidenceFile(evidencia, reservation.getIdReservacion());

        PagoEntity payment = pagoService.createPayment(
                reservation.getIdReservacion(),
                null,
		        BigDecimal.valueOf(request.montoInicial()),
                request.medioPago(),
                evidencePath
        );

        return new CreateReservationAsUserResult(
                reservation.getUsuarioId(),
                reservation.getCanchaId(),
                reservation.getDni(),
                reservation.getTiempoInicio(),
                reservation.getDuracion().toString(),
                reservation.getPrecioTotal(),
                reservation.getEstadoReservacion(),
                totalPrice - request.montoInicial()
        );
    }

    public void cancelReservation(Integer reservationId) {
        ReservacionEntity reservation = this.repository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservación no encontrada."));

        reservation.setEstadoReservacion("CANCELADO");
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

    public Page<ReservacionDto> listCurrentUserReservations(Pageable pageable) {

        UsuarioEntity currentUser = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("No hay un usuario autenticado en este momento."));

        return repository.findByUsuarioId(currentUser.getUserId(), pageable).map(this::toDto);
    }

    public void markAsConfirmed(ReservacionEntity reservation) {
        reservation.setEstadoReservacion("CONFIRMADA");

        repository.save(reservation);
    }
}
