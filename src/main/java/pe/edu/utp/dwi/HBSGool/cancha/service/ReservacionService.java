package pe.edu.utp.dwi.HBSGool.cancha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.cancha.repository.ReservacionRepository;
import pe.edu.utp.dwi.HBSGool.cancha.model.ReservacionEntity;

@Service
@RequiredArgsConstructor
public class ReservacionService {
    private final ReservacionRepository reservacionRepository;

    public boolean modificarEstadoReservacion(Integer id, String nuevoEstado) {
        ReservacionEntity reservacion = reservacionRepository.findById(id)
                .orElse(null);
        if (reservacion == null) return false;
        reservacion.setEstadoReservacion(nuevoEstado);
        reservacionRepository.save(reservacion);
        return true;
    }
}
