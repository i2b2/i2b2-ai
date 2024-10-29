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
 * 		Raj Kuttan
 * 		Lori Phillips
 */
package edu.harvard.i2b2.ai.ws;

import edu.harvard.i2b2.ai.datavo.i2b2message.ResponseMessageType;
import edu.harvard.i2b2.ai.delegate.GetAIResultHandler;
import edu.harvard.i2b2.ai.delegate.GetNameInfoHandler;
import edu.harvard.i2b2.ai.delegate.RequestHandler;
import edu.harvard.i2b2.ai.ws.DeleteDblookupDataMessage;
import edu.harvard.i2b2.ai.ws.ExecutorRunnable;
import edu.harvard.i2b2.ai.ws.GetAllDblookupsDataMessage;
import edu.harvard.i2b2.ai.ws.GetDblookupDataMessage;
import edu.harvard.i2b2.ai.ws.MessageFactory;
import edu.harvard.i2b2.ai.ws.SetDblookupDataMessage;
import edu.harvard.i2b2.common.exception.I2B2Exception;

import org.apache.axiom.om.OMElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import javax.xml.stream.XMLStreamException;


/**
 * This is webservice skeleton class. It parses incoming Workplace service requests
 * and  generates responses in the Work Data Object XML format.
 *
 */
public class AIService {
	private static Log log = LogFactory.getLog(AIService.class);
	protected final Log logesapi = LogFactory.getLog(getClass());

	/**
	 * This function is main webservice interface to get vocab data
	 * for a query. It uses AXIOM elements(OMElement) to conveniently parse
	 * xml messages.
	 *
	 * It excepts incoming request in i2b2 message format, which wraps a Workplace
	 * query inside a vocab query request object. The response is also will be in i2b2
	 * message format, which will wrap work data object. Work data object will
	 * have all the results returned by the query.
	 *
	 *
	 * @param getChildren
	 * @return OMElement in i2b2message format
	 * @throws Exception
	 */
	public OMElement getAi(OMElement getResultElement) 
			throws I2B2Exception {


		//    	OMElement returnElement = null;
		String aieDataResponse = null;
		String unknownErrorMessage = "Error message delivered from the remote server \n" +  
				"You may wish to retry your last action";

		if (getResultElement == null) {
			log.error("Incoming AI request is null");

			ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
					unknownErrorMessage);
			aieDataResponse = MessageFactory.convertToXMLString(responseMsgType);
			return MessageFactory.createResponseOMElementFromString(aieDataResponse);
		}

		GetAIResultDataMessage resultDataMsg = new GetAIResultDataMessage();
		String requestElementString = getResultElement.toString();
		resultDataMsg.setRequestMessageType(requestElementString);

		resultDataMsg.setXml(requestElementString);
		
		long waitTime = 0;
		if ((resultDataMsg.getRequestMessageType() != null) && (resultDataMsg.getRequestMessageType().getRequestHeader() != null)) {
			waitTime = resultDataMsg.getRequestMessageType()
					.getRequestHeader()
					.getResultWaittimeMs();
		}

		//do Workplace query processing inside thread, so that  
		// service could send back message with timeout error.     
		//     ExecutorRunnable er = new ExecutorRunnable();        
		return execute(new GetAIResultHandler(resultDataMsg), waitTime);

	}

	

	/**
	 *   
	 * This method is for finding the workplace item with the given keyword
	 * It uses AXIOM elements(OMElement) to conveniently parse
	 * xml messages.
	 *
	 * It excepts incoming request in i2b2 message format, which wraps an Workplace
	 * query inside a work query request object. The response is also will be in i2b2
	 * message format, which will wrap work data object. Work data object will
	 * have all the results returned by the query.
	 *

	 * @param requestElement
	 * @return
	 * @throws Exception
	 * 
	 * @author Neha Patel
	 */
	public OMElement getNameInfo(OMElement requestElement)
			throws Exception	{

		//OMElement requestElement = null;
		String workplaceDataResponse = null;
		String unknownErrorMessage = "Error message delivered from the remote server \n" +  
				"You may wish to retry your last action";

		if (requestElement == null) {
			log.error("Incoming Find Workplace request is null");

			ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
					unknownErrorMessage);
			workplaceDataResponse = MessageFactory.convertToXMLString(responseMsgType);
			return MessageFactory.createResponseOMElementFromString(workplaceDataResponse);
		}

		GetNameInfoDataMessage foldersDataMsg = new GetNameInfoDataMessage();
		String requestElementString = requestElement.toString();
		logesapi.debug(requestElementString);
		foldersDataMsg.setRequestMessageType(requestElementString);

		long waitTime = 0;
		if ((foldersDataMsg.getRequestMessageType() != null) && (foldersDataMsg.getRequestMessageType().getRequestHeader() != null)) {

			waitTime = foldersDataMsg.getRequestMessageType()
					.getRequestHeader()
					.getResultWaittimeMs();
		}

		return execute(new GetNameInfoHandler(foldersDataMsg), waitTime);  	
	}


	private OMElement execute(RequestHandler handler, long waitTime)throws I2B2Exception{
		//do workplace processing inside thread, so that  
		// service could send back message with timeout error.  
		log.debug("In execute");

		OMElement returnElement = null;

		String unknownErrorMessage = "Error message delivered from the remote server \n" +  
				"You may wish to retry your last action";


		ExecutorRunnable er = new ExecutorRunnable();        

		er.setRequestHandler(handler);

		Thread t = new Thread(er);
		String workplaceDataResponse = null;

		synchronized (t) {
			t.start();

			//        		try {
			//        			if (waitTime > 0) {
			//        				t.wait(waitTime);
			//        			} else {
			//        				t.wait();
			//        			}

			try {
				long startTime = System.currentTimeMillis();
				long deltaTime = -1;
				while((er.isJobCompleteFlag() == false)&& (deltaTime < waitTime)){
					if (waitTime > 0) {
						t.wait(waitTime - deltaTime);
						deltaTime = System.currentTimeMillis() - startTime;
					} else {
						t.wait();
					}
				}

				workplaceDataResponse = er.getOutputString();

				if (workplaceDataResponse == null) {
					if (er.getJobException() != null) {
						log.error("er.jobException is " + er.getJobException().getMessage());

						log.info("waitTime is " + waitTime);
						ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
								unknownErrorMessage);
						workplaceDataResponse = MessageFactory.convertToXMLString(responseMsgType);

					} else if (er.isJobCompleteFlag() == false) {
						//<result_waittime_ms>5000</result_waittime_ms>
						String timeOuterror = "Remote server timed out \n" +    		
								"Result waittime = " +
								waitTime +
								" ms elapsed,\nPlease try again";
						log.error(timeOuterror);
						log.debug("workplace waited " + deltaTime + "ms for " + er.getRequestHandler().getClass().getName());
						ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
								timeOuterror);
						workplaceDataResponse = MessageFactory.convertToXMLString(responseMsgType);

					} else {
						log.error("workplace  data response is null");
						log.info("waitTime is " + waitTime);
						log.debug("workplace waited " + deltaTime + "ms for " + er.getRequestHandler().getClass().getName());
						ResponseMessageType responseMsgType = MessageFactory.doBuildErrorResponse(null,
								unknownErrorMessage);
						workplaceDataResponse = MessageFactory.convertToXMLString(responseMsgType);
					}
				}
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				throw new I2B2Exception("Thread error while running Workplace job ");
			} finally {
				t.interrupt();
				er = null;
				t = null;
			}
		}
		returnElement = MessageFactory.createResponseOMElementFromString(workplaceDataResponse);

		return returnElement;
	}


}
