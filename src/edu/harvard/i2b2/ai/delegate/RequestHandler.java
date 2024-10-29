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

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.harvard.i2b2.ai.datavo.i2b2message.MessageHeaderType;
import edu.harvard.i2b2.ai.datavo.i2b2message.SecurityType;
import edu.harvard.i2b2.ai.datavo.i2b2message.StatusType;
import edu.harvard.i2b2.ai.datavo.pm.CellDataType;
import edu.harvard.i2b2.ai.datavo.pm.ConfigureType;
import edu.harvard.i2b2.ai.datavo.pm.GetUserConfigurationType;
import edu.harvard.i2b2.ai.datavo.pm.ProjectType;
import edu.harvard.i2b2.ai.ejb.DBInfoType;
import edu.harvard.i2b2.ai.util.AIUtil;
import edu.harvard.i2b2.common.exception.I2B2Exception;

public abstract class RequestHandler {
    protected final Log log = LogFactory.getLog(getClass());
    public abstract String execute() throws I2B2Exception;
    private SecurityType securityType = null;
    
    public SecurityType getSecurityType() {
		return securityType;
	}

     
    


}
