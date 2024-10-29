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
 *                 Raj Kuttan
 *                 Lori Phillips
 */
package edu.harvard.i2b2.ai.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.pm.ParamType;
import edu.harvard.i2b2.common.exception.I2B2DAOException;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.common.util.ServiceLocator;
import edu.harvard.i2b2.common.util.axis2.ServiceClient;
import edu.harvard.i2b2.common.util.jaxb.DTOFactory;
import edu.harvard.i2b2.ai.datavo.i2b2message.ApplicationType;

/**
 * This is the Workplace service's main utility class
 * This utility class provides support for
 * fetching resources like datasouce, to read application
 * properties, to get ejb home,etc.
 * $Id: WorkplaceUtil.java,v 1.5 2008/03/13 14:32:32 lcp5 Exp $
 * @author rkuttan
 */
public class AIUtil {

	/** property name for CRC endpoint reference **/
	private static final String CRC_WS_EPR = "ai.ws.crc.url";

	/** property name for ONT endpoint reference **/
	private static final String ONT_WS_EPR = "ai.ws.ont.url";

	/** property name for AI endpoint reference **/
	private static final String AI_WS_EPR = "ai.ws.ai.url";

	/** property name for AI question json reference **/
	private static final String AI_JSON_QUESTION = "ai.json.question";

	/** property name for AI chat json reference **/
	private static final String AI_JSON_CHAT = "ai.json.chat";

	/** property name for AI ontology json reference **/
	private static final String AI_JSON_ONTOLOGY = "ai.json.ontology";


	/** class instance field**/
	private static AIUtil thisInstance = null;

	/** service locator field**/
	private static ServiceLocator serviceLocator = null;

	/** field to store application properties **/
	private static List<ParamType> appProperties = null;

	/** log **/
	protected final Log log = LogFactory.getLog(getClass());

	/** field to store app datasource**/
	private DataSource dataSource = null;

	/** single instance of spring bean factory**/
	private BeanFactory beanFactory = null;

	/**
	 * Private constructor to make the class singleton
	 */
	private AIUtil() {
	}

	/**
	 * Return this class instance
	 * @return OntologyUtil
	 */
	public static AIUtil getInstance() {
		if (thisInstance == null) {
			thisInstance = new AIUtil();
		}

		serviceLocator = ServiceLocator.getInstance();

		return thisInstance;
	}


	/**
	 * Return ONT cell endpoint reference URL
	 * @return
	 * @throws I2B2Exception
	 */
	public String getOntEndpointReference() throws I2B2Exception {
		return getPropertyValue(ONT_WS_EPR).trim();
	}

	/**
	 * Return CRC cell endpoint reference URL
	 * @return
	 * @throws I2B2Exception
	 */
	public String getAIEndpointReference() throws I2B2Exception {
		return getPropertyValue(AI_WS_EPR).trim();
	}

	/**
	 * Return CRC cell endpoint reference URL
	 * @return
	 * @throws I2B2Exception
	 */
	public String getChatJson() throws I2B2Exception {
		return getPropertyValue(AI_JSON_CHAT).trim();
	}

	/**
	 * Return CRC cell endpoint reference URL
	 * @return
	 * @throws I2B2Exception
	 */
	public String getOntologyJson() throws I2B2Exception {
		return getPropertyValue(AI_JSON_ONTOLOGY).trim();
	}

	/**
	 * Return CRC cell endpoint reference URL
	 * @return
	 * @throws I2B2Exception
	 */
	public String getQuestionJson() throws I2B2Exception {
		return getPropertyValue(AI_JSON_QUESTION).trim();
	}


	/**
	 * Return app server datasource
	 * @return datasource
	 * @throws I2B2Exception
	 */
	public DataSource getDataSource(String dataSourceName) throws I2B2Exception {    	
		dataSource = serviceLocator
				.getAppServerDataSource(dataSourceName);
		return dataSource;

	}

	//---------------------
	// private methods here
	//---------------------


	/**
	 * Load application property file into memory
	 */
	private String getPropertyValue(String propertyName)
			throws I2B2Exception {


		if (appProperties == null) {



			//		log.info(sql + domainId + projectId + ownerId);
			//	List<ParamType> queryResult = null;
			try {
				DataSource   ds = this.getDataSource("java:/WorkplaceBootStrapDS");

				JdbcTemplate jt = new JdbcTemplate(ds);
				Connection conn = ds.getConnection();

				String metadataSchema = conn.getSchema();
				conn.close();
				String sql =  "select * from " + metadataSchema + ".hive_cell_params where status_cd <> 'D' and cell_id = 'AI'";

				log.debug("Start query");
				appProperties =  jt.query(sql, new getHiveCellParam());
				log.debug("End query");


			} catch (DataAccessException e) {
				log.error(e.getMessage());
				e.printStackTrace();
				throw new I2B2DAOException("Database error");
			}
			//return queryResult;	
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		String propertyValue = null;//appProperties.getProperty(propertyName);
		for (int i=0; i < appProperties.size(); i++)
		{
			if (appProperties.get(i).getName() != null)
			{
				if (appProperties.get(i).getName().equalsIgnoreCase(propertyName))
					if (appProperties.get(i).getDatatype().equalsIgnoreCase("U"))
						try {
							propertyValue = ServiceClient.getContextRoot() + appProperties.get(i).getValue();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else 
						propertyValue = appProperties.get(i).getValue();
			}
		}

		if ((propertyValue == null) || (propertyValue.trim().length() == 0)) {
			throw new I2B2Exception("Application property file("
					//	+ APPLICATION_PROPERTIES_FILENAME + ") missing "
					+ propertyName + " entry");
		}

		return propertyValue;

	}

	public static MessageHeaderType getMessageHeader()  {
		MessageHeaderType messageHeader = new MessageHeaderType();
		ApplicationType appType = new ApplicationType();
		try {
			appType.setApplicationName("AI"); //getPropertyValue("applicationName"));
			appType.setApplicationVersion("1.8"); //getPropertyValue("applicationVersion"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageHeader.setSendingApplication(appType);
		return messageHeader;
	}}


class getHiveCellParam implements RowMapper<ParamType> {
	@Override
	public ParamType mapRow(ResultSet rs, int rowNum) throws SQLException {

		ParamType param = new ParamType();
		param.setId(rs.getInt("id"));
		param.setName(rs.getString("param_name_cd"));
		param.setValue(rs.getString("value"));
		param.setDatatype(rs.getString("datatype_cd"));
		return param;
	} 
}
