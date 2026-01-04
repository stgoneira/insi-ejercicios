package cl.claveles.terreno.eip.translator.dto.origen;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "inscritos")
@XmlAccessorType(XmlAccessType.FIELD)
public class Inscritos {

    @XmlAttribute
    private String fecha;

    @XmlElement(name = "socio")
    private List<Socio> socios;

    // getters y setters
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Socio> getSocios() {
        return socios;
    }

    public void setSocios(List<Socio> socios) {
        this.socios = socios;
    }
}
