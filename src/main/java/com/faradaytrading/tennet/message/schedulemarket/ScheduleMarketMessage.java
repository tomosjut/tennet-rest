
package com.faradaytrading.tennet.message.schedulemarket;

import com.faradaytrading.tennet.message.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.smallrye.common.constraint.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema
public class ScheduleMarketMessage {

    @NotNull
    @JsonProperty("mRID")
    protected String mrid;

    @NotNull
    @JsonProperty("revisionNumber")
    protected String revisionNumber;

    @NotNull
    @JsonProperty("sender_MarketParticipant.mRID")
    protected PartyIDString senderMarketParticipantMRID;

    @NotNull
    @JsonProperty("receiver_MarketParticipant.mRID")
    protected PartyIDString receiverMarketParticipantMRID;

//LocalDate.now() is that okay?
//    @NotNull
//    @JsonProperty
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    protected LocalDateTime createdDateTime;

    @NotNull
    @JsonProperty("schedule_Time_Period.timeInterval")
    protected ESMPDateTimeInterval scheduleTimePeriodTimeInterval;

    @NotNull
    @JsonProperty("domainMRID")
    protected AreaIDString domainMRID;

    @JsonProperty("TimeSeries")
    protected List<TimeSeries> timeSeries;

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
     * Gets the value of the scheduleTimePeriodTimeInterval property.
     * 
     * @return
     *     possible object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public ESMPDateTimeInterval getScheduleTimePeriodTimeInterval() {
        return scheduleTimePeriodTimeInterval;
    }

    /**
     * Sets the value of the scheduleTimePeriodTimeInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public void setScheduleTimePeriodTimeInterval(ESMPDateTimeInterval value) {
        this.scheduleTimePeriodTimeInterval = value;
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

    /**
     * Gets the value of the timeSeries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore, any modification you make to the
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

}
