package cl.claveles.terreno.eip.translator.dto.canonico;

public record SocioCanonico (
    String fechaInscripcion,
    DatosPersonales datosPersonales,
    Contacto contacto,
    Aporte aporte
) {}
