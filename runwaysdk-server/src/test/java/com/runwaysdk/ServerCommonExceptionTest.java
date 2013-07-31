package com.runwaysdk;

import org.junit.Test;

import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.session.Request;

public class ServerCommonExceptionTest {
	
	@Test
	public void testNotInRequest() {
	try {
    	CommonExceptionProcessor.processException(
    	          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), "Some message, doesn't matter.");
      }
      catch (ProgrammingErrorException err) {
        // Expected
      }
	}
	
	@Test
	@Request
	public void testInRequest() {
	try {
    	CommonExceptionProcessor.processException(
    	          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), "Some message, doesn't matter.");
      }
      catch (ProgrammingErrorException err) {
        // Expected
      }
	}
}
