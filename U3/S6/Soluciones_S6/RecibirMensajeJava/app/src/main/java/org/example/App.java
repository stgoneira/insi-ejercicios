package org.example;

import jakarta.jms.*;
import javax.naming.*;
import org.slf4j.*;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws java.io.IOException {
        recibirMensajes();
        System.in.read();
    }

    private static void recibirMensajes() {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");
            Destination destino = (Destination) jndi.lookup("colas/AccesosSospechosos");

            String brokerUsuario  = "estacionamientos";
            String brokerPassword = "Accesos26.,.";
            try (Connection connection = connectionFactory.createConnection(brokerUsuario, brokerPassword) ) {                
                Session sesion = connection.createSession(Session.CLIENT_ACKNOWLEDGE);
                MessageConsumer consumer = sesion.createConsumer(destino);
                connection.start();
                Message mensaje = consumer.receive();                
                LOGGER.info("Mensaje recibido: {}", mensaje.getBody(String.class));
            }
        } catch (NamingException | JMSException e) {
            LOGGER.error("Problema al intentar recibir el mensaje", e);
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
