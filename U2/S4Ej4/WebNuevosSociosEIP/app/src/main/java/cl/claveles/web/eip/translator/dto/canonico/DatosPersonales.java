package cl.claveles.web.eip.translator.dto.canonico;

public record DatosPersonales (
    TipoIdentificacion tipoIdentificacion,
    String identificador,
    String nombre
) {}
