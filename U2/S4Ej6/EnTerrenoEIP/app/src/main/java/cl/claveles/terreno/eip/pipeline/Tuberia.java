package cl.claveles.terreno.eip.pipeline;

import java.util.ArrayList;
import java.util.List;

public class Tuberia {
    private final List<Procesador> processors = new ArrayList<>();

    public void addProcesador(Procesador p) {
        processors.add(p);
    }

    public Mensaje ejecutar(Mensaje input) {
        Mensaje current = input;
        for (Procesador p : processors) {
            current = p.procesar(current);
        }
        return current;
    }
}
