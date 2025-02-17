
package com.faradaytrading.tennet.message.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema
public class ConfirmedTimeSeries {

    @NotNull
    @JsonProperty("mRID")
    protected String mrid;

    @NotNull
    @JsonProperty("version")
    protected String version;

    @NotNull
    @JsonProperty("businessType")
    protected String businessType;

    @NotNull
    @JsonProperty("product")
    protected String product;

    @NotNull
    @JsonProperty("objectAggregation")
    protected String objectAggregation;

    @JsonProperty("in_Domain.mRID")
    protected AreaIDString inDomainMRID;

    @JsonProperty("out_Domain.mRID")
    protected AreaIDString outDomainMRID;

    @JsonProperty("marketEvaluationPoint.mRID")
    protected MeasurementPointIDString marketEvaluationPointMRID;

    @JsonProperty("in_MarketParticipant.mRID")
    protected PartyIDString inMarketParticipantMRID;

    @JsonProperty("out_MarketParticipant.mRID")
    protected PartyIDString outMarketParticipantMRID;

    @JsonProperty("marketAgreement.type")
    protected String marketAgreementType;

    @JsonProperty("marketAgreement.mRID")
    protected String marketAgreementMRID;

    @NotNull
    @JsonProperty("measure_Unit.name")
    protected String measureUnitName;

    @JsonProperty("curveType")
    protected String curveType;

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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
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
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduct(String value) {
        this.product = value;
    }

    /**
     * Gets the value of the objectAggregation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectAggregation() {
        return objectAggregation;
    }

    /**
     * Sets the value of the objectAggregation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectAggregation(String value) {
        this.objectAggregation = value;
    }

    /**
     * Gets the value of the inDomainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getInDomainMRID() {
        return inDomainMRID;
    }

    /**
     * Sets the value of the inDomainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setInDomainMRID(AreaIDString value) {
        this.inDomainMRID = value;
    }

    /**
     * Gets the value of the outDomainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getOutDomainMRID() {
        return outDomainMRID;
    }

    /**
     * Sets the value of the outDomainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setOutDomainMRID(AreaIDString value) {
        this.outDomainMRID = value;
    }


    /**
     * Gets the value of the marketEvaluationPointMRID property.
     * 
     * @return
     *     possible object is
     *     {@link MeasurementPointIDString }
     *     
     */
    public MeasurementPointIDString getMarketEvaluationPointMRID() {
        return marketEvaluationPointMRID;
    }

    /**
     * Sets the value of the marketEvaluationPointMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasurementPointIDString }
     *     
     */
    public void setMarketEvaluationPointMRID(MeasurementPointIDString value) {
        this.marketEvaluationPointMRID = value;
    }


    /**
     * Gets the value of the inMarketParticipantMRID property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIDString }
     *     
     */
    public PartyIDString getInMarketParticipantMRID() {
        return inMarketParticipantMRID;
    }

    /**
     * Sets the value of the inMarketParticipantMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIDString }
     *     
     */
    public void setInMarketParticipantMRID(PartyIDString value) {
        this.inMarketParticipantMRID = value;
    }


    /**
     * Gets the value of the outMarketParticipantMRID property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIDString }
     *     
     */
    public PartyIDString getOutMarketParticipantMRID() {
        return outMarketParticipantMRID;
    }

    /**
     * Sets the value of the outMarketParticipantMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIDString }
     *     
     */
    public void setOutMarketParticipantMRID(PartyIDString value) {
        this.outMarketParticipantMRID = value;
    }


    /**
     * Gets the value of the marketAgreementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarketAgreementType() {
        return marketAgreementType;
    }

    /**
     * Sets the value of the marketAgreementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarketAgreementType(String value) {
        this.marketAgreementType = value;
    }


    /**
     * Gets the value of the marketAgreementMRID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarketAgreementMRID() {
        return marketAgreementMRID;
    }

    /**
     * Sets the value of the marketAgreementMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarketAgreementMRID(String value) {
        this.marketAgreementMRID = value;
    }


    /**
     * Gets the value of the measureUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeasureUnitName() {
        return measureUnitName;
    }

    /**
     * Sets the value of the measureUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeasureUnitName(String value) {
        this.measureUnitName = value;
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
