package com.runwaysdk.mvc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.controller.IllegalURIMethodException;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.controller.URLConfigurationManager;
import com.runwaysdk.controller.UnknownServletException;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;

public class DispatcherServletTest extends TestCase
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DispatcherServletTest.class);

    TestSetup wrapper = new TestSetup(suite);

    return wrapper;
  }

  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public void testCheckAndDispatch() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/testMethod");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      DispatcherServlet dispatcher = new DispatcherServlet(manager);
      dispatcher.checkAndDispatch(request);

      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );

      String result = new String(baos.toByteArray(), "UTF-8");

      Integer test = new Integer(result);

      Assert.assertEquals(new Integer(15), test);
      Assert.assertEquals(200, resp.getStatus());
      Assert.assertEquals("application/json", resp.getContentType());
    }
    finally
    {
      istream.close();
    }
  }

  public void testBasicParameter() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");
    
    Assert.assertNotNull(istream);
    
    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);
      
      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/number");
      req.setParameter("value", "100");
      
      MockServletResponse resp = new MockServletResponse();
      
      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);
      
      DispatcherServlet dispatcher = new DispatcherServlet(manager);
      dispatcher.checkAndDispatch(request);
      
      ByteArrayOutputStream baos = ( (ByteArrayOutputStream) resp.getOutputStream() );
      
      String result = new String(baos.toByteArray(), "UTF-8");
      
      Integer test = new Integer(result);
      
      Assert.assertEquals(new Integer(100), test);
      Assert.assertEquals(200, resp.getStatus());
      Assert.assertEquals("application/json", resp.getContentType());
    }
    finally
    {
      istream.close();
    }
  }
  
  public void testInvalidUri() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/bad");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      try
      {
        DispatcherServlet dispatcher = new DispatcherServlet(manager);
        dispatcher.checkAndDispatch(request);

        Assert.fail();
      }
      catch (UnknownServletException e)
      {
        // This is expected
      }
    }
    finally
    {
      istream.close();
    }
  }

  public void testGetOnPost() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      MockServletRequest req = new MockServletRequest();
      req.setServletPath("test/generate");

      MockServletResponse resp = new MockServletResponse();

      RequestManager request = new RequestManager(req, resp, ServletMethod.GET, null, null);

      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      try
      {
        DispatcherServlet dispatcher = new DispatcherServlet(manager);
        dispatcher.checkAndDispatch(request);

        Assert.fail();
      }
      catch (IllegalURIMethodException e)
      {
        // This is expected
      }
    }
    finally
    {
      istream.close();
    }
  }
  
}