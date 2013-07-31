package com.runwaysdk;

import org.junit.Test;

import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;

public class ClientCommonExceptionTest {
	
	@Test
	public void testCommonExceptionProcessor() {
	try {
    	CommonExceptionProcessor.processException(
    	          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), "Some message, doesn't matter.");
      }
      catch (ProgrammingErrorExceptionDTO err) {
        // Expected
      }
	}
}
