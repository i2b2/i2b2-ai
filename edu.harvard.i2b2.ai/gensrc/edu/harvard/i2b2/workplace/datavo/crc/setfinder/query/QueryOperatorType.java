//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.17 at 02:58:01 PM EDT 
//


package edu.harvard.i2b2.workplace.datavo.crc.setfinder.query;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for queryOperatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="queryOperatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EQUAL"/>
 *     &lt;enumeration value="GREATEREQUAL"/>
 *     &lt;enumeration value="GREATER"/>
 *     &lt;enumeration value="LESSEQUAL"/>
 *     &lt;enumeration value="LESS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "queryOperatorType", namespace = "http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/")
@XmlEnum
public enum QueryOperatorType {

    EQUAL,
    GREATEREQUAL,
    GREATER,
    LESSEQUAL,
    LESS;

    public String value() {
        return name();
    }

    public static QueryOperatorType fromValue(String v) {
        return valueOf(v);
    }

}
