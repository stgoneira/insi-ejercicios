
package cl.claveles.finanzas.adapter.generado;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cl.claveles.finanzas.adapter.generado package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _CrearCuotaPorCobrar_QNAME = new QName("http://finanzas.claveles.cl/", "crearCuotaPorCobrar");
    private static final QName _CrearCuotaPorCobrarResponse_QNAME = new QName("http://finanzas.claveles.cl/", "crearCuotaPorCobrarResponse");
    private static final QName _CrearSocio_QNAME = new QName("http://finanzas.claveles.cl/", "crearSocio");
    private static final QName _CrearSocioResponse_QNAME = new QName("http://finanzas.claveles.cl/", "crearSocioResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cl.claveles.finanzas.adapter.generado
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CrearCuotaPorCobrar }
     * 
     * @return
     *     the new instance of {@link CrearCuotaPorCobrar }
     */
    public CrearCuotaPorCobrar createCrearCuotaPorCobrar() {
        return new CrearCuotaPorCobrar();
    }

    /**
     * Create an instance of {@link CrearCuotaPorCobrarResponse }
     * 
     * @return
     *     the new instance of {@link CrearCuotaPorCobrarResponse }
     */
    public CrearCuotaPorCobrarResponse createCrearCuotaPorCobrarResponse() {
        return new CrearCuotaPorCobrarResponse();
    }

    /**
     * Create an instance of {@link CrearSocio }
     * 
     * @return
     *     the new instance of {@link CrearSocio }
     */
    public CrearSocio createCrearSocio() {
        return new CrearSocio();
    }

    /**
     * Create an instance of {@link CrearSocioResponse }
     * 
     * @return
     *     the new instance of {@link CrearSocioResponse }
     */
    public CrearSocioResponse createCrearSocioResponse() {
        return new CrearSocioResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearCuotaPorCobrar }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearCuotaPorCobrar }{@code >}
     */
    @XmlElementDecl(namespace = "http://finanzas.claveles.cl/", name = "crearCuotaPorCobrar")
    public JAXBElement<CrearCuotaPorCobrar> createCrearCuotaPorCobrar(CrearCuotaPorCobrar value) {
        return new JAXBElement<>(_CrearCuotaPorCobrar_QNAME, CrearCuotaPorCobrar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearCuotaPorCobrarResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearCuotaPorCobrarResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://finanzas.claveles.cl/", name = "crearCuotaPorCobrarResponse")
    public JAXBElement<CrearCuotaPorCobrarResponse> createCrearCuotaPorCobrarResponse(CrearCuotaPorCobrarResponse value) {
        return new JAXBElement<>(_CrearCuotaPorCobrarResponse_QNAME, CrearCuotaPorCobrarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearSocio }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearSocio }{@code >}
     */
    @XmlElementDecl(namespace = "http://finanzas.claveles.cl/", name = "crearSocio")
    public JAXBElement<CrearSocio> createCrearSocio(CrearSocio value) {
        return new JAXBElement<>(_CrearSocio_QNAME, CrearSocio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearSocioResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CrearSocioResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://finanzas.claveles.cl/", name = "crearSocioResponse")
    public JAXBElement<CrearSocioResponse> createCrearSocioResponse(CrearSocioResponse value) {
        return new JAXBElement<>(_CrearSocioResponse_QNAME, CrearSocioResponse.class, null, value);
    }

}
