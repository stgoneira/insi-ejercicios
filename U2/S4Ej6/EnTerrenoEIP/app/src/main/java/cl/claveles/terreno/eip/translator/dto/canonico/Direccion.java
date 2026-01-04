package cl.claveles.terreno.eip.translator.dto.canonico;

public record Direccion (
    TipoDireccion tipo,
    String direccion,
    String comuna
) {}
