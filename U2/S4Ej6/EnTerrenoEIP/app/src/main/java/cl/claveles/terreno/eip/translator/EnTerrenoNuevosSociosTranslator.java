package cl.claveles.terreno.eip.translator;

import cl.claveles.terreno.eip.pipeline.Mensaje;
import cl.claveles.terreno.eip.pipeline.Procesador;
import cl.claveles.terreno.eip.translator.dto.canonico.*;
import cl.claveles.terreno.eip.translator.dto.origen.Socio;
import com.google.gson.Gson;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

public class EnTerrenoNuevosSociosTranslator implements Procesador {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnTerrenoNuevosSociosTranslator.class);

    @Override
    public Mensaje procesar(Mensaje input) {
        var jsonCanonico = traducirAJsonCanonico(input.getPayload());
        return new Mensaje(jsonCanonico, null);
    }

    private String traducirAJsonCanonico(String xmlOriginal) {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Socio.class);
            Unmarshaller unmarshaller = contexto.createUnmarshaller();
            StringReader reader = new StringReader(xmlOriginal);
            Socio socio = (Socio) unmarshaller.unmarshal(reader);
            String nombreCompleto   = socio.getNombre() +" "+ socio.getApellido();
            String rut              = socio.getRut();
            String telefono         = socio.getTelefono();
            String direccion        = socio.getDireccion();
            String comuna           = socio.getComuna();
            Long montoMensual       = socio.getMonto();
            byte diaPago            = Byte.parseByte(socio.getDia()+"");

            LOGGER.info("MENSAJE ORIGINAL: Nombre={} | rut={} | telefono={} | dirección={}, montoMensual={}, diaPago={}",
                    nombreCompleto,
                    rut,
                    telefono,
                    direccion,
                    montoMensual,
                    diaPago
            );

            DatosPersonales datosPersonales = new DatosPersonales(TipoIdentificacion.RUT, rut, nombreCompleto);
            List<Telefono> telefonos = List.of(new Telefono(true, "", telefono));
            List<Direccion> direcciones = List.of(new Direccion(TipoDireccion.DOMICILIO, direccion, comuna));
            Aporte aporte = new Aporte(montoMensual, diaPago);
            Contacto contacto = new Contacto(telefonos, direcciones);
            SocioCanonico socioCanonico = new SocioCanonico(LocalDateTime.now().toString(), datosPersonales, contacto, aporte);

            Gson gson = new Gson();
            String jsonCanonico = gson.toJson(socioCanonico);

            LOGGER.info("MENSAJE CANONICO: {}",jsonCanonico);

            return jsonCanonico;
        } catch (JAXBException e) {
            LOGGER.error("Error al instanciar JAXB", e);
            throw new RuntimeException(e);
        }
    }
}
