package cl.claveles.web.eip.translator.dto.canonico;

public record SocioCanonico(
    String fechaInscripcion,
    String origen,
    DatosPersonales datosPersonales,
    Contacto contacto,
    Aporte aporte
) {}
