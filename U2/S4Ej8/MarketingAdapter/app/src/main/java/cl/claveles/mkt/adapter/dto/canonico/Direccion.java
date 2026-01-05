package cl.claveles.mkt.adapter.dto.canonico;

public record Direccion (
    TipoDireccion tipo,
    String direccion,
    String comuna
) {}
