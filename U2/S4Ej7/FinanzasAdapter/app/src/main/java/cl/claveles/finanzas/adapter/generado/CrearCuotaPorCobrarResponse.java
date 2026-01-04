
package cl.claveles.finanzas.adapter.generado;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for crearCuotaPorCobrarResponse complex type&lt;/p&gt;.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="crearCuotaPorCobrarResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cuotaId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearCuotaPorCobrarResponse", propOrder = {
    "cuotaId"
})
public class CrearCuotaPorCobrarResponse {

    protected String cuotaId;

    /**
     * Gets the value of the cuotaId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuotaId() {
        return cuotaId;
    }

    /**
     * Sets the value of the cuotaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuotaId(String value) {
        this.cuotaId = value;
    }

}
