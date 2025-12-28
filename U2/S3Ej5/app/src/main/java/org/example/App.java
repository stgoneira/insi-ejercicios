package org.example;

// Jakarta Messaging
import jakarta.jms.*;
// JNDI
import javax.naming.InitialContext;
import javax.naming.NamingException;
// SL4J
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory =
                (ConnectionFactory) jndi.lookup("connFactoryBroker1");
            Destination destino = (Destination) jndi.lookup(
                "topicos/customerSearches"
            );

            String brokerUsuario = "retailnova";
            String brokerPassword = "Nova25";
            try (
                Connection connection = connectionFactory.createConnection(
                    brokerUsuario,
                    brokerPassword
                )
            ) {
                Session sesion = connection.createSession();
                MessageProducer messageProducer = sesion.createProducer(
                    destino
                );

                //String mensaje = "Mensaje de prueba";
                String mensaje = args[0];
                TextMessage textMessage = sesion.createTextMessage(mensaje);
                messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                messageProducer.send(textMessage);

                LOGGER.info(
                    "Enviando mensaje a tópico busquedasClientes: " + mensaje
                );
            }
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (jndi != null) {
                try {
                    jndi.close();
                } catch (NamingException fne) {
                    LOGGER.error("Problema al cerrar JNDI", fne);
                }
            }
        }
    }
}
