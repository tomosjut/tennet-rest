
package com.faradaytrading.tennet.message.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.datatype.Duration;
import java.util.ArrayList;
import java.util.List;

@Schema
public class SeriesPeriod {

    @NotNull
    @JsonProperty("timeInterval")
    protected ESMPDateTimeInterval timeInterval;

    @NotNull
    @JsonProperty("resolution")
    protected Duration resolution;

    @NotNull
    @JsonProperty("Point")
    protected List<Point> points;

    /**
     * Gets the value of the timeInterval property.
     * 
     * @return
     *     possible object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public ESMPDateTimeInterval getTimeInterval() {
        return timeInterval;
    }

    /**
     * Sets the value of the timeInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESMPDateTimeInterval }
     *     
     */
    public void setTimeInterval(ESMPDateTimeInterval value) {
        this.timeInterval = value;
    }

    /**
     * Gets the value of the resolution property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getResolution() {
        return resolution;
    }

    /**
     * Sets the value of the resolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setResolution(Duration value) {
        this.resolution = value;
    }

    /**
     * Gets the value of the points property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the points property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Point }
     * 
     * 
     */
    public List<Point> getPoints() {
        if (points == null) {
            points = new ArrayList<Point>();
        }
        return this.points;
    }

    public void unsetPoints() {
        this.points = null;
    }

}
