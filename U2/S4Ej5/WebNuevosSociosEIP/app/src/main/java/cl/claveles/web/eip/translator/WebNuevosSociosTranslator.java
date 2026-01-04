package cl.claveles.web.eip.translator;

import cl.claveles.web.eip.pipeline.Mensaje;
import cl.claveles.web.eip.pipeline.Procesador;
import cl.claveles.web.eip.translator.dto.canonico.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class WebNuevosSociosTranslator implements Procesador {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebNuevosSociosTranslator.class);

    @Override
    public Mensaje process(Mensaje input) {
        String jsonOriginal  = input.getPayload();
        String jsonTraducido = traducirAJsonCanonico(jsonOriginal);
        return new Mensaje(jsonTraducido, null);
    }

    private String traducirAJsonCanonico(String jsonOriginal) {
        JsonObject jsonRoot = JsonParser.parseString( jsonOriginal ).getAsJsonObject();
        String nombreCompleto = jsonRoot.get("nombreCompleto").getAsString();
        String rut = jsonRoot.get("rut").getAsString();
        String telefono = jsonRoot.get("telefono").getAsString();
        String email = jsonRoot.get("email").getAsString();
        String direccion = jsonRoot.get("direccion").getAsString();
        Long montoMensual = jsonRoot.get("montoMensual").getAsLong();
        byte diaPago = jsonRoot.get("diaPago").getAsByte();

        LOGGER.info(
                "MENSAJE ORIGINAL: Nombre={} | rut={} | telefono={} | email={} | dirección={}, montoMensual={}, diaPago={}",
                nombreCompleto,
                rut,
                telefono,
                email,
                direccion,
                montoMensual,
                diaPago
        );

        DatosPersonales datosPersonales = new DatosPersonales(
                TipoIdentificacion.RUT,
                rut,
                nombreCompleto
        );
        List<Telefono> telefonos = List.of(new Telefono(true, "", telefono));
        List<Direccion> direcciones = List.of(
                new Direccion(TipoDireccion.DOMICILIO, direccion, "")
        );
        Aporte aporte = new Aporte(montoMensual, diaPago);
        Contacto contacto = new Contacto(email, telefonos, direcciones);
        SocioCanonico socioCanonico = new SocioCanonico(
                LocalDateTime.now().toString(),
                "",
                datosPersonales,
                contacto,
                aporte
        );

        Gson gson = new Gson();
        String jsonCanonico = gson.toJson(socioCanonico);

        LOGGER.info("MENSAJE CANONICO: {}", jsonCanonico);

        return jsonCanonico;
    }
}
