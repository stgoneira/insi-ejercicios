package cl.claveles.mkt.adapter.dto.canonico;

public record SocioCanonico (
    String fechaInscripcion,
    DatosPersonales datosPersonales,
    Contacto contacto,
    Aporte aporte
) {}
