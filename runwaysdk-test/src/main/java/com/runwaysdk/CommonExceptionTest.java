/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk;

import junit.framework.TestCase;

import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.session.Request;

public class CommonExceptionTest extends TestCase
{
	public CommonExceptionTest() {
		super();
	}
	
    public CommonExceptionTest( String testName )
    {
        super( testName );
    }

    public void testClient()
    {
      try {
    	CommonExceptionProcessor.processException(
    	          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), "Some message, doesn't matter.");
      }
      catch (ProgrammingErrorExceptionDTO err) {
        // Expected
      }
      catch (ProgrammingErrorException err) {
        fail("A server exception was thrown in a client environment.");
      }
    }
    
    @Request
    public void testServer() {
    	try {
        CommonExceptionProcessor.processException(
        	      ExceptionConstants.ProgrammingErrorException.getExceptionClass(), "Some message, doesn't matter.");
    	}
    	catch (ProgrammingErrorException e) {
          // Expected
    	}
    	catch (ProgrammingErrorExceptionDTO e) {
    		fail("A DTO exception was thrown within a request state.");
    	}
    }
}
