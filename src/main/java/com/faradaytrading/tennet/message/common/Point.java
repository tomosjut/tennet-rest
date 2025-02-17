
package com.faradaytrading.tennet.message.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema
public class Point {

    @JsonProperty("position")
    protected int position;

    @NotNull
    @JsonProperty("quantity")
    protected BigDecimal quantity;

    @JsonProperty("posFR_Quantity.quantity")
    protected BigDecimal posFRQuantityQuantity;

    @JsonProperty("negFR_Quantity.quantity")
    protected BigDecimal negFRQuantityQuantity;

    @JsonProperty("Reason")
    protected List<Reason> reasons;

    /**
     * Gets the value of the position property.
     * 
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     */
    public void setPosition(int value) {
        this.position = value;
    }


    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }


    /**
     * Gets the value of the posFRQuantityQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPosFRQuantityQuantity() {
        return posFRQuantityQuantity;
    }

    /**
     * Sets the value of the posFRQuantityQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPosFRQuantityQuantity(BigDecimal value) {
        this.posFRQuantityQuantity = value;
    }


    /**
     * Gets the value of the negFRQuantityQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNegFRQuantityQuantity() {
        return negFRQuantityQuantity;
    }

    /**
     * Sets the value of the negFRQuantityQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNegFRQuantityQuantity(BigDecimal value) {
        this.negFRQuantityQuantity = value;
    }


    /**
     * Gets the value of the reasons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the reasons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReasons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reason }
     * 
     * 
     */
    public List<Reason> getReasons() {
        if (reasons == null) {
            reasons = new ArrayList<Reason>();
        }
        return this.reasons;
    }


    public void unsetReasons() {
        this.reasons = null;
    }

}
