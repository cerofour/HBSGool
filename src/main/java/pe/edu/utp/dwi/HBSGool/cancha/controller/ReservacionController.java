package pe.edu.utp.dwi.HBSGool.cancha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.cancha.service.ReservacionService;

import java.util.Map;

@RestController
@RequestMapping("/api/reservacion")
@RequiredArgsConstructor
public class ReservacionController {
    private final ReservacionService reservacionService;

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> modificarEstadoReservacion(@PathVariable("id") Integer id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estadoReservacion");
        boolean resultado = reservacionService.modificarEstadoReservacion(id, nuevoEstado);
        if (resultado) {
            return ResponseEntity.ok("Estado de reservación actualizado exitosamente");
        } else {
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado de la reservación");
        }
    }
}
