
package com.faradaytrading.tennet.message.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema
public class FinancialPrice {

    @NotNull
    @JsonProperty("amount")
    protected BigDecimal amount;

    @JsonProperty("direction")
    protected String direction;

    @JsonProperty("priceDescriptor.type")
    protected String priceDescriptorType;

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirection(String value) {
        this.direction = value;
    }

    /**
     * Gets the value of the priceDescriptorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceDescriptorType() {
        return priceDescriptorType;
    }

    /**
     * Sets the value of the priceDescriptorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceDescriptorType(String value) {
        this.priceDescriptorType = value;
    }

}
