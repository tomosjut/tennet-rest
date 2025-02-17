
package com.faradaytrading.tennet.message.reportinginformation;

import com.faradaytrading.tennet.message.common.*;
import com.faradaytrading.tennet.message.schedulemarket.TimeSeries;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema
public class ReportingInformationMarketMessage {

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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    protected LocalDateTime createdDateTime;

    @NotNull
    @JsonProperty("time_Period.timeInterval")
    protected ESMPDateTimeInterval timePeriodTimeInterval;

    @JsonProperty("domain.mRID")
    protected AreaIDString domainMRID;

    @JsonProperty("dataset_MarketDocument.mRID")
    protected String datasetMarketDocumentMRID;

    @JsonProperty("dataset_MarketDocument.revisionNumber")
    protected String datasetMarketDocumentRevisionNumber;

    @JsonProperty
    protected ActionStatus docStatus;

    @JsonProperty("referenced_DateAndOrTime.date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    protected LocalDate referencedDateAndOrTimeDate;

    @JsonProperty("referenced_DateAndOrTime.time")
    @JsonFormat(pattern = "HH:mm:ss")
    protected XMLGregorianCalendar referencedDateAndOrTimeTime;

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

    public boolean isSetMRID() {
        return (this.mrid!= null);
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

    public boolean isSetRevisionNumber() {
        return (this.revisionNumber!= null);
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

    public boolean isSetType() {
        return (this.type!= null);
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

    public boolean isSetProcessProcessType() {
        return (this.processProcessType!= null);
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

    public boolean isSetSenderMarketParticipantMRID() {
        return (this.senderMarketParticipantMRID!= null);
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

    public boolean isSetSenderMarketParticipantMarketRoleType() {
        return (this.senderMarketParticipantMarketRoleType!= null);
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

    public boolean isSetReceiverMarketParticipantMRID() {
        return (this.receiverMarketParticipantMRID!= null);
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

    public boolean isSetReceiverMarketParticipantMarketRoleType() {
        return (this.receiverMarketParticipantMarketRoleType!= null);
    }

    /**
     * Gets the value of the createdDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public LocalDateTime getCreatedDateTime() {
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
    public void setCreatedDateTime(LocalDateTime value) {
        this.createdDateTime = value;
    }

    public boolean isSetCreatedDateTime() {
        return (this.createdDateTime!= null);
    }

    /**
     * Gets the value of the timePeriodTimeInterval property.
     * 
     * @return
     *     possible object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public ESMPDateTimeInterval getTimePeriodTimeInterval() {
        return timePeriodTimeInterval;
    }

    /**
     * Sets the value of the timePeriodTimeInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public void setTimePeriodTimeInterval(ESMPDateTimeInterval value) {
        this.timePeriodTimeInterval = value;
    }

    public boolean isSetTimePeriodTimeInterval() {
        return (this.timePeriodTimeInterval!= null);
    }

    /**
     * Gets the value of the domainMRID property.
     * 
     * @return
     *     possible object is
     *     {@link AreaIDString }
     *     
     */
    public AreaIDString getDomainMRID() {
        return domainMRID;
    }

    /**
     * Sets the value of the domainMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaIDString }
     *     
     */
    public void setDomainMRID(AreaIDString value) {
        this.domainMRID = value;
    }

    public boolean isSetDomainMRID() {
        return (this.domainMRID!= null);
    }

    /**
     * Gets the value of the datasetMarketDocumentMRID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatasetMarketDocumentMRID() {
        return datasetMarketDocumentMRID;
    }

    /**
     * Sets the value of the datasetMarketDocumentMRID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatasetMarketDocumentMRID(String value) {
        this.datasetMarketDocumentMRID = value;
    }

    public boolean isSetDatasetMarketDocumentMRID() {
        return (this.datasetMarketDocumentMRID!= null);
    }

    /**
     * Gets the value of the datasetMarketDocumentRevisionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatasetMarketDocumentRevisionNumber() {
        return datasetMarketDocumentRevisionNumber;
    }

    /**
     * Sets the value of the datasetMarketDocumentRevisionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatasetMarketDocumentRevisionNumber(String value) {
        this.datasetMarketDocumentRevisionNumber = value;
    }

    public boolean isSetDatasetMarketDocumentRevisionNumber() {
        return (this.datasetMarketDocumentRevisionNumber!= null);
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

    public boolean isSetDocStatus() {
        return (this.docStatus!= null);
    }

    /**
     * Gets the value of the referencedDateAndOrTimeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public LocalDate getReferencedDateAndOrTimeDate() {
        return referencedDateAndOrTimeDate;
    }

    /**
     * Sets the value of the referencedDateAndOrTimeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferencedDateAndOrTimeDate(LocalDate value) {
        this.referencedDateAndOrTimeDate = value;
    }

    public boolean isSetReferencedDateAndOrTimeDate() {
        return (this.referencedDateAndOrTimeDate!= null);
    }

    /**
     * Gets the value of the referencedDateAndOrTimeTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReferencedDateAndOrTimeTime() {
        return referencedDateAndOrTimeTime;
    }

    /**
     * Sets the value of the referencedDateAndOrTimeTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReferencedDateAndOrTimeTime(XMLGregorianCalendar value) {
        this.referencedDateAndOrTimeTime = value;
    }

    public boolean isSetReferencedDateAndOrTimeTime() {
        return (this.referencedDateAndOrTimeTime!= null);
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

    public boolean isSetTimeSeries() {
        return ((this.timeSeries!= null)&&(!this.timeSeries.isEmpty()));
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

    public boolean isSetReasons() {
        return ((this.reasons!= null)&&(!this.reasons.isEmpty()));
    }

    public void unsetReasons() {
        this.reasons = null;
    }

}
