package pe.edu.utp.dwi.HBSGool.cancha.model;

public class CanchaPatchDTO {
    private String nombre;
    private String descripcion;
    private Double precioHora;
    private String estadoCancha;

    public CanchaPatchDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecioHora() { return precioHora; }
    public void setPrecioHora(Double precioHora) { this.precioHora = precioHora; }

    public String getEstadoCancha() { return estadoCancha; }
    public void setEstadoCancha(String estadoCancha) { this.estadoCancha = estadoCancha; }
}
