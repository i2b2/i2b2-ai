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
package edu.harvard.i2b2.ai.ws;

import edu.harvard.i2b2.ai.datavo.i2b2message.BodyType;
import edu.harvard.i2b2.ai.datavo.wdo.DeleteDblookupType;
import edu.harvard.i2b2.common.exception.I2B2Exception;
import edu.harvard.i2b2.common.util.jaxb.JAXBUnWrapHelper;
import edu.harvard.i2b2.common.util.jaxb.JAXBUtilException;

public class DeleteDblookupDataMessage extends RequestDataMessage { //swc20160519

	//private static Log log = LogFactory.getLog(DeleteDblookupDataMessage.class);
    
	public DeleteDblookupDataMessage(String requestElementString) throws I2B2Exception {
        super.setRequestMessageType(requestElementString);
	}
    
    /**
     * Function to get DeleteDblookupType object from i2b2 request message type
     * @return
     * @throws JAXBUtilException
     */
    public DeleteDblookupType DeleteDblookupType() throws JAXBUtilException {
        BodyType bodyType = reqMessageType.getMessageBody();
        JAXBUnWrapHelper helper = new JAXBUnWrapHelper();
        DeleteDblookupType delDblookupType = (DeleteDblookupType) helper.getObjectByClass(bodyType.getAny(), DeleteDblookupType.class);        
        return delDblookupType;
    }

}
