package cl.claveles.finanzas.adapter.dto.canonico;

public record Direccion (
    TipoDireccion tipo,
    String direccion,
    String comuna
) {}
