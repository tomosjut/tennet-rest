
package com.faradaytrading.tennet.message.balancingmarket;

import com.faradaytrading.tennet.message.common.AreaIDString;
import com.faradaytrading.tennet.message.common.Reason;
import com.faradaytrading.tennet.message.balancingmarket.SeriesPeriod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema
public class TimeSeries {

    @NotNull
    @JsonProperty("mRID")
    protected String mrid;

    @NotNull
    @JsonProperty("businessType")
    protected String businessType;

    @JsonProperty("acquiring_Domain.mRID")
    protected AreaIDString acquiringDomainMRID;

    @JsonProperty("connecting_Domain.mRID")
    protected AreaIDString connectingDomainMRID;

    @JsonProperty("type_MarketAgreement.type")
    protected String typeMarketAgreementType;

    @JsonProperty("standard_MarketProduct.marketProductType")
    protected String standardMarketProductMarketProductType;

    @JsonProperty("original_MarketProduct.marketProductType")
    protected String originalMarketProductMarketProductType;

    @JsonProperty("mktPSRType.psrType")
    protected String mktPSRTypePsrType;

    @JsonProperty("flowDirection.direction")
    protected String flowDirectionDirection;

    @JsonProperty("currency_Unit.name")
    protected String currencyUnitName;

    @JsonProperty("quantity_Measurement_Unit.name")
    protected String quantityMeasurementUnitName;

    @JsonProperty("price_Measurement_Unit.name")
    protected String priceMeasurementUnitName;

    @JsonProperty("curveType")
    protected String curveType;

    @JsonProperty("cancelledTS")
    protected String cancelledTS;

    @JsonProperty("auction.mRID")
    protected String auctionMRID;

    @JsonProperty("Period")
    protected List<SeriesPeriod> periods;

    @JsonProperty("Reason")
    protected List<Reason> reasons;

    /**
     * Gets the value of the mrid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMRID() {
        return mrid;
    }

    /**
     * Sets the value of the mrid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMRID(String value) {
        this.mrid = value;
    }

    /**
     * Gets the value of the businessType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * Sets the value of the businessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessType(String value) {
        this.businessType = value;
    }


    /**
     * Gets the value of the acquiringDomainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getAcquiringDomainMRID() {
        return acquiringDomainMRID;
    }

    /**
     * Sets the value of the acquiringDomainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setAcquiringDomainMRID(AreaIDString value) {
        this.acquiringDomainMRID = value;
    }


    /**
     * Gets the value of the connectingDomainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getConnectingDomainMRID() {
        return connectingDomainMRID;
    }

    /**
     * Sets the value of the connectingDomainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setConnectingDomainMRID(AreaIDString value) {
        this.connectingDomainMRID = value;
    }


    /**
     * Gets the value of the typeMarketAgreementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeMarketAgreementType() {
        return typeMarketAgreementType;
    }

    /**
     * Sets the value of the typeMarketAgreementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeMarketAgreementType(String value) {
        this.typeMarketAgreementType = value;
    }


    /**
     * Gets the value of the standardMarketProductMarketProductType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardMarketProductMarketProductType() {
        return standardMarketProductMarketProductType;
    }

    /**
     * Sets the value of the standardMarketProductMarketProductType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardMarketProductMarketProductType(String value) {
        this.standardMarketProductMarketProductType = value;
    }


    /**
     * Gets the value of the originalMarketProductMarketProductType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalMarketProductMarketProductType() {
        return originalMarketProductMarketProductType;
    }

    /**
     * Sets the value of the originalMarketProductMarketProductType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalMarketProductMarketProductType(String value) {
        this.originalMarketProductMarketProductType = value;
    }


    /**
     * Gets the value of the mktPSRTypePsrType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMktPSRTypePsrType() {
        return mktPSRTypePsrType;
    }

    /**
     * Sets the value of the mktPSRTypePsrType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMktPSRTypePsrType(String value) {
        this.mktPSRTypePsrType = value;
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
     * Gets the value of the currencyUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyUnitName() {
        return currencyUnitName;
    }

    /**
     * Sets the value of the currencyUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyUnitName(String value) {
        this.currencyUnitName = value;
    }


    /**
     * Gets the value of the quantityMeasurementUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantityMeasurementUnitName() {
        return quantityMeasurementUnitName;
    }

    /**
     * Sets the value of the quantityMeasurementUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantityMeasurementUnitName(String value) {
        this.quantityMeasurementUnitName = value;
    }


    /**
     * Gets the value of the priceMeasurementUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceMeasurementUnitName() {
        return priceMeasurementUnitName;
    }

    /**
     * Sets the value of the priceMeasurementUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceMeasurementUnitName(String value) {
        this.priceMeasurementUnitName = value;
    }


    /**
     * Gets the value of the curveType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurveType() {
        return curveType;
    }

    /**
     * Sets the value of the curveType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurveType(String value) {
        this.curveType = value;
    }


    /**
     * Gets the value of the cancelledTS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelledTS() {
        return cancelledTS;
    }

    /**
     * Sets the value of the cancelledTS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelledTS(String value) {
        this.cancelledTS = value;
    }


    /**
     * Gets the value of the auctionMRID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuctionMRID() {
        return auctionMRID;
    }

    /**
     * Sets the value of the auctionMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuctionMRID(String value) {
        this.auctionMRID = value;
    }


    /**
     * Gets the value of the periods property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the periods property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPeriods().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SeriesPeriod }
     * 
     * 
     */
    public List<SeriesPeriod> getPeriods() {
        if (periods == null) {
            periods = new ArrayList<SeriesPeriod>();
        }
        return this.periods;
    }


    public void unsetPeriods() {
        this.periods = null;
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
