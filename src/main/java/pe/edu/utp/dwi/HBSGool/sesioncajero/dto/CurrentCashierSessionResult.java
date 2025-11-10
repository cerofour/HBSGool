package pe.edu.utp.dwi.HBSGool.sesioncajero.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
public class CurrentCashierSessionResult {
    private Boolean abierta;
    private Integer idSesion;
    private Integer idCierre;
    private Short idCajero;
    private Double montoApertura;
    private LocalDateTime fechaApertura;
}
