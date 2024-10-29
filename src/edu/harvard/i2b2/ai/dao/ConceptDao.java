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
import edu.harvard.i2b2.ai.datavo.i2b2message.BodyType;
import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.pm.ProjectType;
import edu.harvard.i2b2.ai.datavo.wdo.DblookupType;
import edu.harvard.i2b2.ai.datavo.wdo.DeleteDblookupType;
import edu.harvard.i2b2.ai.datavo.wdo.FindByChildType;
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
import edu.harvard.i2b2.ai.datavo.ontology.VocabRequestType;

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

public class ConceptDao extends JdbcDaoSupport {

	private static Log log = LogFactory.getLog(ConceptDao.class);
	private static String dbluTable;
	private static String key = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ";
	private static String keyOrder = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ORDER BY c_project_path ";
	private String domainId = null;
	private String userId = null;
	AIUtil aiutil = AIUtil.getInstance();


	public ConceptDao() {		
		//	initAIDao();
	} 

	public ConceptDao(MessageHeaderType reqMsgHdr) throws I2B2Exception, JAXBUtilException {
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



	public String getAIResult(VocabRequestType getResultType, SecurityType securityType, String projectInfo) {
		// TODO Auto-generated method stub
		String str = "";

		// MM skip json api call

		//CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		/* Skpped call */
		try {

			
//		    \"prompt\": \" Below is an instruction that describes a task. Write a response that appropriately completes the request.\n\n### Instruction:\n\nI want to find all patients with Diabetes over age of 65.\n\n### Response:\n```xml\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1//\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1//\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1//\">",

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(aiutil.getAIEndpointReference()); //"http://127.0.0.1:5000/v1/chat/completions");
			String JSON_STRING=aiutil.getOntologyJson();
			if (JSON_STRING.contains("{{{QUESTION}}}"))
				JSON_STRING = JSON_STRING.replaceAll("\\{\\{\\{QUESTION\\}\\}\\}", getResultType.getMatchStr().getValue());

					
					/*"{ \"messages\": [ { \"content\": \"Create a very short summary that uses 5 completion_tokens, What is just the medical term for "+ getResultType.getMatchStr().getValue() + "?\"," +
					"\"instruction_template\": \"Llama-v3\"," +
					"\"role\": \"user\","  +
					"\"preset\": \"Divine Intellect\""  +
					"} ], \"mode\": \"chat-instruct\" }";
					*/

		
		    HttpEntity stringEntity = new StringEntity(JSON_STRING,ContentType.APPLICATION_JSON);

			httpPost.setEntity(stringEntity );
			CloseableHttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			str = EntityUtils.toString(entity);
			/*
			str = "{\r\n"
					+ "    \"id\": \"chatcmpl-1729883329290381568\",\r\n"
					+ "    \"object\": \"chat.completion\",\r\n"
					+ "    \"created\": 1729883329,\r\n"
					+ "    \"model\": \"ruslanmv_Medical-Llama3-8B\",\r\n"
					+ "    \"choices\": [\r\n"
					+ "        {\r\n"
					+ "            \"index\": 0,\r\n"
					+ "            \"finish_reason\": \"stop\",\r\n"
					+ "            \"message\": {\r\n"
					+ "                \"role\": \"assistant\",\r\n"
					+ "                \"content\": \"```json { \\\"answer\\\": \\\"hypertension\\\" } ```\"\r\n"
					+ "            }\r\n"
					+ "        }\r\n"
					+ "    ],\r\n"
					+ "    \"usage\": {\r\n"
					+ "        \"prompt_tokens\": 61,\r\n"
					+ "        \"completion_tokens\": 14,\r\n"
					+ "        \"total_tokens\": 75\r\n"
					+ "    }\r\n"
					+ "}";
	*/
			log.info("From AI: " + str);
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
			str = str.substring(str.indexOf('{'),str.lastIndexOf('}')+1);
			jobject = new JsonParser().parse(str).getAsJsonObject(); //jarray.get(0).getAsJsonObject();
			str = jobject.get("answer").getAsString();
			//str = str.split("\n")[0];
			str = str.strip();
			if (str.endsWith("."))
				str = str.substring(0, str.length()-1);
			

			//str = jobject.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// @Deprecated httpClient.getConnectionManager().shutdown(); 
		}

		String response = null;
		try {
			
			
			getResultType.getMatchStr().setValue(str);
			 response = CallOntologyUtil.callOntology(getResultType, securityType,  projectInfo, aiutil.getOntEndpointReference() + "/OntologyService/getNameInfo");
					//.callSetfinderQuery(query,securityType, projectInfo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return response;
	}


}

