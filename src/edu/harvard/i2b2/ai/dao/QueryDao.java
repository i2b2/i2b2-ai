/*******************************************************************************
 * Copyright (c) 2006-2018 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. I2b2 is also distributed under
 * the terms of the Healthcare Disclaimer.
 ******************************************************************************/
/*

 * 
 * Contributors:
 * 		Wayne Chan
 */
package edu.harvard.i2b2.ai.dao;

import edu.harvard.i2b2.ai.datavo.i2b2message.RequestMessageType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.ItemType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.MasterInstanceResultResponseType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.PanelType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryDefinitionRequestType;
import edu.harvard.i2b2.ai.datavo.crc.setfinder.query.QueryDefinitionType;
import edu.harvard.i2b2.ai.datavo.i2b2message.BodyType;
import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.pm.ProjectType;
import edu.harvard.i2b2.ai.datavo.wdo.GetQuestionType;
import edu.harvard.i2b2.ai.datavo.wdo.SetDblookupType;
import edu.harvard.i2b2.ai.delegate.crc.CallCRCUtil;
import edu.harvard.i2b2.ai.delegate.ontology.CallOntologyUtil;
import edu.harvard.i2b2.ai.ejb.DBInfoType;
import edu.harvard.i2b2.ai.util.AIJAXBUtil;
import edu.harvard.i2b2.ai.util.AIUtil;
import edu.harvard.i2b2.common.exception.I2B2DAOException;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.common.util.jaxb.JAXBUnWrapHelper;
import edu.harvard.i2b2.common.util.jaxb.JAXBUtilException;
import edu.harvard.i2b2.ai.datavo.i2b2message.SecurityType;
import edu.harvard.i2b2.ai.datavo.ontology.ConceptsType;
import edu.harvard.i2b2.ai.datavo.ontology.ConceptType;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.sql.DataSource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
 */
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpPost;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
//import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import com.google.gson.Gson;

public class QueryDao extends JdbcDaoSupport {

	private static Log log = LogFactory.getLog(QueryDao.class);
	private static DataSource ds = null;
	private static JdbcTemplate jt;
	private static String dbluTable;
	private static String key = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ";
	private static String keyOrder = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ORDER BY c_project_path ";
	private String domainId = null;
	private String userId = null;
	AIUtil aiutil = AIUtil.getInstance();


	public QueryDao() {		
		//	initAIDao();
	} 

	public QueryDao(MessageHeaderType reqMsgHdr) throws I2B2Exception, JAXBUtilException {
		domainId = reqMsgHdr.getSecurity().getDomain();
		userId = reqMsgHdr.getSecurity().getUsername();
		//	initAIDao();
	}



	public String slashEnd(String s) {
		StringBuffer sb = new StringBuffer(s);
		if (!s.endsWith("/")) {
			sb.append('/');
		}
		log.info(sb.toString());
		return sb.toString();
	}


	public String getQueryResult(GetQuestionType getResultType, SecurityType securityType, String projectInfo) {
		// TODO Auto-generated method stub
		String str = "";



		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		try {



			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(aiutil.getAIEndpointReference()); //"http://127.0.0.1:5000/v1/chat/completions");
			String JSON_STRING=aiutil.getQuestionJson();
			if (JSON_STRING.contains("{{{QUESTION}}}"))
				JSON_STRING = JSON_STRING.replaceAll("\\{\\{\\{QUESTION\\}\\}\\}", getResultType.getQuestion());


			HttpEntity stringEntity = new StringEntity(JSON_STRING,ContentType.APPLICATION_JSON);

			httpPost.setEntity(stringEntity );
			CloseableHttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			str = EntityUtils.toString(entity);


			JsonElement jelement = new JsonParser().parse(str);
			JsonObject  jobject = jelement.getAsJsonObject();
			//JsonElement c = jobject.get("choices");


			JsonObject jObj=(JsonObject)jobject.get("choices").getAsJsonArray().get(0);
			//JsonElement msg = jObj.get("message");
			JsonElement jObj2 = jObj.get("message");//.get(0);
			jobject =jObj2.getAsJsonObject();
			//System.out.println(jObj2.get("content"));

			//JsonElement d = jobject.get("choices").getAsJsonObject().get("message");
			str = jobject.get("content").getAsString(); //.getAsJsonArray("content");
			log.info("initial AI response: " + str);
			if (str.endsWith("\""))
				str = str.substring(str.indexOf('{')) + "}";
			else
				str = str.substring(str.indexOf('{'),str.lastIndexOf('}')+1);
			jobject = new JsonParser().parse(str).getAsJsonObject(); //jarray.get(0).getAsJsonObject();
			str = jobject.get("answer").getAsString();
			//str = str.split("\n")[0];
			str = str.strip();
			if (str.endsWith("."))
				str = str.substring(0, str.length()-1);

			log.info("Cleaned AI response: " + str );
			//str = jobject.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// @Deprecated httpClient.getConnectionManager().shutdown(); 
		}
		
		//QueryDefinitionType queryFromAI = null;
		QueryDefinitionType query = new QueryDefinitionType();
		try {
			String[] terms = str.toLowerCase().split("and");

			query.setQueryName(str);
			query.setSpecificityScale(0);
			query.setQueryTiming("ANY");

			//List<PanelType> panelList = queryFromAI.getPanel();

			//for (PanelType panel: panelList)
			//for (int i=0; i < panelList.size(); i++)
			for (int i=0; i < terms.length; i++)
			{
				PanelType panel = new PanelType();

				PanelType newPanel = new PanelType();
				newPanel.setPanelNumber(i+1);
				newPanel.setPanelTiming("ANY");
				newPanel.setPanelAccuracyScale(100);
				newPanel.setInvert(0);
				newPanel.setTotalItemOccurrences(panel.getTotalItemOccurrences());

				//List<ItemType> itemList = panel.getItem();

				ItemType newItem = new ItemType();
				
				terms[i] = terms[i].trim();
				terms[i] = terms[i].replaceAll("_", " ");

				ConceptsType concepts = CallOntologyUtil.callOntology(terms[i], "contains",  securityType, "@", projectInfo, "http://127.0.0.1:9090/i2b2/services/OntologyService/getNameInfo");

				if (concepts != null && concepts.getConcept() != null)
				{
					//newItem.setDimDimcode( concepts.getConcept().get(0).getDimcode());
					for (ConceptType concept : concepts.getConcept()) {
						newItem.setHlevel( concept.getLevel());
						newItem.setItemName( concept.getName());
						newItem.setItemKey( concept.getKey());
						newItem.setItemIcon( concept.getVisualattributes());
						newItem.setTooltip( concept.getTooltip());
						newItem.setClazz( "ENC");
						newItem.setItemIsSynonym( false);
						log.info("Concept: " + concept.getName());
						if (concept.getTotalnum() != null && concept.getTotalnum() > 0)
							newPanel.getItem().add(newItem);
					}
				}
				
				if (newPanel.getItem().size() > 0)
					query.getPanel().add(newPanel);
			}


			MasterInstanceResultResponseType response = CallCRCUtil.callSetfinderQuery(query,securityType, projectInfo);
			if (response != null)
				str = response.getQueryMaster().getQueryMasterId();
			else
				str = "-1";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return str;
	}

	private QueryDefinitionType  getQueryDefinitionType(String requestString) throws I2B2DAOException { 
		QueryDefinitionType queryDefReqType = null;
		JAXBElement responseJaxb;
		try {
			responseJaxb = AIJAXBUtil.getJAXBUtil()
					.unMashallFromString(requestString);

			queryDefReqType = (QueryDefinitionType) responseJaxb.getValue();
			//BodyType bodyType = r.getMessageBody();
			// 	get body and search for analysis definition
			JAXBUnWrapHelper unWraphHelper = new JAXBUnWrapHelper();


			//queryDefReqType = (QueryDefinitionRequestType) unWraphHelper
			//		.getObjectByClass(bodyType.getAny(),
			//				QueryDefinitionRequestType.class);
		} catch (JAXBUtilException e) {
			throw new I2B2DAOException(e.getMessage());
		}

		return queryDefReqType;
	}
}

