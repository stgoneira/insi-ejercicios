package cl.claveles.terreno.eip.translator.dto.canonico;

public record DatosPersonales (
    TipoIdentificacion tipoIdentificacion,
    String identificador,
    String nombre
) {}
