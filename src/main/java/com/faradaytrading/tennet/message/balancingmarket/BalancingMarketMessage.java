
package com.faradaytrading.tennet.message.balancingmarket;

import com.faradaytrading.tennet.message.common.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.vertx.runtime.jackson.InstantDeserializer;
import io.quarkus.vertx.runtime.jackson.InstantSerializer;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Schema
public class BalancingMarketMessage {

    @NotNull
    @JsonProperty("mRID")
    protected String mrid;
    
    @NotNull
    @JsonProperty("revisionNumber")
    protected String revisionNumber;
    
    @NotNull
    @JsonProperty("type")
    protected String type;
    
    @NotNull
    @JsonProperty("process.processType")
    protected String processProcessType;
    
    @NotNull
    @JsonProperty("sender_MarketParticipant.mRID")
    protected PartyIDString senderMarketParticipantMRID;
    
    @NotNull
    @JsonProperty("sender_MarketParticipant.marketRole.type")
    protected String senderMarketParticipantMarketRoleType;
    
    @NotNull
    @JsonProperty("receiver_MarketParticipant.mRID")
    protected PartyIDString receiverMarketParticipantMRID;
    
    @NotNull
    @JsonProperty("receiver_MarketParticipant.marketRole.type")
    protected String receiverMarketParticipantMarketRoleType;
    
    @NotNull
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    protected Instant createdDateTime;
    
    @JsonProperty
    protected ActionStatus docStatus;
    
    @JsonProperty("area_Domain.mRID")
    protected AreaIDString areaDomainMRID;
    
    @JsonProperty("allocationDecision_DateAndOrTime.dateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    protected Instant allocationDecisionDateAndOrTimeDateTime;
    
    @NotNull
    @JsonProperty("period.timeInterval")
    protected ESMPDateTimeInterval periodTimeInterval;
    
    @JsonProperty("TimeSeries")
    protected List<TimeSeries> timeSeries;
    
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
     * Gets the value of the revisionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * Sets the value of the revisionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevisionNumber(String value) {
        this.revisionNumber = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the processProcessType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessProcessType() {
        return processProcessType;
    }

    /**
     * Sets the value of the processProcessType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessProcessType(String value) {
        this.processProcessType = value;
    }

    /**
     * Gets the value of the senderMarketParticipantMRID property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIDString }
     *     
     */
    public PartyIDString getSenderMarketParticipantMRID() {
        return senderMarketParticipantMRID;
    }

    /**
     * Sets the value of the senderMarketParticipantMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIDString }
     *     
     */
    public void setSenderMarketParticipantMRID(PartyIDString value) {
        this.senderMarketParticipantMRID = value;
    }

    /**
     * Gets the value of the senderMarketParticipantMarketRoleType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderMarketParticipantMarketRoleType() {
        return senderMarketParticipantMarketRoleType;
    }

    /**
     * Sets the value of the senderMarketParticipantMarketRoleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderMarketParticipantMarketRoleType(String value) {
        this.senderMarketParticipantMarketRoleType = value;
    }

    /**
     * Gets the value of the receiverMarketParticipantMRID property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIDString }
     *     
     */
    public PartyIDString getReceiverMarketParticipantMRID() {
        return receiverMarketParticipantMRID;
    }

    /**
     * Sets the value of the receiverMarketParticipantMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIDString }
     *     
     */
    public void setReceiverMarketParticipantMRID(PartyIDString value) {
        this.receiverMarketParticipantMRID = value;
    }

    /**
     * Gets the value of the receiverMarketParticipantMarketRoleType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverMarketParticipantMarketRoleType() {
        return receiverMarketParticipantMarketRoleType;
    }

    /**
     * Sets the value of the receiverMarketParticipantMarketRoleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverMarketParticipantMarketRoleType(String value) {
        this.receiverMarketParticipantMarketRoleType = value;
    }

    /**
     * Gets the value of the createdDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Instant getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Sets the value of the createdDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedDateTime(Instant value) {
        this.createdDateTime = value;
    }

    /**
     * Gets the value of the docStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ActionStatus }
     *     
     */
    public ActionStatus getDocStatus() {
        return docStatus;
    }

    /**
     * Sets the value of the docStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionStatus }
     *     
     */
    public void setDocStatus(ActionStatus value) {
        this.docStatus = value;
    }

    /**
     * Gets the value of the areaDomainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getAreaDomainMRID() {
        return areaDomainMRID;
    }

    /**
     * Sets the value of the areaDomainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setAreaDomainMRID(AreaIDString value) {
        this.areaDomainMRID = value;
    }

    /**
     * Gets the value of the allocationDecisionDateAndOrTimeDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Instant getAllocationDecisionDateAndOrTimeDateTime() {
        return allocationDecisionDateAndOrTimeDateTime;
    }

    /**
     * Sets the value of the allocationDecisionDateAndOrTimeDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllocationDecisionDateAndOrTimeDateTime(Instant value) {
        this.allocationDecisionDateAndOrTimeDateTime = value;
    }

    /**
     * Gets the value of the periodTimeInterval property.
     * 
     * @return
     *     possible object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public ESMPDateTimeInterval getPeriodTimeInterval() {
        return periodTimeInterval;
    }

    /**
     * Sets the value of the periodTimeInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public void setPeriodTimeInterval(ESMPDateTimeInterval value) {
        this.periodTimeInterval = value;
    }

    /**
     * Gets the value of the timeSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the timeSeries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TimeSeries }
     * 
     * 
     */
    public List<TimeSeries> getTimeSeries() {
        if (timeSeries == null) {
            timeSeries = new ArrayList<TimeSeries>();
        }
        return this.timeSeries;
    }

    public void unsetTimeSeries() {
        this.timeSeries = null;
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
