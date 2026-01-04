package cl.claveles.web.eip.translator.dto.canonico;

import java.util.List;

public record Contacto(
    String email,
    List<Telefono> telefonos,
    List<Direccion> direcciones
) {}
