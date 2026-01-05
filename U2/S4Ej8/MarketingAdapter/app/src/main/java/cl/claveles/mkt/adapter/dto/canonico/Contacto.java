package cl.claveles.mkt.adapter.dto.canonico;

import java.util.List;

public record Contacto (
    List<Telefono> telefonos,
    List<Direccion> direcciones
) {}
