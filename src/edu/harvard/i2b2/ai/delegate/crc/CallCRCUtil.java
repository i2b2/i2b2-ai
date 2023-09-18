/*******************************************************************************
 * Copyright (c) 2006-2018 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. I2b2 is also distributed under
 * the terms of the Healthcare Disclaimer.
 ******************************************************************************/
package edu.harvard.i2b2.ai.delegate.crc;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import edu.harvard.i2b2.common.util.axis2.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Logger;

import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.CrcXmlResultResponseType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.MasterRequestType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.MasterResponseType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.PsmQryHeaderType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.PsmRequestTypeType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ResultRequestType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.UserType;
import edu.harvard.i2b2.ai.datavo.i2b2message.ApplicationType;
import edu.harvard.i2b2.ai.datavo.i2b2message.BodyType;
import edu.harvard.i2b2.ai.datavo.i2b2message.FacilityType;
import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.i2b2message.RequestHeaderType;
import edu.harvard.i2b2.ai.datavo.i2b2message.RequestMessageType;
import edu.harvard.i2b2.ai.datavo.i2b2message.ResponseMessageType;
import edu.harvard.i2b2.ai.datavo.i2b2message.ResultStatusType;
import edu.harvard.i2b2.ai.datavo.i2b2message.SecurityType;
import edu.harvard.i2b2.ai.util.AIJAXBUtil;
import edu.harvard.i2b2.ai.util.AIUtil;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.common.util.jaxb.JAXBUnWrapHelper;
import edu.harvard.i2b2.common.util.jaxb.JAXBUtilException;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.MasterInstanceResultResponseType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ItemType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.PanelType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryDefinitionRequestType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryDefinitionType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryModeType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ResultOutputOptionListType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ResultOutputOptionType;

public class CallCRCUtil {

	//	private SecurityType securityType = null;
	//	private String projectId = null;
	//private String crcUrl = null;
	static AIUtil workplaceUtil = AIUtil.getInstance();
	static ServiceClient serviceClient = null;
	private static Log log = LogFactory.getLog(CallCRCUtil.class);
	protected static Logger logesapi = ESAPI.getLogger(CallCRCUtil.class);

	
	private static String crcUrl = "http://127.0.0.1:9090/i2b2/services/QueryToolService";

	
	/*
	public CallCRCUtil(SecurityType securityType, String projectId)
			throws I2B2Exception {
		this.securityType = securityType;
		this.projectId = projectId;
		this.crcUrl = workplaceUtil.getCRCUrl();
		log.debug("CRC Workplace call url: " + crcUrl);
	}

	public CallCRCUtil(String crcUrl, SecurityType securityType,
			String projectId) throws I2B2Exception {
		this.securityType = securityType;
		this.projectId = projectId;
		this.crcUrl = crcUrl;
	}
	 */

	public static MasterInstanceResultResponseType callSetfinderQuery(QueryDefinitionType queryDef,  SecurityType securityType,  String projectId) throws I2B2Exception {
		RequestMessageType requestMessageType = buildSetfinderQueryRequestMessage(queryDef, securityType, projectId);
		MasterInstanceResultResponseType masterInstanceResultResponseType = null;
		try {
			OMElement requestElement = buildOMElement(requestMessageType);
			log.info("AI CRC call's request xml "
					+ requestElement);
			String response = ServiceClient.sendREST(crcUrl + "/request", requestElement);
			logesapi.debug(null,"AI CRC call's reponse xml " + response);
			masterInstanceResultResponseType = getMasterInstanceResultResponseMessage(response);
		} catch (JAXBUtilException jaxbEx) {
			throw new I2B2Exception("Error in CRC setfinder ", jaxbEx);
		} catch (XMLStreamException e) {
			throw new I2B2Exception("Error in CRC setfinder ", e);

		} catch (Exception e) {

			throw new I2B2Exception("Error in CRC setfinder ", e);
		}	
		return masterInstanceResultResponseType;
	}
	
	
	
	public static RequestMessageType buildSetfinderQueryRequestMessage(QueryDefinitionType queryDef,  SecurityType securityType,  String projectId) {
		QueryDefinitionRequestType queryDefinitionRequestType = new QueryDefinitionRequestType();
		ResultOutputOptionListType resultOutputOptionListType = new ResultOutputOptionListType();
		ResultOutputOptionType resultOutputOptionType = new ResultOutputOptionType();
		resultOutputOptionType.setName("patient_count_xml");
		resultOutputOptionType.setFullName("PATIENT_COUNT_XML");
		resultOutputOptionType.setPriorityIndex(9);
		resultOutputOptionListType.getResultOutput().add(resultOutputOptionType);
		queryDefinitionRequestType.setQueryDefinition(queryDef);
		queryDefinitionRequestType.setResultOutputList(resultOutputOptionListType);

		MessageHeaderType messageHeaderType =   AIUtil.getMessageHeader();
		messageHeaderType.setSecurity(securityType);
		messageHeaderType.setProjectId(projectId);

		messageHeaderType.setReceivingApplication(messageHeaderType
				.getSendingApplication());
		FacilityType facilityType = new FacilityType();
		facilityType.setFacilityName("sample");
		messageHeaderType.setSendingFacility(facilityType);
		messageHeaderType.setReceivingFacility(facilityType);

		RequestMessageType requestMessageType = new RequestMessageType();
		edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory of = new edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory();
		BodyType bodyType = new BodyType();
		PsmQryHeaderType psm = new PsmQryHeaderType();
		psm.setRequestType(PsmRequestTypeType.CRC_QRY_RUN_QUERY_INSTANCE_FROM_QUERY_DEFINITION);
		psm.setQueryMode(QueryModeType.OPTIMIZE_WITHOUT_TEMP_TABLE);
		UserType userType = new UserType();
		userType.setGroup(projectId);
		userType.setLogin(securityType.getUsername());
		userType.setValue(securityType.getUsername());
		psm.setUser(userType);
		bodyType.getAny().add(of.createPsmheader(psm));
		bodyType.getAny().add(of.createRequest(queryDefinitionRequestType));
		requestMessageType.setMessageBody(bodyType);

		requestMessageType.setMessageHeader(messageHeaderType);

		RequestHeaderType requestHeader = new RequestHeaderType();
		requestHeader.setResultWaittimeMs(180000); //3000);
		requestMessageType.setRequestHeader(requestHeader);
		return requestMessageType;
	}
	
	
	
	
	
	/*
	
	public static String callCRCResultInstanceXML(String resultInstanceID, SecurityType securityType,  String projectId)
			throws I2B2Exception {
		//		ResultResponseType resultResponseType = null;
		String response = null;
		try {
			log.debug("begin build element");
			RequestMessageType requestMessageType = buildResultInstanceRequestXMLRequestMessage(resultInstanceID, securityType, projectId);
			OMElement requestElement = buildOMElement(requestMessageType);
			logesapi.debug(null,"callCRCQueryRequestXML - CRC setfinder query request XML call's request xml "
					+ requestElement);
			response = getServiceClient("/request", requestElement).toString();
			logesapi.debug(null,"callCRCQueryRequestXML - CRC setfinder query request XML call's response xml " + response.toString());
			//resultResponseType = getResultResponseMessage(response.toString());
			//masterInstanceResultResponseType = getResponseMessage(response
			//		.toString());

		} catch (JAXBUtilException jaxbEx) {
			log.error(jaxbEx.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", jaxbEx);
		} catch (XMLStreamException e) {
			log.error(e.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", e);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", e);
		} finally {
			if (serviceClient != null) {
				try{
					serviceClient.cleanupTransport();
					serviceClient.cleanup();
				} catch (AxisFault e) {
					log.debug("Error .", e);
				}
			}
		}			

		return response;
	}
=	
	public static String callCRCQueryRequestXML(String queryMasterId, SecurityType securityType,  String projectId)
			throws I2B2Exception {
		//		ResultResponseType resultResponseType = null;
		//MasterResponseType masterInstanceResultResponseType = null;
		String response = null;
		try {
			log.debug("begin build element");
			RequestMessageType requestMessageType = buildSetfinderRequestXMLRequestMessage(queryMasterId, securityType, projectId);
			OMElement requestElement = buildOMElement(requestMessageType);
			log.debug("callCRCQueryRequestXML - CRC setfinder query request XML call's request xml "
					+ requestElement);
			response = getServiceClient("/request", requestElement).toString();

			//log.debug("callCRCQueryRequestXML - CRC setfinder query request XML call's response xml " + response.toString());
			//resultResponseType = getResultResponseMessage(response.toString());
			//masterInstanceResultResponseType = getMasterInstanceResultResponseMessage(response
			//		.toString());

		} catch (JAXBUtilException jaxbEx) {
			log.error(jaxbEx.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", jaxbEx);
		} catch (XMLStreamException e) {
			log.error(e.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", e);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new I2B2Exception("Error in CRC upload ", e);
		} finally {
			if (serviceClient != null) {
				try{
					serviceClient.cleanupTransport();
					serviceClient.cleanup();
				} catch (AxisFault e) {
					log.debug("Error .", e);
				}
			}
		}	
		return response;
	}

*/
	//	public RequestMessageType buildSetfinderStatusRequestMessage(String queryInstanceId) {
	public static RequestMessageType buildResultInstanceRequestXMLRequestMessage(String resultInstanceId, SecurityType securityType,  String projectId) {
		ResultRequestType masterRequestType = new ResultRequestType();
		masterRequestType.setQueryResultInstanceId(resultInstanceId);

		MessageHeaderType messageHeaderType = new MessageHeaderType();
		ApplicationType appType = new ApplicationType();
		appType.setApplicationName("Workplace Cell");
		appType.setApplicationVersion("1.701");
		messageHeaderType.setSendingApplication(appType);

		messageHeaderType.setSecurity(securityType);
		messageHeaderType.setProjectId(projectId);

		messageHeaderType.setReceivingApplication(messageHeaderType
				.getSendingApplication());
		FacilityType facilityType = new FacilityType();
		facilityType.setFacilityName("sample");
		messageHeaderType.setSendingFacility(facilityType);
		messageHeaderType.setReceivingFacility(facilityType);

		RequestMessageType requestMessageType = new RequestMessageType();

		edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory of = new edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory();
		BodyType bodyType = new BodyType();
		PsmQryHeaderType psm = new PsmQryHeaderType();
		psm.setRequestType(PsmRequestTypeType.CRC_QRY_GET_RESULT_DOCUMENT_FROM_RESULT_INSTANCE_ID); //.CRC_QRY_GET_QUERY_RESULT_INSTANCE_LIST_FROM_QUERY_INSTANCE_ID);
		bodyType.getAny().add(of.createPsmheader(psm));

		bodyType.getAny().add(of.createRequest(masterRequestType));
		requestMessageType.setMessageBody(bodyType);

		requestMessageType.setMessageHeader(messageHeaderType);

		RequestHeaderType requestHeader = new RequestHeaderType();
		requestHeader.setResultWaittimeMs(3000);
		requestMessageType.setRequestHeader(requestHeader);
		return requestMessageType;
	}

	public static RequestMessageType buildSetfinderRequestXMLRequestMessage(String queryMasterId, SecurityType securityType,  String projectId) {
		MasterRequestType masterRequestType = new MasterRequestType();
		masterRequestType.setQueryMasterId(queryMasterId);

		MessageHeaderType messageHeaderType = new MessageHeaderType();
		ApplicationType appType = new ApplicationType();
		appType.setApplicationName("Workplace Cell");
		appType.setApplicationVersion("1.701");
		messageHeaderType.setSendingApplication(appType);

		messageHeaderType.setSecurity(securityType);
		messageHeaderType.setProjectId(projectId);

		messageHeaderType.setReceivingApplication(messageHeaderType
				.getSendingApplication());
		FacilityType facilityType = new FacilityType();
		facilityType.setFacilityName("sample");
		messageHeaderType.setSendingFacility(facilityType);
		messageHeaderType.setReceivingFacility(facilityType);

		RequestMessageType requestMessageType = new RequestMessageType();

		edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory of = new edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ObjectFactory();
		BodyType bodyType = new BodyType();
		PsmQryHeaderType psm = new PsmQryHeaderType();
		psm.setRequestType(PsmRequestTypeType.CRC_QRY_GET_REQUEST_XML_FROM_QUERY_MASTER_ID); //.CRC_QRY_GET_QUERY_RESULT_INSTANCE_LIST_FROM_QUERY_INSTANCE_ID);
		bodyType.getAny().add(of.createPsmheader(psm));

		bodyType.getAny().add(of.createRequest(masterRequestType));
		requestMessageType.setMessageBody(bodyType);

		requestMessageType.setMessageHeader(messageHeaderType);

		RequestHeaderType requestHeader = new RequestHeaderType();
		requestHeader.setResultWaittimeMs(3000);
		requestMessageType.setRequestHeader(requestHeader);
		return requestMessageType;
	}

	private static CrcXmlResultResponseType getResponseMessage(
			String responseXml) throws JAXBUtilException, I2B2Exception {
		JAXBElement responseJaxb = AIJAXBUtil.getJAXBUtil()
				.unMashallFromString(responseXml);
		ResponseMessageType r = (ResponseMessageType) responseJaxb.getValue();
		log.debug("CRC's workplace call response xml" + responseXml);

		JAXBUnWrapHelper helper = new JAXBUnWrapHelper();
		ResultStatusType rt = r.getResponseHeader().getResultStatus();
		if (rt.getStatus().getType().equals("ERROR")) {
			throw new I2B2Exception(rt.getStatus().getValue());
		}
		CrcXmlResultResponseType masterInstanceResultResponseType = (CrcXmlResultResponseType) helper
				.getObjectByClass(r.getMessageBody().getAny(),
						CrcXmlResultResponseType.class);
		log.debug("got CrcXmlResultResponseType: " + masterInstanceResultResponseType);
		return masterInstanceResultResponseType;
	}

	private static MasterInstanceResultResponseType getMasterInstanceResultResponseMessage(
			String responseXml) throws JAXBUtilException, I2B2Exception {
		JAXBElement responseJaxb = AIJAXBUtil.getJAXBUtil()
				.unMashallFromString(responseXml);
		ResponseMessageType r = (ResponseMessageType) responseJaxb.getValue();
		log.debug("CRC's workplace call response xml" + responseXml);

		JAXBUnWrapHelper helper = new JAXBUnWrapHelper();
		ResultStatusType rt = r.getResponseHeader().getResultStatus();
		if (rt.getStatus().getType().equals("ERROR")) {
			throw new I2B2Exception(rt.getStatus().getValue());
		}
		MasterInstanceResultResponseType masterInstanceResultResponseType = (MasterInstanceResultResponseType) helper
				.getObjectByClass(r.getMessageBody().getAny(),
						MasterInstanceResultResponseType.class);
		log.debug("got MasterInstanceResultResponseType");
		return masterInstanceResultResponseType;
	}



	private static OMElement buildOMElement(RequestMessageType requestMessageType)
			throws XMLStreamException, JAXBUtilException {
		StringWriter strWriter = new StringWriter();
		edu.harvard.i2b2.ai.datavo.i2b2message.ObjectFactory hiveof = new edu.harvard.i2b2.ai.datavo.i2b2message.ObjectFactory();
		AIJAXBUtil.getJAXBUtil().marshaller(
				hiveof.createRequest(requestMessageType), strWriter);
		// getOMElement from message
		OMFactory fac = OMAbstractFactory.getOMFactory();

		StringReader strReader = new StringReader(strWriter.toString());
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader reader = xif.createXMLStreamReader(strReader);
		OMElement request = OMXMLBuilderFactory.createStAXOMBuilder(reader).getDocumentElement();

		return request;
	}

	/*
	private static OMElement getServiceClient(String operationName, OMElement request) throws AxisFault, I2B2Exception {
		// call
		OMElement response = null;

		serviceClient = CRCServiceClient.getServiceClient();

		ServiceContext context = serviceClient.getServiceContext();
		MultiThreadedHttpConnectionManager connManager = (MultiThreadedHttpConnectionManager)context.getProperty(HTTPConstants.MULTITHREAD_HTTP_CONNECTION_MANAGER);

		if(connManager == null) {
			connManager = new MultiThreadedHttpConnectionManager();
			context.setProperty(HTTPConstants.MULTITHREAD_HTTP_CONNECTION_MANAGER, connManager);
			connManager.getParams().setMaxTotalConnections(100);
			connManager.getParams().setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, 100);
		}
		HttpClient httpClient = new HttpClient(connManager);


		Options options = new Options();
		options.setTo(new EndpointReference(workplaceUtil.getCRCUrl() + operationName));
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setProperty(Constants.Configuration.ENABLE_REST,
				Constants.VALUE_TRUE);
		options.setProperty(HTTPConstants.CACHED_HTTP_CLIENT,
				httpClient);	
		options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, Constants.VALUE_TRUE);
		//		options.setProperty(HTTPConstants.AUTO_RELEASE_CONNECTION, true);
		serviceClient.setOptions(options);
		response = serviceClient.sendReceive(request);


		return response;

	}
*/
}
