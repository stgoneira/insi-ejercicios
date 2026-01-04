package cl.losclaveles.web.socios.listener;

import cl.losclaveles.web.socios.NuevoSocio;
import com.google.gson.Gson;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@Component
public class NuevoSocioListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NuevoSocioListener.class);

    @EventListener
    public void gestionarNuevoSocio(NuevoSocio nuevoSocio) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");
            Destination destino = (Destination) jndi.lookup("colas/webNuevosSocios");

            String brokerUsuario  = "claveles";
            String brokerPassword = "Fundacion25..";
            try (Connection connection = connectionFactory.createConnection(brokerUsuario, brokerPassword) ) {
                Session sesion = connection.createSession();
                MessageProducer messageProducer = sesion.createProducer(destino);

                String mensaje = convertirStrToJson(nuevoSocio);
                TextMessage textMessage = sesion.createTextMessage(mensaje);
                messageProducer.send( textMessage );

                LOGGER.info("Enviando mensaje a cola: {}", mensaje);
            }
        } catch (NamingException | JMSException e) {
            LOGGER.error("Problema al intentar enviar el mensaje JMS", e);
            throw new RuntimeException(e);
        } finally {
            if(jndi != null) {
                try {
                    jndi.close();
                } catch (NamingException fne) {
                    LOGGER.error("Problema al cerrar JNDI", fne);
                }
            }
        }
    }

    private String convertirStrToJson(NuevoSocio nuevoSocio) {
        var gson = new Gson();
        return gson.toJson(nuevoSocio);
    }

}
