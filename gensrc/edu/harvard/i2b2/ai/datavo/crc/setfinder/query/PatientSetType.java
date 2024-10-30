//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.09.15 at 01:59:06 PM EDT 
//


package edu.harvard.i2b2.ai.datavo.crc.setfinder.query;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for patient_setType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patient_setType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cohort_patient" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="set_index" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}set_indexType"/>
 *                   &lt;element name="patient_id" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}patient_numType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="patient_set_coll_id" use="required" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}patient_set_coll_idType" />
 *       &lt;attribute name="result_instance_id" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}result_instance_idType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "patient_setType", propOrder = {
    "cohortPatient"
})
public class PatientSetType {

    @XmlElement(name = "cohort_patient", required = true)
    protected List<PatientSetType.CohortPatient> cohortPatient;
    @XmlAttribute(name = "patient_set_coll_id", required = true)
    protected String patientSetCollId;
    @XmlAttribute(name = "result_instance_id")
    protected String resultInstanceId;

    /**
     * Gets the value of the cohortPatient property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cohortPatient property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCohortPatient().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PatientSetType.CohortPatient }
     * 
     * 
     */
    public List<PatientSetType.CohortPatient> getCohortPatient() {
        if (cohortPatient == null) {
            cohortPatient = new ArrayList<PatientSetType.CohortPatient>();
        }
        return this.cohortPatient;
    }

    /**
     * Gets the value of the patientSetCollId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatientSetCollId() {
        return patientSetCollId;
    }

    /**
     * Sets the value of the patientSetCollId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatientSetCollId(String value) {
        this.patientSetCollId = value;
    }

    /**
     * Gets the value of the resultInstanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultInstanceId() {
        return resultInstanceId;
    }

    /**
     * Sets the value of the resultInstanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultInstanceId(String value) {
        this.resultInstanceId = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="set_index" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}set_indexType"/>
     *         &lt;element name="patient_id" type="{http://www.i2b2.org/xsd/cell/crc/psm/1.1/}patient_numType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "setIndex",
        "patientId"
    })
    public static class CohortPatient {

        @XmlElement(name = "set_index")
        protected int setIndex;
        @XmlElement(name = "patient_id", required = true)
        protected String patientId;

        /**
         * Gets the value of the setIndex property.
         * 
         */
        public int getSetIndex() {
            return setIndex;
        }

        /**
         * Sets the value of the setIndex property.
         * 
         */
        public void setSetIndex(int value) {
            this.setIndex = value;
        }

        /**
         * Gets the value of the patientId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPatientId() {
            return patientId;
        }

        /**
         * Sets the value of the patientId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPatientId(String value) {
            this.patientId = value;
        }

    }

}
