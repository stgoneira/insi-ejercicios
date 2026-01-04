package cl.claveles.web.eip.translator.dto.canonico;

public record Direccion (
    TipoDireccion tipo,
    String direccion,
    String comuna
) {}
