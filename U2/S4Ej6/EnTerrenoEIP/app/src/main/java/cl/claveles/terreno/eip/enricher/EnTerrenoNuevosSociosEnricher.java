package cl.claveles.terreno.eip.enricher;

import cl.claveles.terreno.eip.pipeline.Mensaje;
import cl.claveles.terreno.eip.pipeline.Procesador;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnTerrenoNuevosSociosEnricher implements Procesador {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnTerrenoNuevosSociosEnricher.class);

    @Override
    public Mensaje procesar(Mensaje input) {
        LOGGER.info("Mensaje original: {}", input.getPayload());
        String jsonOriginal  = input.getPayload();
        JsonObject jsonRoot = JsonParser.parseString( jsonOriginal ).getAsJsonObject();
        jsonRoot.addProperty("origen", "terreno");
        var gson = new Gson();
        var jsonEnriquecido = gson.toJson(jsonRoot);
        LOGGER.info("Mensaje procesado: {}", jsonEnriquecido);
        return new Mensaje(jsonEnriquecido, null);
    }
}
