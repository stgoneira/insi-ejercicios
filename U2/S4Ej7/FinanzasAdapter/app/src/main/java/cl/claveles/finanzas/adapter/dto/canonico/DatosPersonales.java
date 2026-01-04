package cl.claveles.finanzas.adapter.dto.canonico;

public record DatosPersonales (
    TipoIdentificacion tipoIdentificacion,
    String identificador,
    String nombre
) {}
