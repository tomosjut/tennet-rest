
package com.faradaytrading.tennet.message.common;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeasurementPointID_String", propOrder = {
    "value"
})
public class MeasurementPointIDString {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "codingScheme", required = true)
    protected String codingScheme;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the codingScheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodingScheme() {
        return codingScheme;
    }

    /**
     * Sets the value of the codingScheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodingScheme(String value) {
        this.codingScheme = value;
    }

}
