package cl.losclaveles.web.socios;

public class NuevoSocio {
    private String nombreCompleto;
    private String rut;
    private String telefono;
    private String email;
    private String direccion;
    private Long montoMensual;
    private byte diaPago;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRut() {
        return rut;
    }
    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getMontoMensual() {
        return montoMensual;
    }
    public void setMontoMensual(Long montoMensual) {
        this.montoMensual = montoMensual;
    }

    public byte getDiaPago() {
        return diaPago;
    }
    public void setDiaPago(byte diaPago) {
        this.diaPago = diaPago;
    }

    @Override
    public String toString() {
        return "NuevoSocio{" +
                "nombreCompleto='" + nombreCompleto + '\'' +
                ", rut='" + rut + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", montoMensual=" + montoMensual +
                ", diaPago=" + diaPago +
                '}';
    }
}
