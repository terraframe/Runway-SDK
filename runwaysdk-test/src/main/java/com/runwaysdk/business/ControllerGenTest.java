package com.runwaysdk.business;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.catalina.core.DummyRequest;
import org.apache.catalina.core.DummyResponse;
import org.apache.catalina.session.StandardSession;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.controller.IllegalURIMethodException;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.UnknownServletException;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;

public class ControllerGenTest extends TestCase
{

  class MockSession extends StandardSession
  {
    /**
     *
     */
    private static final long       serialVersionUID = 3674944194141854328L;

    private HashMap<String, Object> attributes;

    public MockSession()
    {
      super(null);
      this.attributes = new HashMap<String, Object>();
    }

    public Object getAttribute(String arg0)
    {
      return attributes.get(arg0);
    }

    public void setAttribute(String arg0, Object arg1)
    {
      attributes.put(arg0, arg1);
    }
  }

  class MockRequest extends DummyRequest
  {
    private String                    method;

    private HashMap<String, String[]> parameters;

    private HashMap<String, Object>   attributes;

    // private HashMap<String, String[]> parameterValues;

    private MockSession               session;

    public MockRequest(String method, String uri)
    {
      this.method = method;
      this.parameters = new HashMap<String, String[]>();
      // this.parameterValues = new HashMap<String, String[]>();
      this.attributes = new HashMap<String, Object>();
      this.session = new MockSession();

      String[] split = uri.split("/");

      this.setRequestURI(uri);
      this.setContextPath("/" + split[1]);
      this.setServletPath("/" + split[2]);
    }

    @Override
    public Map<?, ?> getParameterMap()
    {
      return parameters;
    }

    @Override
    public String getMethod()
    {
      return method;
    }

    @Override
    public String getParameter(String name)
    {
      String[] params = parameters.get(name);
      if (params != null && params.length > 0)
      {
        return params[0];
      }
      else
      {
        return null;
      }
    }

    public void setParameter(String name, String value)
    {
      parameters.put(name, new String[] { value });
    }

    @Override
    public String[] getParameterValues(String name)
    {
      return parameters.get(name);
      // return parameterValues.get(name);
    }

    public void setParameterValues(String name, String[] values)
    {
      parameters.put(name, values);
      // parameterValues.put(name, values);
    }

    @Override
    public Locale getLocale()
    {
      return CommonProperties.getDefaultLocale();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames()
    {
      StringBuffer buffer = new StringBuffer();

      for (String key : parameters.keySet())
      {
        buffer.append(" " + key);
      }

      StringTokenizer tok = new StringTokenizer(buffer.toString().replaceFirst(" ", ""));

      return tok;
    }

    @Override
    public Object getAttribute(String name)
    {
      return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value)
    {
      attributes.put(name, value);
    }

    @Override
    public HttpSession getSession()
    {
      return session;
    }
  }

  private static final String      pack = "test.generated";

  private static MdControllerDAO   mdController;

  private static MdBusinessDAO     mdBusiness;

  private static MdStructDAO       mdStruct;

  private static MdRelationshipDAO mdRelationship;

  private static MdExceptionDAO    mdException;

  private static String            testActionUri;

  private static String            changeRequestUri;

  private static String            testPrimitiveUri;

  private static String            testBusinessUri;

  private static String            testRelationshipUri;

  private static String            testPrimitiveArrayUri;

  private static String            testDTOArrayUri;

  private static String            testExceptionUri;

//  private static String            testMultipartFileParameterUri;

  public static Test suite()
  {
    TestSuite suite = new TestSuite(ControllerGenTest.class.getSimpleName());
    suite.addTestSuite(ControllerGenTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  private static void classSetUp()
  {
    mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.PACKAGE, pack);
    mdStruct.setValue(MdStructInfo.NAME, "TestStruct");
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Struct");
    mdStruct.apply();

    MdAttributeConcreteDAO mdDouble = MdAttributeDoubleDAO.newInstance();
    mdDouble.setValue(MdAttributeDoubleInfo.NAME, "testDouble");
    mdDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Double");
    mdDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdStruct.getId());
    mdDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "2");
    mdDouble.setValue(MdAttributeDoubleInfo.LENGTH, "2");
    mdDouble.apply();

    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdBusiness.setGenerateMdController(false);
    mdBusiness.apply();

    MdAttributeConcreteDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "255");
    mdAttribute.apply();

    MdAttributeConcreteDAO structAttribute = MdAttributeStructDAO.newInstance();
    structAttribute.setValue(MdAttributeStructInfo.NAME, "testStruct");
    structAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Struct");
    structAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    structAttribute.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getId());
    structAttribute.apply();

    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setValue(MdRelationshipInfo.NAME, "TestRelationship");
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "testParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Relationship");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "testChild");
    mdRelationship.setGenerateMdController(false);
    mdRelationship.apply();

    MdAttributeConcreteDAO integerAttribute = MdAttributeIntegerDAO.newInstance();
    integerAttribute.setValue(MdAttributeIntegerInfo.NAME, "testInteger");
    integerAttribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    integerAttribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Integer");
    integerAttribute.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.FALSE);
    integerAttribute.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    integerAttribute.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    integerAttribute.apply();

    mdController = MdControllerDAO.newInstance();
    mdController.setValue(MdControllerInfo.NAME, "TestController");
    mdController.setValue(MdControllerInfo.PACKAGE, pack);
    mdController.setStructValue(MdControllerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");
    mdController.setStructValue(MdControllerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Controller");
    mdController.apply();

    MdActionDAO mdAction = MdActionDAO.newInstance();
    mdAction.setValue(MdActionInfo.NAME, "testAction");
    mdAction.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
    mdAction.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Action");
    mdAction.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction.apply();

    MdParameterDAO seqParameter = MdParameterDAO.newInstance();
    seqParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    seqParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
    seqParameter.setValue(MdParameterInfo.NAME, "seq");
    seqParameter.setValue(MdParameterInfo.ORDER, "0");
    seqParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    seqParameter.apply();

    MdParameterDAO testParameter = MdParameterDAO.newInstance();
    testParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction.getId());
    testParameter.setValue(MdParameterInfo.TYPE, "java.lang.Long");
    testParameter.setValue(MdParameterInfo.NAME, "value");
    testParameter.setValue(MdParameterInfo.ORDER, "1");
    testParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "value");
    testParameter.apply();

    MdActionDAO mdAction2 = MdActionDAO.newInstance();
    mdAction2.setValue(MdActionInfo.NAME, "changeRequest");
    mdAction2.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction2.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Change Request");
    mdAction2.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Change Request");
    mdAction2.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction2.apply();

    MdActionDAO mdAction3 = MdActionDAO.newInstance();
    mdAction3.setValue(MdActionInfo.NAME, "testPrimitives");
    mdAction3.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction3.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Primitives");
    mdAction3.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Primitives");
    mdAction3.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction3.apply();

    MdParameterDAO shortParam = MdParameterDAO.newInstance();
    shortParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    shortParam.setValue(MdParameterInfo.TYPE, "java.lang.Short");
    shortParam.setValue(MdParameterInfo.NAME, "shortParam");
    shortParam.setValue(MdParameterInfo.ORDER, "0");
    shortParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    shortParam.apply();

    MdParameterDAO integerParam = MdParameterDAO.newInstance();
    integerParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    integerParam.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    integerParam.setValue(MdParameterInfo.NAME, "integerParam");
    integerParam.setValue(MdParameterInfo.ORDER, "1");
    integerParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    integerParam.apply();

    MdParameterDAO longParam = MdParameterDAO.newInstance();
    longParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    longParam.setValue(MdParameterInfo.TYPE, "java.lang.Long");
    longParam.setValue(MdParameterInfo.NAME, "longParam");
    longParam.setValue(MdParameterInfo.ORDER, "2");
    longParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    longParam.apply();

    MdParameterDAO floatParam = MdParameterDAO.newInstance();
    floatParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    floatParam.setValue(MdParameterInfo.TYPE, "java.lang.Float");
    floatParam.setValue(MdParameterInfo.NAME, "floatParam");
    floatParam.setValue(MdParameterInfo.ORDER, "3");
    floatParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    floatParam.apply();

    MdParameterDAO doubleParam = MdParameterDAO.newInstance();
    doubleParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    doubleParam.setValue(MdParameterInfo.TYPE, "java.lang.Double");
    doubleParam.setValue(MdParameterInfo.NAME, "doubleParam");
    doubleParam.setValue(MdParameterInfo.ORDER, "4");
    doubleParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    doubleParam.apply();

    MdParameterDAO characterParam = MdParameterDAO.newInstance();
    characterParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    characterParam.setValue(MdParameterInfo.TYPE, "java.lang.Character");
    characterParam.setValue(MdParameterInfo.NAME, "characterParam");
    characterParam.setValue(MdParameterInfo.ORDER, "5");
    characterParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    characterParam.apply();

    MdParameterDAO stringParam = MdParameterDAO.newInstance();
    stringParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    stringParam.setValue(MdParameterInfo.TYPE, "java.lang.String");
    stringParam.setValue(MdParameterInfo.NAME, "stringParam");
    stringParam.setValue(MdParameterInfo.ORDER, "6");
    stringParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    stringParam.apply();

    MdParameterDAO dateParam = MdParameterDAO.newInstance();
    dateParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction3.getId());
    dateParam.setValue(MdParameterInfo.TYPE, "java.util.Date");
    dateParam.setValue(MdParameterInfo.NAME, "dateParam");
    dateParam.setValue(MdParameterInfo.ORDER, "7");
    dateParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequence");
    dateParam.apply();

    MdActionDAO mdAction4 = MdActionDAO.newInstance();
    mdAction4.setValue(MdActionInfo.NAME, "testBusiness");
    mdAction4.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction4.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdAction4.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdAction4.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction4.apply();

    MdParameterDAO businessParam = MdParameterDAO.newInstance();
    businessParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction4.getId());
    businessParam.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    businessParam.setValue(MdParameterInfo.NAME, "businessParam");
    businessParam.setValue(MdParameterInfo.ORDER, "0");
    businessParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Business Param");
    businessParam.apply();

    MdActionDAO mdAction5 = MdActionDAO.newInstance();
    mdAction5.setValue(MdActionInfo.NAME, "testRelationship");
    mdAction5.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction5.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Relationship");
    mdAction5.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Relationship");
    mdAction5.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction5.apply();

    MdParameterDAO relationshipParam = MdParameterDAO.newInstance();
    relationshipParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction5.getId());
    relationshipParam.setValue(MdParameterInfo.TYPE, mdRelationship.definesType());
    relationshipParam.setValue(MdParameterInfo.NAME, "relationshipParam");
    relationshipParam.setValue(MdParameterInfo.ORDER, "0");
    relationshipParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship Param");
    relationshipParam.apply();

    MdActionDAO mdAction6 = MdActionDAO.newInstance();
    mdAction6.setValue(MdActionInfo.NAME, "testArray");
    mdAction6.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction6.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Array");
    mdAction6.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Array");
    mdAction6.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction6.apply();

    MdParameterDAO stringArrayParam = MdParameterDAO.newInstance();
    stringArrayParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction6.getId());
    stringArrayParam.setValue(MdParameterInfo.TYPE, "java.lang.String[]");
    stringArrayParam.setValue(MdParameterInfo.NAME, "testArray");
    stringArrayParam.setValue(MdParameterInfo.ORDER, "0");
    stringArrayParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Array");
    stringArrayParam.apply();

    MdActionDAO mdAction7 = MdActionDAO.newInstance();
    mdAction7.setValue(MdActionInfo.NAME, "testDTOArray");
    mdAction7.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction7.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction7.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction7.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction7.apply();

    MdParameterDAO dtoArrayParam = MdParameterDAO.newInstance();
    dtoArrayParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction7.getId());
    dtoArrayParam.setValue(MdParameterInfo.TYPE, mdBusiness.definesType() + "[]");
    dtoArrayParam.setValue(MdParameterInfo.NAME, "testArray");
    dtoArrayParam.setValue(MdParameterInfo.ORDER, "0");
    dtoArrayParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Array");
    dtoArrayParam.apply();

    MdActionDAO mdAction8 = MdActionDAO.newInstance();
    mdAction8.setValue(MdActionInfo.NAME, "testException");
    mdAction8.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction8.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction8.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction8.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction8.apply();

    MdActionDAO mdAction9 = MdActionDAO.newInstance();
    mdAction9.setValue(MdActionInfo.NAME, "testMultipartFileParameter");
    mdAction9.setValue(MdActionInfo.ENCLOSING_MD_CONTROLLER, mdController.getId());
    mdAction9.setStructValue(MdActionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction9.setStructValue(MdActionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test DTO Array");
    mdAction9.setValue(MdActionInfo.IS_POST, MdAttributeBooleanInfo.TRUE);
    mdAction9.apply();

    MdParameterDAO multipartFileParam = MdParameterDAO.newInstance();
    multipartFileParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdAction9.getId());
    multipartFileParam.setValue(MdParameterInfo.TYPE, MdActionInfo.MULTIPART_FILE_PARAMETER);
    multipartFileParam.setValue(MdParameterInfo.NAME, "file");
    multipartFileParam.setValue(MdParameterInfo.ORDER, "0");
    multipartFileParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "File parameter");
    multipartFileParam.apply();

    mdException = MdExceptionDAO.newInstance();
    mdException.setValue(MdExceptionInfo.NAME, "TestException");
    mdException.setValue(MdExceptionInfo.PACKAGE, pack);
    mdException.setStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Exception");
    mdException.apply();

    testActionUri = "/contextPath/" + mdController.definesType() + "." + mdAction.getName() + MdActionInfo.ACTION_SUFFIX;
    changeRequestUri = "/contextPath/" + mdController.definesType() + "." + mdAction2.getName() + MdActionInfo.ACTION_SUFFIX;
    testPrimitiveUri = "/contextPath/" + mdController.definesType() + "." + mdAction3.getName() + MdActionInfo.ACTION_SUFFIX;
    testBusinessUri = "/contextPath/" + mdController.definesType() + "." + mdAction4.getName() + MdActionInfo.ACTION_SUFFIX;
    testRelationshipUri = "/contextPath/" + mdController.definesType() + "." + mdAction5.getName() + MdActionInfo.ACTION_SUFFIX;
    testPrimitiveArrayUri = "/contextPath/" + mdController.definesType() + "." + mdAction6.getName() + MdActionInfo.ACTION_SUFFIX;
    testDTOArrayUri = "/contextPath/" + mdController.definesType() + "." + mdAction7.getName() + MdActionInfo.ACTION_SUFFIX;
    testExceptionUri = "/contextPath/" + mdController.definesType() + "." + mdAction8.getName() + MdActionInfo.ACTION_SUFFIX;
//    testMultipartFileParameterUri = "/contextPath/" + mdController.definesType() + "." + mdAction9.getName() + MdActionInfo.ACTION_SUFFIX;

    finalizeSetup();
  }

  private static void finalizeSetup()
  {
    mdController.setValue(MdControllerInfo.STUB_SOURCE, getSource());
    mdController.apply();

    mdBusiness.setValue(MdBusinessInfo.DTO_STUB_SOURCE, getDTOsource());
    mdBusiness.apply();
  }

  private static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  public void testSetRequestParameter() throws Exception
  {
    HttpServletRequest req = new MockRequest("GET", testActionUri);
    HttpServletResponse resp = new DummyResponse();
    Boolean b = new Boolean(false);

    req.setAttribute("testAttribute", MdAttributeBooleanInfo.FALSE);

    Class<?> controllerClass = LoaderDecorator.load(mdController.definesType());
    Constructor<?> constructor = controllerClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class, Boolean.class);
    Object object = constructor.newInstance(req, resp, b);

    controllerClass.getMethod("changeRequest").invoke(object);
    HttpServletRequest ret = (HttpServletRequest) controllerClass.getMethod("getRequest").invoke(object);

    assertEquals(MdAttributeBooleanInfo.TRUE, ret.getAttribute("testAttribute"));
  }

  public void testActionParameter() throws Exception
  {
    HttpServletRequest req = new MockRequest("GET", testActionUri);
    HttpServletResponse resp = new DummyResponse();
    Boolean b = new Boolean(false);

    long value = 10;

    req.setAttribute("testAttribute", 0);
    assertEquals(0, req.getAttribute("testAttribute"));

    Class<?> controllerClass = LoaderDecorator.load(mdController.definesType());
    Constructor<?> constructor = controllerClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class, Boolean.class);
    Object object = constructor.newInstance(req, resp, b);

    controllerClass.getMethod("testAction", Long.class, Long.class).invoke(object, value, 0L);
    HttpServletRequest ret = (HttpServletRequest) controllerClass.getMethod("getRequest").invoke(object);

    assertEquals(value, ret.getAttribute("testAttribute"));
  }

  public void testDispatcherNoParameter() throws Exception
  {
    HttpServletRequest req = new MockRequest("POST", changeRequestUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute("testAttribute", MdAttributeBooleanInfo.FALSE);

    new ServletDispatcher().service(req, resp);

    assertEquals(MdAttributeBooleanInfo.TRUE, req.getAttribute("testAttribute"));

  }

  public void testDispatcherPrimitiveParameters() throws Exception
  {
    MockRequest req = new MockRequest("POST", testActionUri);
    HttpServletResponse resp = new DummyResponse();

    long value = 10;

    req.setAttribute("testAttribute", 0);
    req.setParameter("seq", Long.toString(value));
    req.setParameter("value", Long.toString(value));

    assertEquals(0, req.getAttribute("testAttribute"));

    new ServletDispatcher().service(req, resp);

    assertEquals(value * 2, req.getAttribute("testAttribute"));
  }

  public void testDispatcherAllPrimitiveParameters() throws Exception
  {
    MockRequest req = new MockRequest("POST", testPrimitiveUri);
    HttpServletResponse resp = new DummyResponse();

    Short shortParam = new Short((short) 1);
    Integer integerParam = new Integer(2);
    Long longParam = new Long(3);
    Float floatParam = new Float(1.1);
    Double doubleParam = new Double(2.2);
    Character characterParam = new Character('a');
    String stringParam = new String("ab");
    Date dateParam = new Date(System.currentTimeMillis());
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT, CommonProperties.getDefaultLocale());

    req.setParameter("shortParam", shortParam.toString());
    req.setParameter("integerParam", integerParam.toString());
    req.setParameter("longParam", longParam.toString());
    req.setParameter("floatParam", floatParam.toString());
    req.setParameter("doubleParam", doubleParam.toString());
    req.setParameter("characterParam", characterParam.toString());
    req.setParameter("stringParam", stringParam.toString());
    req.setParameter("dateParam", dateFormat.format(dateParam));

    new ServletDispatcher().service(req, resp);

    assertEquals(req.getAttribute("shortParam"), shortParam);
    assertEquals(req.getAttribute("integerParam"), integerParam);
    assertEquals(req.getAttribute("longParam"), longParam);
    assertEquals(req.getAttribute("floatParam"), floatParam);
    assertEquals(req.getAttribute("doubleParam"), doubleParam);
    assertEquals(req.getAttribute("characterParam"), characterParam);
    assertEquals(req.getAttribute("stringParam"), stringParam);
    assertEquals(dateFormat.format(dateParam), dateFormat.format(req.getAttribute("dateParam")));
  }

  public void testDispatcherBusinessParameters() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientSession = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testBusinessUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientSession);
    req.setParameter("businessParam.testCharacter", "testValue");
    req.setParameter("businessParam.testStruct.testDouble", "10.2");
    req.setParameter("businessParam.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("businessParam.componentId", "unknown");

    new ServletDispatcher().service(req, resp);

    assertEquals("10.2", req.getAttribute("testDouble"));

    systemSession.logout();
  }

  public void testDispatcherRelationshipParameters() throws Exception
  {
    BusinessDAO dao = BusinessDAO.newInstance(mdBusiness.definesType());
    dao.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "test");
    dao.apply();

    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testRelationshipUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("relationshipParam.testInteger", "10");
    req.setParameter("relationshipParam.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("relationshipParam.componentId", "blank");
    req.setParameter("#relationshipParam.parent.id", dao.getId());
    req.setParameter("#relationshipParam.child.id", dao.getId());

    new ServletDispatcher().service(req, resp);

    assertEquals(10, req.getAttribute("testInteger"));

    systemSession.logout();
  }

  public void testDispatcherPrimitiveArrayParameters() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    try
    {
      MockRequest req = new MockRequest("POST", testPrimitiveArrayUri);
      HttpServletResponse resp = new DummyResponse();

      String[] array = new String[] { "cat", "mouse", "dog", "lion", "eagle", "bat" };

      req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
      req.setParameterValues("testArray", array);

      new ServletDispatcher().service(req, resp);

      assertEquals("lion", req.getAttribute("testArray"));
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testDispatcherPrimitiveFailure() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    try
    {

      MockRequest req = new MockRequest("POST", testActionUri);
      HttpServletResponse resp = new DummyResponse();

      long value = 10;

      req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
      req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
      req.setAttribute("testAttribute", 20);

      // Set invalid value
      req.setParameter("seq", "abcdef");
      req.setParameter("value", Long.toString(value));

      assertEquals(20, req.getAttribute("testAttribute"));

      new ServletDispatcher().service(req, resp);

      assertEquals("abcdef", req.getAttribute("testAttribute"));
    }
    finally
    {
      systemSession.logout();
    }

  }

  public void testDispatcherDTOFailure() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testBusinessUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setAttribute(ClientConstants.CLIENTSESSION, systemSession);
    req.setParameter("businessParam.testCharacter", "testValue");
    req.setParameter("businessParam.testStruct.testDouble", "invalid");
    req.setParameter("businessParam.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("businessParam.componentId", "unknown");

    new ServletDispatcher().service(req, resp);

    assertEquals("invalid", req.getAttribute("testDouble"));
    assertEquals("testValue Extra Stuff", req.getAttribute(TestFixConst.ATTRIBUTE_CHARACTER));

    systemSession.logout();
  }

  public void testDispatcherUnknownAction() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", "/contextPath/" + mdController.definesType() + ".unknown.mojo");
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

    try
    {
      new ServletDispatcher().service(req, resp);

      fail("Update to invoke an action which has not been defined");
    }
    catch (UnknownServletException e)
    {
      // This is expected
      assertEquals("An action at the uri [/test.generated.TestController.unknown.mojo] does not exist.", e.getMessage());
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testGetMethodOnPost() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("GET", testBusinessUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);
    req.setParameter("businessParam.testCharacter", "testValue");
    req.setParameter("businessParam.testStruct.testDouble", "invalid");
    req.setParameter("businessParam.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("businessParam.componentId", "unknown");

    try
    {
      new ServletDispatcher().service(req, resp);

      fail("Able to invoke a POST only method through a GET");
    }
    catch (IllegalURIMethodException e)
    {
      // This is expected
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testDispatcherUnknownServlet() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", "/contextPath/invalid.unkown.mojo");
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

    try
    {
      new ServletDispatcher().service(req, resp);

      fail("Update to invoke an action which has not been defined");
    }
    catch (UnknownServletException e)
    {
      // This is expected
      assertEquals("An action at the uri [/invalid.unkown.mojo] does not exist.", e.getMessage());
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testPropigateException() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testExceptionUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

    try
    {
      new ServletDispatcher().service(req, resp);

      fail("Update to invoke an action which has not been defined");
    }
    catch (RuntimeException e)
    {
      assertTrue(LoaderDecorator.load(pack + ".TestExceptionDTO").isInstance(e));
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testDTOArray() throws Exception
  {
    ClientSession systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    ClientRequestIF clientRequest = systemSession.getRequest();

    MockRequest req = new MockRequest("POST", testDTOArrayUri);
    HttpServletResponse resp = new DummyResponse();

    req.setAttribute(ClientConstants.CLIENTREQUEST, clientRequest);

    req.setParameter("testArray_0.testCharacter", "testValue");
    req.setParameter("testArray_0.testStruct.testDouble", "10.2");
    req.setParameter("testArray_0.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("testArray_0.componentId", "unknown");

    req.setParameter("testArray_1.testCharacter", "testValue2");
    req.setParameter("testArray_1.testStruct.testDouble", "10.3");
    req.setParameter("testArray_1.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("testArray_1.componentId", "unknown");

    req.setParameter("testArray_2.testCharacter", "testValue3");
    req.setParameter("testArray_2.testStruct.testDouble", "10.4");
    req.setParameter("testArray_2.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("testArray_2.componentId", "unknown");

    req.setParameter("testArray_3.testCharacter", "testValue4");
    req.setParameter("testArray_3.testStruct.testDouble", "10.5");
    req.setParameter("testArray_3.isNew", MdAttributeBooleanInfo.TRUE);
    req.setParameter("testArray_3.componentId", "unknown");

    try
    {
      new ServletDispatcher().service(req, resp);

      Object testCharacter = req.getAttribute(TestFixConst.ATTRIBUTE_CHARACTER);

      assertEquals("10.5", req.getAttribute("testDouble"));
      assertEquals("testValue4", testCharacter);
    }
    finally
    {
      systemSession.logout();
    }
  }

  private static String getDTOsource()
  {
    String source = "package test.generated; \n" + "" + "import java.util.Locale;\n" + "" + "public class TestBusinessDTO extends TestBusinessDTOBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public final static String CLASS = \"test.generated.TestBusiness\";\n" + "  private static final long serialVersionUID = 1220396508382L;\n" + "  public TestBusinessDTO(" + ClientRequestIF.class.getName() + " clientRequest)\n" + "  {\n" + "    super(clientRequest);\n" + "  }\n" + "  /**\n" + "  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.\n" + "  * \n" + "  * @param businessDTO The BusinessDTO to duplicate\n" + "  * @param clientRequest The clientRequest this DTO should use to communicate with the server.\n" + "  */\n"
        + "  protected TestBusinessDTO(" + BusinessDTO.class.getName() + " businessDTO, " + ClientRequestIF.class.getName() + " clientRequest)\n" + "  {\n" + "    super(businessDTO, clientRequest);\n" + "  }\n" + "}\n";
    return source;
  }

  private static String getSource()
  {
    String source = "package test.generated; \n" + "public class TestController extends TestControllerBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public TestController(javax.servlet.http.HttpServletRequest req, javax.servlet.http.HttpServletResponse resp, java.lang.Boolean bool)\n" + "  {\n" + "    super(req, resp, bool);\n" + "  }\n" + "  public void testAction(java.lang.Long seq, java.lang.Long value)\n" + "  {\n" + "    req.setAttribute(\"testAttribute\", seq + value);\n" + "  }\n" + "  public void failTestAction(java.lang.String seq, java.lang.String value)\n" + "  {\n" + "    req.setAttribute(\"testAttribute\", seq);\n" + "  }\n" + "  public void changeRequest()\n" + "  {\n" + "    req.setAttribute(\"testAttribute\", \"true\");\n" + "  }\n"
        + "  public void testPrimitives(java.lang.Short shortParam, java.lang.Integer integerParam, java.lang.Long longParam, java.lang.Float floatParam, java.lang.Double doubleParam, java.lang.Character characterParam, java.lang.String stringParam, java.util.Date dateParam)\n" + "  {\n" + "    req.setAttribute(\"shortParam\", shortParam);\n" + "    req.setAttribute(\"integerParam\", integerParam);\n" + "    req.setAttribute(\"longParam\", longParam);\n" + "    req.setAttribute(\"floatParam\", floatParam);\n" + "    req.setAttribute(\"doubleParam\", doubleParam);\n" + "    req.setAttribute(\"characterParam\", characterParam);\n" + "    req.setAttribute(\"stringParam\", stringParam);\n" + "    req.setAttribute(\"dateParam\", dateParam);\n" + "  }\n"
        + "  public void testBusiness(test.generated.TestBusinessDTO businessParam)\n" + "  {\n" + "    req.setAttribute(\"testCharacter\", businessParam.getValue(\"testCharacter\"));\n" + "    req.setAttribute(\"testDouble\", businessParam.getTestStruct().getValue(\"testDouble\"));\n" + "  }\n" + "  public void failTestBusiness(test.generated.TestBusinessDTO businessParam)\n" + "  {\n" + "    req.setAttribute(\"testCharacter\", \"testValue Extra Stuff\");\n" + "    req.setAttribute(\"testDouble\", \"invalid\");\n" + "  }\n" + "  public void testRelationship(test.generated.TestRelationshipDTO relationshipParam)\n" + "  {\n" + "    req.setAttribute(\"testInteger\", relationshipParam.getTestInteger());\n" + "  }\n" + "  public void testArray(java.lang.String[] testArray)\n" + "  {\n"
        + "    req.setAttribute(\"testArray\", testArray[3]);\n" + "  }\n" + "  public void testDTOArray(test.generated.TestBusinessDTO[] testArray)\n" + "  {\n" + "    req.setAttribute(\"testCharacter\", testArray[3].getValue(\"testCharacter\"));\n" + "    req.setAttribute(\"testDouble\", testArray[3].getTestStruct().getValue(\"testDouble\"));\n" + "  }\n" + "  public void testException()\n" + "  {\n" + " throw new " + pack + ".TestExceptionDTO(super.getClientRequest());\n" + "  }\n"  + "  public void testMultipartFileParameter(" + MdActionInfo.MULTIPART_FILE_PARAMETER +" file)\n" + "  {\n" + "}\n" + "}\n";

    return source;
  }
}
