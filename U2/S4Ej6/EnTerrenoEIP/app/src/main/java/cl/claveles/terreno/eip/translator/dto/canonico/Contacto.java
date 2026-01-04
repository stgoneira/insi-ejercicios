package cl.claveles.terreno.eip.translator.dto.canonico;

import java.util.List;

public record Contacto (
    List<Telefono> telefonos,
    List<Direccion> direcciones
) {}
