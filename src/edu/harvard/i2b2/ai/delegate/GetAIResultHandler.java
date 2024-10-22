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
 * 		Lori Phillips
 */
package edu.harvard.i2b2.ai.delegate;

import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.JAXBElement;

import edu.harvard.i2b2.ai.dao.AIDao;
import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.i2b2message.RequestMessageType;
import edu.harvard.i2b2.ai.datavo.i2b2message.ResponseMessageType;
import edu.harvard.i2b2.ai.datavo.pm.ProjectType;
import edu.harvard.i2b2.ai.datavo.wdo.FolderType;
import edu.harvard.i2b2.ai.datavo.wdo.FoldersType;
import edu.harvard.i2b2.ai.datavo.wdo.GetQuestionType;
import edu.harvard.i2b2.ai.util.AIJAXBUtil;
import edu.harvard.i2b2.ai.ws.GetAIResultDataMessage;
import edu.harvard.i2b2.ai.ws.MessageFactory;
import edu.harvard.i2b2.common.exception.I2B2DAOException;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.ai.datavo.i2b2message.SecurityType;


public class GetAIResultHandler extends RequestHandler {
	private GetAIResultDataMessage  getResultMsg = null;
	private GetQuestionType getResultType = null;
	private String projectInfo = null;
	private SecurityType securityType = null;
private String text = null;
	public GetAIResultHandler(GetAIResultDataMessage requestMsg) throws I2B2Exception{

		getResultMsg = requestMsg;
		getResultType = requestMsg.getAiResultType();
		
		securityType = requestMsg.getMessageHeaderType().getSecurity();
		projectInfo = requestMsg.getMessageHeaderType().getProjectId();
		if (getResultType == null) 
		{
			getResultType = new GetQuestionType();
			String aieDataResponse = requestMsg.getXml();
			/*
			String unknownErrorMessage = "Error message delivered from the remote server \n" +  
					"You may wish to retry your last action";

			getResultType = new GetQuestionType();
			
			ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
					unknownErrorMessage);
			aieDataResponse = MessageFactory.convertToXMLString(responseMsgType);
			*/
			
			String result = aieDataResponse.substring(aieDataResponse.indexOf("<question>")+10, aieDataResponse.indexOf("</question>"));
			
			getResultType.setQuestion(result);
		}
		List<Object> text2 = requestMsg.getRequestMessageType().getMessageBody().getAny();
		text = requestMsg.getRequestMessageType().getMessageBody().toString();
	//	JAXBElement jaxbElement = WorkplaceJAXBUtil.getJAXBUtil().unMashallFromString(requestWdo);
	//	this.reqMessageType = (RequestMessageType) jaxbElement.getValue();
	//	projectInfo = getRoleInfo(requestMsg.getMessageHeaderType());	
	//	setDbInfo(requestMsg.getMessageHeaderType());
	}
	
	@Override
	public String execute() throws I2B2Exception{
		// call ejb and pass input object
		AIDao childDao = new AIDao();
		FoldersType folders = new FoldersType();
		ResponseMessageType responseMessageType = null;
		
		// check to see if we have projectInfo (if not indicates PM service problem)
	/*	if(projectInfo == null) {
			String response = null;
			responseMessageType = MessageFactory.doBuildErrorResponse(getResultMsg.getMessageHeaderType(), "User was not validated");
			response = MessageFactory.convertToXMLString(responseMessageType);
			log.debug("USER_INVALID or PM_SERVICE_PROBLEM");
			return response;	
		}
		*/
		
		String response = null;	
		try {
			response = childDao.getAIResult(getResultType, securityType, projectInfo, this.getDbInfo());
		} catch (Exception e1) {
			log.error(e1.getMessage());
			responseMessageType = MessageFactory.doBuildErrorResponse(getResultMsg.getMessageHeaderType(), "Database error");
		}

		// no errors found 
		if(responseMessageType == null) {
			// no db error but response is empty
			if (response == null) {
				log.debug("query results are empty");
				responseMessageType = MessageFactory.doBuildErrorResponse(getResultMsg.getMessageHeaderType(), "Query results are empty");
			}
			
//			 No errors, non-empty response received
			// If max is specified, check that response is not > max
			/*
			else if(getResultType.getMax() != null) {
				// if max exceeded send error message
				if(response.size() > getResultType.getMax()){
					log.debug("Max request size of " + getResultType.getMax() + " exceeded ");
					responseMessageType = MessageFactory.doBuildErrorResponse(getResultMsg.getMessageHeaderType(), "MAX_EXCEEDED");
				}
				// otherwise send results
				else {
					Iterator it = response.iterator();
					while (it.hasNext())
					{
						FolderType node = (FolderType)it.next();
						folders.getFolder().add(node);
					}
					// create ResponseMessageHeader using information from request message header.
					MessageHeaderType messageHeader = MessageFactory.createResponseMessageHeader(getResultMsg.getMessageHeaderType());          
					responseMessageType = MessageFactory.createBuildResponse(messageHeader,folders);
				}       
			}
			*/

			// max not specified so send results
			else {
				/*
				Iterator it = response.iterator();
				while (it.hasNext())
				{
					FolderType node = (FolderType)it.next();
					if (node.getProtectedAccess() == null)
						node.setProtectedAccess("N");
					folders.getFolder().add(node);
				}*/
				
				MessageHeaderType messageHeader = MessageFactory.createResponseMessageHeader(getResultMsg.getMessageHeaderType());          
				responseMessageType = MessageFactory.createNonStandardResponse(messageHeader, response); //.createBuildResponse(messageHeader,response);
				
			}     
		}
        String responseWdo = null;
       
		responseWdo = MessageFactory.convertToXMLString(responseMessageType);
		if(responseWdo == null)
			log.error("GetResult responseWdo is null");
		return responseWdo;
	}
    
}
