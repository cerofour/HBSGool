package pe.edu.utp.dwi.HBSGool.cancha.dto;

import java.time.LocalDateTime;

public class SesionCajeroResumenDTO {
    private Integer idSesionCajero;
    private Integer idCajero;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private Double montoTeorico;
    private Double montoReal;
    private Double diferencia;

    public SesionCajeroResumenDTO() {}

    public SesionCajeroResumenDTO(Integer idSesionCajero, Integer idCajero, LocalDateTime fechaApertura, LocalDateTime fechaCierre, Double montoTeorico, Double montoReal) {
        this.idSesionCajero = idSesionCajero;
        this.idCajero = idCajero;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.montoTeorico = montoTeorico;
        this.montoReal = montoReal;
        this.diferencia = (montoTeorico != null && montoReal != null) ? montoReal - montoTeorico : null;
    }

    // Getters y setters
    public Integer getIdSesionCajero() { return idSesionCajero; }
    public void setIdSesionCajero(Integer idSesionCajero) { this.idSesionCajero = idSesionCajero; }
    public Integer getIdCajero() { return idCajero; }
    public void setIdCajero(Integer idCajero) { this.idCajero = idCajero; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }
    public Double getMontoTeorico() { return montoTeorico; }
    public void setMontoTeorico(Double montoTeorico) { this.montoTeorico = montoTeorico; }
    public Double getMontoReal() { return montoReal; }
    public void setMontoReal(Double montoReal) { this.montoReal = montoReal; }
    public Double getDiferencia() { return diferencia; }
    public void setDiferencia(Double diferencia) { this.diferencia = diferencia; }
}
