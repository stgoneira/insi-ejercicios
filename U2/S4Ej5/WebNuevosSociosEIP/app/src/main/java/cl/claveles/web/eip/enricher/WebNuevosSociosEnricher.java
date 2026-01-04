package cl.claveles.web.eip.enricher;

import cl.claveles.web.eip.pipeline.Mensaje;
import cl.claveles.web.eip.pipeline.Procesador;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebNuevosSociosEnricher implements Procesador {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebNuevosSociosEnricher.class);

    @Override
    public Mensaje process(Mensaje input) {
        LOGGER.info("Mensaje original: {}", input.getPayload());
        String jsonOriginal  = input.getPayload();
        JsonObject jsonRoot = JsonParser.parseString( jsonOriginal ).getAsJsonObject();
        jsonRoot.addProperty("origen", "web");
        var gson = new Gson();
        var jsonEnriquecido = gson.toJson(jsonRoot);
        LOGGER.info("Mensaje procesado: {}", jsonEnriquecido);
        return new Mensaje(jsonEnriquecido, null);
    }
}
