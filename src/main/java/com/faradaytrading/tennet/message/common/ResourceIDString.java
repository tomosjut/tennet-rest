
package com.faradaytrading.tennet.message.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public class ResourceIDString {

    @JsonProperty("value")
    protected String value;

    @NotNull
    @JsonProperty("codingScheme")
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
