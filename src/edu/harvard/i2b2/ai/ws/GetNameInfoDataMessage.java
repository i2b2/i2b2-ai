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
 * Creator:
 * 		Neha Patel
 */
package edu.harvard.i2b2.ai.ws;

import edu.harvard.i2b2.ai.datavo.i2b2message.BodyType;
import edu.harvard.i2b2.ai.datavo.ontology.VocabRequestType;
import edu.harvard.i2b2.ai.datavo.wdo.FindByChildType;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.common.util.jaxb.JAXBUnWrapHelper;
import edu.harvard.i2b2.common.util.jaxb.JAXBUtilException;


public class GetNameInfoDataMessage extends RequestDataMessage{
	
	public GetNameInfoDataMessage() throws I2B2Exception {
	}

	/**
	 * Function to get getNameInfoReturnType object from i2b2 request message type
	 * @return
	 * @throws JAXBUtilException
	 */

	public FindByChildType getFindByRequestType() throws JAXBUtilException {
		BodyType bodyType = reqMessageType.getMessageBody();
		JAXBUnWrapHelper helper = new JAXBUnWrapHelper();
		FindByChildType findByRequestType = (FindByChildType) helper.getObjectByClass(bodyType.getAny(),
				FindByChildType.class);        
		return findByRequestType;
	}

	public VocabRequestType getVocabRequestType() throws JAXBUtilException {
		BodyType bodyType = reqMessageType.getMessageBody();
		JAXBUnWrapHelper helper = new JAXBUnWrapHelper();
		VocabRequestType vocabRequestType = (VocabRequestType) helper.getObjectByClass(bodyType.getAny(),
				VocabRequestType.class);        
		return vocabRequestType;
	}

}
