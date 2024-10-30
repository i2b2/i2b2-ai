//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.09.15 at 01:59:06 PM EDT 
//


package edu.harvard.i2b2.ai.datavo.ontology;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for get_modifier_childrenType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="get_modifier_childrenType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applied_path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="applied_concept" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.i2b2.org/xsd/cell/ont/1.1/}return_attributeGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "get_modifier_childrenType", propOrder = {
    "parent",
    "appliedPath",
    "appliedConcept"
})
public class GetModifierChildrenType {

    @XmlElement(required = true)
    protected String parent;
    @XmlElement(name = "applied_path", required = true)
    protected String appliedPath;
    @XmlElement(name = "applied_concept", required = true)
    protected String appliedConcept;
    @XmlAttribute(name = "hiddens")
    protected Boolean hiddens;
    @XmlAttribute(name = "synonyms")
    protected Boolean synonyms;
    @XmlAttribute(name = "max")
    protected Integer max;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "blob")
    protected Boolean blob;

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParent(String value) {
        this.parent = value;
    }

    /**
     * Gets the value of the appliedPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppliedPath() {
        return appliedPath;
    }

    /**
     * Sets the value of the appliedPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppliedPath(String value) {
        this.appliedPath = value;
    }

    /**
     * Gets the value of the appliedConcept property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppliedConcept() {
        return appliedConcept;
    }

    /**
     * Sets the value of the appliedConcept property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppliedConcept(String value) {
        this.appliedConcept = value;
    }

    /**
     * Gets the value of the hiddens property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHiddens() {
        if (hiddens == null) {
            return false;
        } else {
            return hiddens;
        }
    }

    /**
     * Sets the value of the hiddens property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHiddens(Boolean value) {
        this.hiddens = value;
    }

    /**
     * Gets the value of the synonyms property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSynonyms() {
        if (synonyms == null) {
            return false;
        } else {
            return synonyms;
        }
    }

    /**
     * Sets the value of the synonyms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSynonyms(Boolean value) {
        this.synonyms = value;
    }

    /**
     * Gets the value of the max property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMax(Integer value) {
        this.max = value;
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
        if (type == null) {
            return "default";
        } else {
            return type;
        }
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
     * Gets the value of the blob property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isBlob() {
        if (blob == null) {
            return false;
        } else {
            return blob;
        }
    }

    /**
     * Sets the value of the blob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBlob(Boolean value) {
        this.blob = value;
    }

}
