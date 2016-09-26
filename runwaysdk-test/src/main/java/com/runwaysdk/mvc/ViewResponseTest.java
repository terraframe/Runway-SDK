package com.runwaysdk.mvc;

import java.io.File;

import org.junit.Assert;

import com.runwaysdk.controller.JSPFetcher;
import com.runwaysdk.controller.RequestManager;
import com.runwaysdk.controller.ServletMethod;
import com.runwaysdk.mvc.ViewResponse;
import com.runwaysdk.request.MockServletRequest;
import com.runwaysdk.request.MockServletResponse;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ViewResponseTest extends TestCase
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ViewResponseTest.class);

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

  public void testAttribute() throws Exception
  {
    String name = "test";
    Integer value = new Integer(45);

    ViewResponse response = new ViewResponse("template.jsp", "view.jsp");
    response.set(name, value);

    Assert.assertEquals(value, response.getAttribute(name));
  }

  public void testHandle() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String template = "template.jsp";
    String view = "view.jsp";

    ViewResponse response = new ViewResponse(template, view);
    response.handle(manager);

    Assert.assertEquals(view, req.getAttribute(JSPFetcher.INNER_JSP));
    Assert.assertEquals(template, req.getDispatcher().getLocation());
  }

  public void testHandleDirectory() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String template = "template.jsp";
    String directory = "test";
    String view = "view.jsp";

    ViewResponse response = new ViewResponse(template, directory, view);
    response.handle(manager);

    Assert.assertEquals(directory + File.separator + view, req.getAttribute(JSPFetcher.INNER_JSP));
    Assert.assertEquals(template, req.getDispatcher().getLocation());
  }

  public void testHandleAttributes() throws Exception
  {
    MockServletRequest req = new MockServletRequest();
    MockServletResponse resp = new MockServletResponse();
    RequestManager manager = new RequestManager(req, resp, ServletMethod.GET, null, null);

    String name = "test";
    Integer value = new Integer(45);

    ViewResponse response = new ViewResponse("template.jsp", "view.jsp");
    response.set(name, value);
    response.handle(manager);

    Assert.assertEquals(value, (Integer) req.getAttribute(name));
  }
}
