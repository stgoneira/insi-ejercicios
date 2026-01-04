package cl.claveles.finanzas.adapter.dto.canonico;

import java.time.LocalDateTime;

public record SocioCanonico (
    String fechaInscripcion,
    DatosPersonales datosPersonales,
    Contacto contacto,
    Aporte aporte
) {}
