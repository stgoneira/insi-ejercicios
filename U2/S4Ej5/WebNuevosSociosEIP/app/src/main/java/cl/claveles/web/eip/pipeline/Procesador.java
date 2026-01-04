package cl.claveles.web.eip.pipeline;

public interface Procesador {
    Mensaje process(Mensaje input);
}
