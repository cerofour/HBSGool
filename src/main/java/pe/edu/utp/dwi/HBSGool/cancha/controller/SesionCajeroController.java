package pe.edu.utp.dwi.HBSGool.cancha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.cancha.dto.SesionCajeroResumenDTO;
import pe.edu.utp.dwi.HBSGool.cancha.service.SesionCajeroService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sesion-cajero")
@RequiredArgsConstructor
public class SesionCajeroController {
    private final SesionCajeroService sesionCajeroService;

    @GetMapping("/resumen")
    public List<SesionCajeroResumenDTO> getSesionesCajero(
            @RequestParam Integer idCajero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin
    ) {
        return sesionCajeroService.obtenerResumenSesiones(idCajero, fechaInicio, fechaFin);
    }
}
