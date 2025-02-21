
package com.faradaytrading.tennet.message.balancingmarket;

import com.faradaytrading.tennet.message.common.Reason;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema
public class Point {

    @NotNull
    @JsonProperty("position")
    protected int position;

    @JsonProperty("quantity")
    protected BigDecimal quantity;

    @JsonProperty("secondaryQuantity")
    protected BigDecimal secondaryQuantity;

    @JsonProperty("unavailable_Quantity.quantity")
    protected BigDecimal unavailableQuantityQuantity;

    @JsonProperty("activation_Price.amount")
    protected BigDecimal activationPriceAmount;

    @JsonProperty("procurement_Price.amount")
    protected BigDecimal procurementPriceAmount;

    @JsonProperty("min_Price.amount")
    protected BigDecimal minPriceAmount;

    @JsonProperty("max_Price.amount")
    protected BigDecimal maxPriceAmount;

    @JsonProperty("imbalance_Price.amount")
    protected BigDecimal imbalancePriceAmount;

    @JsonProperty("imbalance_Price.category")
    protected String imbalancePriceCategory;

    @JsonProperty("flowDirection.direction")
    protected String flowDirectionDirection;

    @JsonProperty("Financial_Price")
    protected List<FinancialPrice> financialPrices;

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
     * Gets the value of the secondaryQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSecondaryQuantity() {
        return secondaryQuantity;
    }

    /**
     * Sets the value of the secondaryQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSecondaryQuantity(BigDecimal value) {
        this.secondaryQuantity = value;
    }


    /**
     * Gets the value of the unavailableQuantityQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnavailableQuantityQuantity() {
        return unavailableQuantityQuantity;
    }

    /**
     * Sets the value of the unavailableQuantityQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnavailableQuantityQuantity(BigDecimal value) {
        this.unavailableQuantityQuantity = value;
    }


    /**
     * Gets the value of the activationPriceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getActivationPriceAmount() {
        return activationPriceAmount;
    }

    /**
     * Sets the value of the activationPriceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setActivationPriceAmount(BigDecimal value) {
        this.activationPriceAmount = value;
    }


    /**
     * Gets the value of the procurementPriceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProcurementPriceAmount() {
        return procurementPriceAmount;
    }

    /**
     * Sets the value of the procurementPriceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProcurementPriceAmount(BigDecimal value) {
        this.procurementPriceAmount = value;
    }


    /**
     * Gets the value of the minPriceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMinPriceAmount() {
        return minPriceAmount;
    }

    /**
     * Sets the value of the minPriceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMinPriceAmount(BigDecimal value) {
        this.minPriceAmount = value;
    }


    /**
     * Gets the value of the maxPriceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMaxPriceAmount() {
        return maxPriceAmount;
    }

    /**
     * Sets the value of the maxPriceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMaxPriceAmount(BigDecimal value) {
        this.maxPriceAmount = value;
    }


    /**
     * Gets the value of the imbalancePriceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImbalancePriceAmount() {
        return imbalancePriceAmount;
    }

    /**
     * Sets the value of the imbalancePriceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImbalancePriceAmount(BigDecimal value) {
        this.imbalancePriceAmount = value;
    }


    /**
     * Gets the value of the imbalancePriceCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImbalancePriceCategory() {
        return imbalancePriceCategory;
    }

    /**
     * Sets the value of the imbalancePriceCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImbalancePriceCategory(String value) {
        this.imbalancePriceCategory = value;
    }


    /**
     * Gets the value of the flowDirectionDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowDirectionDirection() {
        return flowDirectionDirection;
    }

    /**
     * Sets the value of the flowDirectionDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowDirectionDirection(String value) {
        this.flowDirectionDirection = value;
    }


    /**
     * Gets the value of the financialPrices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the financialPrices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFinancialPrices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link _351.iec62325.tc57wg16._451_6.balancingdocument._4._5.FinancialPrice }
     * 
     * 
     */
    public List<FinancialPrice> getFinancialPrices() {
        if (financialPrices == null) {
            financialPrices = new ArrayList<FinancialPrice>();
        }
        return this.financialPrices;
    }


    public void unsetFinancialPrices() {
        this.financialPrices = null;
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
