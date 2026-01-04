package cl.claveles.xmladapter.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Socio {

    @XmlAttribute
    private int monto;

    @XmlAttribute
    private int dia;

    private String nombre;
    private String apellido;
    private String rut;
    private String dv;
    private String telefono;
    private String correo;
    private String direccion;
    private String comuna;

    // getters y setters
    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
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

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return (
            "Socio{" +
            "monto=" +
            monto +
            ", dia=" +
            dia +
            ", nombre='" +
            nombre +
            '\'' +
            ", apellido='" +
            apellido +
            '\'' +
            ", rut='" +
            rut +
            '\'' +
            ", dv='" +
            dv +
            '\'' +
            ", telefono='" +
            telefono +
            '\'' +
            ", direccion='" +
            direccion +
            '\'' +
            ", comuna='" +
            comuna +
            '\'' +
            '}'
        );
    }
}
