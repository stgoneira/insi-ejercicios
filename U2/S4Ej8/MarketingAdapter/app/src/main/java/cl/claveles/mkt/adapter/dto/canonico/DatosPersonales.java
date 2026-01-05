package cl.claveles.mkt.adapter.dto.canonico;

public record DatosPersonales (
    TipoIdentificacion tipoIdentificacion,
    String identificador,
    String nombre
) {}
