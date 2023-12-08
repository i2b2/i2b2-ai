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
import edu.harvard.i2b2.ai.datavo.wdo.DblookupType;
import edu.harvard.i2b2.ai.datavo.wdo.DeleteDblookupType;
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
import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import com.google.gson.Gson;

public class AIDao extends JdbcDaoSupport {

	private static Log log = LogFactory.getLog(AIDao.class);
	private static DataSource ds = null;
	private static JdbcTemplate jt;
	private static String dbluTable;
	private static String key = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ";
	private static String keyOrder = " LOWER(c_domain_id)=LOWER(?) AND (LOWER(c_owner_id)=LOWER(?) OR c_owner_id='@') ORDER BY c_project_path ";
	private String domainId = null;
	private String userId = null;

	public AIDao() {		
		//	initAIDao();
	} 

	public AIDao(MessageHeaderType reqMsgHdr) throws I2B2Exception, JAXBUtilException {
		domainId = reqMsgHdr.getSecurity().getDomain();
		userId = reqMsgHdr.getSecurity().getUsername();
		//	initAIDao();
	}

	private void initAIDao() {		
		try {
			ds = AIUtil.getInstance().getDataSource("java:/AIBootStrapDS");
		} catch (I2B2Exception e2) {
			log.error(e2.getMessage());;
		} 
		jt = new JdbcTemplate(ds);
		String metadataSchema = "";
		try {
			Connection conn = ds.getConnection();

			metadataSchema = conn.getSchema() + ".";
			conn.close();
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
		dbluTable = metadataSchema + "ai_db_lookup ";
		log.info("AI_DB_LOOKUP = " + dbluTable);
	} 

	public String slashEnd(String s) {
		StringBuffer sb = new StringBuffer(s);
		if (!s.endsWith("/")) {
			sb.append('/');
		}
		log.info(sb.toString());
		return sb.toString();
	}


	public int insertDblookup(final SetDblookupType dblookupType) throws DataAccessException, I2B2Exception {
		int numRowsAdded = 0;
		String sql = "INSERT INTO " + dbluTable +
				"(c_domain_id, c_project_path, c_owner_id, c_db_fullschema, c_db_datasource, c_db_servertype, c_db_nicename, c_db_tooltip, c_comment, c_entry_date, c_change_date, c_status_cd) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";		
		numRowsAdded = jt.update(sql, 
				dblookupType.getDomainId(),  
				slashEnd(dblookupType.getProjectPath()),
				dblookupType.getOwnerId(),
				dblookupType.getDbFullschema(),
				dblookupType.getDbDatasource(),
				dblookupType.getDbServertype(),
				dblookupType.getDbNicename(),
				dblookupType.getDbTooltip(),
				dblookupType.getComment(),
				Calendar.getInstance().getTime(),
				Calendar.getInstance().getTime(),
				dblookupType.getStatusCd()
				);
		log.info("insertDblookup - Number of rows added: " + numRowsAdded);
		return numRowsAdded;
	}

	public int updateDblookup(final SetDblookupType dblookupType) throws DataAccessException, I2B2Exception {
		int numRowsSet = 0;
		String sql = "UPDATE " + dbluTable +
				"SET c_db_fullschema=?, c_db_datasource=?, c_db_servertype=?, c_db_nicename=?, c_db_tooltip=?, c_comment=?, c_change_date=?, c_status_cd=? WHERE c_project_path=? AND " + 
				key;		
		numRowsSet = jt.update(sql, 
				dblookupType.getDbFullschema(),
				dblookupType.getDbDatasource(),
				dblookupType.getDbServertype(),
				dblookupType.getDbNicename(),
				dblookupType.getDbTooltip(),
				dblookupType.getComment(),
				Calendar.getInstance().getTime(),
				dblookupType.getStatusCd(),
				slashEnd(dblookupType.getProjectPath()),
				dblookupType.getDomainId(),  
				dblookupType.getOwnerId()
				);
		log.info("updateDblookup - Number of rows updated: " + numRowsSet);
		return numRowsSet;
	}

	public int deleteDblookup(final DeleteDblookupType dblookupType) throws DataAccessException, I2B2Exception {
		int numRowsDeleted = 0;
		String sql = "DELETE FROM " + dbluTable + " WHERE c_project_path=? AND " + key;		
		try {
			numRowsDeleted = jt.update(sql, slashEnd(dblookupType.getProjectPath()), dblookupType.getDomainId(), dblookupType.getOwnerId());
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new I2B2DAOException("Database error");
		}
		return numRowsDeleted;	
	}

	public String getAIResult(GetQuestionType getResultType, SecurityType securityType, String projectInfo, DBInfoType dbInfo) {
		// TODO Auto-generated method stub
		String str = "";

		// MM skip json api call

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		/* Skpped call */
		try {

			
//		    \"prompt\": \" Below is an instruction that describes a task. Write a response that appropriately completes the request.\n\n### Instruction:\n\nI want to find all patients with Diabetes over age of 65.\n\n### Response:\n```xml\n<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1//\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1//\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1//\">",

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/api/v1/generate");
			String JSON_STRING="{ \"prompt\": \" Below is an instruction that describes a task. Write a response that appropriately completes the request.\\n\\n### Instruction:\\n\\n"+ getResultType.getQuestion() + "\\n\\n### Response:\\n```xml\\n<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>\\n<ns4:query_definition xmlns:ns2=\\\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\\\" xmlns:ns4=\\\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\\\" xmlns:ns3=\\\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1/\\\">\"," +
					"\"instruction_template\": \"i2b2\"," +
					"\"mode\": \"instruct\"," +
					"\"max_new_tokens\": \"2048\"," +
					"\"preset\": \"My Preset\" }";
//					"\"preset\": \"Divine Intellect\" }";
			HttpEntity stringEntity = new StringEntity(JSON_STRING,ContentType.APPLICATION_JSON);
			// HttpEntity stringEntity = new StringEntity(JSON_STRING,ContentType.APPLICATION_JSON);
			httpPost.setEntity(stringEntity );
			CloseableHttpResponse response = httpclient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			str = EntityUtils.toString(entity);


			JsonElement jelement = new JsonParser().parse(str);
			JsonObject  jobject = jelement.getAsJsonObject();
			JsonArray jarray = jobject.getAsJsonArray("results");
			jobject = jarray.get(0).getAsJsonObject();
			str = jobject.get("text").getAsString();
			str = str.substring(1,str.length()-3);

			//str = jobject.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// @Deprecated httpClient.getConnectionManager().shutdown(); 
		}
		if (str.startsWith(">"))
			str = "<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1/\"" +
					str;
		else if ((!str.startsWith("<ns4:query_definition") && (!str.startsWith("<query_definition"))))
			str = "<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1/\">" +
					str;
		
		str ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n" + str;
			//	+ "<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1/\">\r\n";

	/* 
		str ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
				+ "<ns4:query_definition xmlns:ns2=\"http://www.i2b2.org/xsd/cell/crc/psm/1.1/\" xmlns:ns4=\"http://www.i2b2.org/xsd/cell/crc/psm/querydefinition/1.1/\" xmlns:ns3=\"http://www.i2b2.org/xsd/cell/crc/psm/analysisdefinition/1.1/\">\r\n"
				+ "    <query_name>Diabete-Essenti@17:29:14</query_name>\r\n"
				+ "    <query_timing>ANY</query_timing>\r\n"
				+ "    <specificity_scale>0</specificity_scale>\r\n"
				+ "    <panel>\r\n"
				+ "        <panel_number>1</panel_number>\r\n"
				+ "        <panel_timing>ANY</panel_timing>\r\n"
				+ "        <panel_accuracy_scale>100</panel_accuracy_scale>\r\n"
				+ "        <invert>0</invert>\r\n"
				+ "        <total_item_occurrences>1</total_item_occurrences>\r\n"
				+ "        <item>\r\n"
				+ "            <hlevel>2</hlevel>\r\n"
				+ "            <item_name>Diabetes mellitus (e08-e13)</item_name>\r\n"
				+ "            <item_key>\\\\ICD10_ICD9\\Diagnoses\\(E00-E89) Endo~c157\\(E08-E13) Diab~1uxc\\</item_key>\r\n"
				+ "            <item_icon>FA</item_icon>\r\n"
				+ "            <tooltip>Diagnoses \\ Endocrine, nutritional and metabolic diseases (e00-e89) \\ Diabetes mellitus (e08-e13)</tooltip>\r\n"
				+ "            <class>ENC</class>\r\n"
				+ "            <item_is_synonym>false</item_is_synonym>\r\n"
				+ "        </item>\r\n"
				+ "    </panel>\r\n"
				+ "    <panel>\r\n"
				+ "        <panel_number>2</panel_number>\r\n"
				+ "        <panel_timing>ANY</panel_timing>\r\n"
				+ "        <panel_accuracy_scale>100</panel_accuracy_scale>\r\n"
				+ "        <invert>0</invert>\r\n"
				+ "        <total_item_occurrences>1</total_item_occurrences>\r\n"
				+ "        <item>\r\n"
				+ "            <hlevel>3</hlevel>\r\n"
				+ "            <item_name>Essential (primary) hypertension</item_name>\r\n"
				+ "            <item_key>\\\\ICD10_ICD9\\Diagnoses\\(I00-I99) Dise~3wkq\\(I10-I15) Hype~56lq\\(I10) Essentia~tyxe\\</item_key>\r\n"
				+ "            <item_icon>LA</item_icon>\r\n"
				+ "            <tooltip>Diagnoses \\ Diseases of the circulatory system (i00-i99) \\ Hypertensive diseases (i10-i15) \\ Essential (primary) hypertension</tooltip>\r\n"
				+ "            <class>ENC</class>\r\n"
				+ "            <item_is_synonym>false</item_is_synonym>\r\n"
				+ "        </item>\r\n"
				+ "    </panel>\r\n"
				+ "    <panel>\r\n"
				+ "        <panel_number>3</panel_number>\r\n"
				+ "        <panel_timing>ANY</panel_timing>\r\n"
				+ "        <panel_accuracy_scale>100</panel_accuracy_scale>\r\n"
				+ "        <invert>0</invert>\r\n"
				+ "        <total_item_occurrences>1</total_item_occurrences>\r\n"
				+ "        <item>\r\n"
				+ "            <hlevel>3</hlevel>\r\n"
				+ "            <item_name>  65 years old</item_name>\r\n"
				+ "            <item_key>\\\\i2b2_DEMO\\i2b2\\Demographics\\Age\\&gt;= 65 years old\\65\\</item_key>\r\n"
				+ "            <item_icon>LA</item_icon>\r\n"
				+ "            <tooltip>Demographic \\ Age \\ &gt;= 65 years old \\ 65</tooltip>\r\n"
				+ "            <class>ENC</class>\r\n"
				+ "            <constrain_by_date>true</constrain_by_date>\r\n"
				+ "            <constrain_by_modifier>true</constrain_by_modifier>\r\n"
				+ "            <constrain_by_value>true</constrain_by_value>\r\n"
				+ "            <item_is_synonym>false</item_is_synonym>\r\n"
				+ "        </item>\r\n"
				+ "    </panel>\r\n"
				+ "</ns4:query_definition>\r\n";
		*/
		QueryDefinitionType queryFromAI = null;
		QueryDefinitionType query = new QueryDefinitionType();
		try {
			queryFromAI =  getQueryDefinitionType(str);
			
			query.setQueryName(queryFromAI.getQueryName());
			query.setSpecificityScale(0);
			query.setQueryTiming("ANY");
			
			List<PanelType> panelList = queryFromAI.getPanel();

			//for (PanelType panel: panelList)
			for (int i=0; i < panelList.size(); i++)
			{
				PanelType panel = panelList.get(i);
				
				PanelType newPanel = new PanelType();
				newPanel.setPanelNumber(i+1);
				newPanel.setPanelTiming("ANY");
				newPanel.setPanelAccuracyScale(100);
				newPanel.setInvert(0);
				newPanel.setTotalItemOccurrences(panel.getTotalItemOccurrences());
				
				List<ItemType> itemList = panel.getItem();
				for (ItemType item: itemList)
				{
					ItemType newItem = new ItemType();
					
					ConceptsType concepts = CallOntologyUtil.callOntology(item.getItemName(), "contains",  securityType, item.getItemKey().substring(2,item.getItemKey().indexOf('\\', 4)), projectInfo, "http://127.0.0.1:9090/i2b2/services/OntologyService/getNameInfo");
					
					if (concepts != null && concepts.getConcept() != null)
					{
						//newItem.setDimDimcode( concepts.getConcept().get(0).getDimcode());
						newItem.setHlevel( concepts.getConcept().get(0).getLevel());
						newItem.setItemName( concepts.getConcept().get(0).getName());
						newItem.setItemKey( concepts.getConcept().get(0).getKey());
						newItem.setItemIcon( concepts.getConcept().get(0).getVisualattributes());
						newItem.setTooltip( concepts.getConcept().get(0).getTooltip());
						newItem.setClazz( "ENC");
						newItem.setItemIsSynonym( false);
						log.info("Concept: " + concepts.getConcept().get(0).getName());
					}
					newPanel.getItem().add(newItem);
				}
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

