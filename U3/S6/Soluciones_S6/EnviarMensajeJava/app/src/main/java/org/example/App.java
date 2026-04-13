package org.example;

import jakarta.jms.*;
import javax.naming.*;
import org.slf4j.*;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.error("Debe proporcionar un mensaje como argumento.");
            return;
        }
        String mensaje = args[0];
        enviarMensaje(mensaje);
    }

    private static void enviarMensaje(String mensaje) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");
            Destination destino = (Destination) jndi.lookup("colas/AccesosSospechosos");

            String brokerUsuario  = "estacionamientos";
            String brokerPassword = "Accesos26.,.";
            try (Connection connection = connectionFactory.createConnection(brokerUsuario, brokerPassword) ) {
                Session sesion = connection.createSession();
                MessageProducer messageProducer = sesion.createProducer(destino);
                TextMessage textMessage = sesion.createTextMessage(mensaje);
                messageProducer.send( textMessage );
                LOGGER.info("Enviando mensaje a cola: "+mensaje);
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
}
