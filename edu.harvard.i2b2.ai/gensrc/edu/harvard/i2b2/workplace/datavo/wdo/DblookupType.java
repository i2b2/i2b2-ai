//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.17 at 02:58:01 PM EDT 
//


package edu.harvard.i2b2.workplace.datavo.wdo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dblookupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dblookupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="domain_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="owner_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="db_fullschema" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="db_datasource" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="db_servertype" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="db_nicename" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="db_tooltip" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="entry_date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="change_date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status_cd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="project_path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dblookupType", propOrder = {
    "domainId",
    "ownerId",
    "dbFullschema",
    "dbDatasource",
    "dbServertype",
    "dbNicename",
    "dbTooltip",
    "comment",
    "entryDate",
    "changeDate",
    "statusCd"
})
public class DblookupType {

    @XmlElement(name = "domain_id", required = true)
    protected String domainId;
    @XmlElement(name = "owner_id", required = true)
    protected String ownerId;
    @XmlElement(name = "db_fullschema", required = true)
    protected String dbFullschema;
    @XmlElement(name = "db_datasource", required = true)
    protected String dbDatasource;
    @XmlElement(name = "db_servertype", required = true)
    protected String dbServertype;
    @XmlElement(name = "db_nicename", required = true)
    protected String dbNicename;
    @XmlElement(name = "db_tooltip", required = true)
    protected String dbTooltip;
    @XmlElement(required = true)
    protected String comment;
    @XmlElement(name = "entry_date", required = true)
    protected String entryDate;
    @XmlElement(name = "change_date", required = true)
    protected String changeDate;
    @XmlElement(name = "status_cd", required = true)
    protected String statusCd;
    @XmlAttribute(name = "project_path")
    protected String projectPath;

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainId(String value) {
        this.domainId = value;
    }

    /**
     * Gets the value of the ownerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the value of the ownerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerId(String value) {
        this.ownerId = value;
    }

    /**
     * Gets the value of the dbFullschema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbFullschema() {
        return dbFullschema;
    }

    /**
     * Sets the value of the dbFullschema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbFullschema(String value) {
        this.dbFullschema = value;
    }

    /**
     * Gets the value of the dbDatasource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbDatasource() {
        return dbDatasource;
    }

    /**
     * Sets the value of the dbDatasource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbDatasource(String value) {
        this.dbDatasource = value;
    }

    /**
     * Gets the value of the dbServertype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbServertype() {
        return dbServertype;
    }

    /**
     * Sets the value of the dbServertype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbServertype(String value) {
        this.dbServertype = value;
    }

    /**
     * Gets the value of the dbNicename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbNicename() {
        return dbNicename;
    }

    /**
     * Sets the value of the dbNicename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbNicename(String value) {
        this.dbNicename = value;
    }

    /**
     * Gets the value of the dbTooltip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbTooltip() {
        return dbTooltip;
    }

    /**
     * Sets the value of the dbTooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbTooltip(String value) {
        this.dbTooltip = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the entryDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntryDate() {
        return entryDate;
    }

    /**
     * Sets the value of the entryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntryDate(String value) {
        this.entryDate = value;
    }

    /**
     * Gets the value of the changeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangeDate() {
        return changeDate;
    }

    /**
     * Sets the value of the changeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangeDate(String value) {
        this.changeDate = value;
    }

    /**
     * Gets the value of the statusCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCd() {
        return statusCd;
    }

    /**
     * Sets the value of the statusCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCd(String value) {
        this.statusCd = value;
    }

    /**
     * Gets the value of the projectPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectPath() {
        return projectPath;
    }

    /**
     * Sets the value of the projectPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectPath(String value) {
        this.projectPath = value;
    }

}
