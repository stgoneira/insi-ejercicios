package cl.claveles.web.eip;

import cl.claveles.web.eip.enricher.WebNuevosSociosEnricher;
import cl.claveles.web.eip.pipeline.Mensaje;
import cl.claveles.web.eip.pipeline.Tuberia;
import cl.claveles.web.eip.translator.WebNuevosSociosTranslator;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        var tuberia = new Tuberia();
        tuberia.addProcesador( new WebNuevosSociosTranslator());
        tuberia.addProcesador( new WebNuevosSociosEnricher() );

        String brokerUsuario = "claveles";
        String brokerPassword = "Fundacion25..";

        procesarMensajesJMS(
                "colas/webNuevosSocios",
                "topicos/nuevosSocios",
                tuberia,
                brokerUsuario,
                brokerPassword
        );
    }

    private static void procesarMensajesJMS(
            String canalOrigen,
            String canalDestino,
            Tuberia tuberiaProcesamiento,
            String brokerUsuario,
            String brokerPassword
    ) {
        InitialContext jndi = null;
        try {
            jndi = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) jndi.lookup("connFactoryBroker1");
            Destination origen = (Destination) jndi.lookup( canalOrigen);
            Destination destino = (Destination) jndi.lookup( canalDestino);

            try (
                Connection connection = connectionFactory.createConnection(brokerUsuario, brokerPassword)
            ) {
                Session sesion = connection.createSession( Session.CLIENT_ACKNOWLEDGE );
                MessageConsumer consumer = sesion.createConsumer(origen);
                consumer.setMessageListener( (jmsMensaje ) -> {
                    try {
                        var producer = sesion.createProducer(destino);

                        var mensajeTuberia = new Mensaje(jmsMensaje.getBody(String.class), null);
                        var jsonProcesado = tuberiaProcesamiento.ejecutar( mensajeTuberia );

                        var jmsMensajeProcesado = sesion.createTextMessage(jsonProcesado.getPayload());
                        producer.send(jmsMensajeProcesado);
                        jmsMensaje.acknowledge();
                    } catch (JMSException e) {
                                throw new RuntimeException(e);
                    }
                });
                connection.start();
                Thread.currentThread().join(); // mantiene la aplicación abierta
            } catch (InterruptedException e) {
                LOGGER.error( "Error al mantener la aplicación en ejecución (Thread.join())", e );
                throw new RuntimeException(e);
            }
        } catch (NamingException ne) {
            LOGGER.error("Error al usar JNDI", ne);
            throw new RuntimeException(ne);
        } catch (JMSException je) {
            LOGGER.error("Error al usar JMS", je);
            throw new RuntimeException(je);
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
