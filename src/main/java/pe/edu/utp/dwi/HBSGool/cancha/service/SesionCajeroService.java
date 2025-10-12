package pe.edu.utp.dwi.HBSGool.cancha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.cancha.dto.SesionCajeroResumenDTO;
import pe.edu.utp.dwi.HBSGool.cancha.model.SesionCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cancha.model.CierreCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cancha.repository.SesionCajeroRepository;
import pe.edu.utp.dwi.HBSGool.cancha.repository.CierreCajeroRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionCajeroService {
    private final SesionCajeroRepository sesionCajeroRepository;
    private final CierreCajeroRepository cierreCajeroRepository;

    public List<SesionCajeroResumenDTO> obtenerResumenSesiones(Integer idCajero, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<SesionCajeroEntity> sesiones;
        if (fechaInicio != null && fechaFin != null) {
            sesiones = sesionCajeroRepository.findByCajeroIdAndFechaAperturaBetween(idCajero, fechaInicio, fechaFin);
        } else {
            sesiones = sesionCajeroRepository.findByCajeroId(idCajero);
        }
        List<SesionCajeroResumenDTO> resumen = new ArrayList<>();
        for (SesionCajeroEntity sesion : sesiones) {
            CierreCajeroEntity cierre = cierreCajeroRepository.findBySesionCajeroId(sesion.getIdSesionCajero());
            if (cierre != null) {
                resumen.add(new SesionCajeroResumenDTO(
                    sesion.getIdSesionCajero(),
                    sesion.getCajeroId(),
                    sesion.getFechaApertura(),
                    cierre.getFecha(),
                    cierre.getMontoTeorico(),
                    cierre.getMontoReal()
                ));
            }
        }
        return resumen;
    }
}
