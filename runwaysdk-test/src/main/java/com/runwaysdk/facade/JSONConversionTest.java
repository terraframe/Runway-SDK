/**
*
*/
package com.runwaysdk.facade;

import java.util.List;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.runwaysdk.ClientSession;
import com.runwaysdk.TestSuiteTF;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.generation.json.JSONFacade;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.transport.conversion.json.JSONReturnObject;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiTermMdDTO;
import com.runwaysdk.transport.metadata.AttributeTermMdDTO;
import com.runwaysdk.web.json.JSONController;

public class JSONConversionTest extends TestCase
{
  protected static String          pack                         = "com.test.controller";

  protected static BusinessDTO     testUserMd                   = null;

  protected static String          testUserType                 = null;

  protected static BusinessDTO     tommyUser                    = null;

  protected static BusinessDTO     littleBillyTables            = null;

  protected static ClientSession   systemSession                = null;

  protected static ClientRequestIF clientRequest                = null;

  protected static BusinessDTO     parentMdBusiness             = null;

  protected static String          parentMdBusinessType         = null;

  protected static BusinessDTO     mdAttributeMultiReferenceDTO = null;

  protected static BusinessDTO     mdAttributeMultiTermDTO      = null;

  protected static BusinessDTO     mdAttributeTermDTO           = null;

  protected static BusinessDTO     termClass                    = null;

  protected static String          termType                     = null;

  public static Test suite()
  {
    TestSuiteTF suite = new TestSuiteTF();
    suite.addTestSuite(JSONConversionTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
        clientRequest = systemSession.getRequest();
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession("default", new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession("default", userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return clientSession.getRequest();
  }

  public static void classSetUp()
  {
    // create a new TestUser type with a phone number struct
    testUserMd = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    testUserMd.setValue(MdBusinessInfo.NAME, "TestUser");
    testUserMd.setValue(MdBusinessInfo.PACKAGE, pack);
    testUserMd.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testUserMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "User Subclass");
    testUserMd.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, UserInfo.ID_VALUE);
    clientRequest.createBusiness(testUserMd);

    testUserType = definesType(testUserMd);

    tommyUser = clientRequest.newBusiness(testUserType);
    tommyUser.setValue(UserInfo.USERNAME, "Tommy");
    tommyUser.setValue(UserInfo.PASSWORD, "music");
    clientRequest.createBusiness(tommyUser);

    littleBillyTables = clientRequest.newBusiness(testUserType);
    littleBillyTables.setValue(UserInfo.USERNAME, "Billy");
    littleBillyTables.setValue(UserInfo.PASSWORD, "Tables");
    clientRequest.createBusiness(littleBillyTables);

    parentMdBusiness = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    parentMdBusiness.setValue(MdBusinessInfo.NAME, "ParentTest");
    parentMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    parentMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test parent");
    parentMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    parentMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.addEnumItem(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.toString());
    clientRequest.createBusiness(parentMdBusiness);

    // class for term object attribute
    termClass = clientRequest.newBusiness(MdTermInfo.CLASS);
    termClass.setValue(MdBusinessInfo.NAME, "TermClass");
    termClass.setValue(MdBusinessInfo.PACKAGE, pack);
    termClass.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    termClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A term class");
    termClass.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit term object");
    termClass.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    termClass.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(termClass);

    mdAttributeMultiReferenceDTO = clientRequest.newBusiness(MdAttributeMultiReferenceInfo.CLASS);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.NAME, "aMultiReference");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeMultiReferenceDTO);

    mdAttributeMultiTermDTO = clientRequest.newBusiness(MdAttributeMultiTermInfo.CLASS);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.NAME, "aMultiTerm");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeMultiTermDTO);

    mdAttributeTermDTO = clientRequest.newBusiness(MdAttributeTermInfo.CLASS);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.NAME, "aTerm");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeTermDTO);

    parentMdBusinessType = definesType(parentMdBusiness);
    termType = definesType(termClass);

  }

  private static String definesType(BusinessDTO mdBusinessDTO)
  {
    return pack + "." + mdBusinessDTO.getValue(MdTypeInfo.NAME);
  }

  public static void classTearDown()
  {
    clientRequest.delete(parentMdBusiness.getId());

    clientRequest.delete(termClass.getId());

    clientRequest.delete(littleBillyTables.getId());

    clientRequest.delete(tommyUser.getId());

    clientRequest.delete(testUserMd.getId());

    systemSession.logout();
  }

  public void testAttributeMultiReference() throws JSONException
  {
    String attributeName = "aMultiReference";

    BusinessDTO term = clientRequest.newBusiness(termType);
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = (BusinessDTO) clientRequest.newMutable(parentMdBusinessType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());

      String businessJSON = JSONFacade.getJSONFromComponentDTO(instance).toString();

      JSONObject response = new JSONObject(JSONController.createBusiness(clientRequest.getSessionId(), businessJSON));

      BusinessDTO test = (BusinessDTO) JSONFacade.getObjectFromJSON(clientRequest.getSessionId(), parentMdBusinessType, response.getString(JSONReturnObject.RETURN_VALUE));

      try
      {
        // Validate the value
        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getId(), results.get(0));

        // Validate the metadata
        Assert.assertTrue(test.getAttributeMd(attributeName) instanceof AttributeMultiReferenceMdDTO);
      }
      finally
      {
        clientRequest.lock(test);
        clientRequest.delete(test.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeMultiReferenceBrowserResponse() throws JSONException
  {
    String attributeName = "aMultiReference";

    BusinessDTO term = clientRequest.newBusiness(termType);
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = (BusinessDTO) clientRequest.newMutable(parentMdBusinessType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());

      String businessJSON = JSONFacade.getJSONFromComponentDTO(instance).toString();

      JSONObject response = new JSONObject(JSONController.createBusiness(clientRequest.getSessionId(), businessJSON));

      /*
       * Modify the return object to fake like its been submitted from the
       * browser. When submitted the item ids in multi reference attributes are
       * serailized as a JSONObject where key and value are the same.
       */
      JSONObject jsonObject = response.getJSONObject(JSONReturnObject.RETURN_VALUE);
      JSONObject attributeMap = jsonObject.getJSONObject(JSON.ENTITY_DTO_ATTRIBUTE_MAP.getLabel());
      JSONObject attribute = attributeMap.getJSONObject(attributeName);
      JSONArray items = attribute.getJSONArray(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel());

      JSONObject itemIds = new JSONObject();

      for (int i = 0; i < items.length(); i++)
      {
        String itemId = items.getString(i);
        itemIds.put(itemId, itemId);
      }

      attribute.put(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel(), itemIds);

      BusinessDTO test = (BusinessDTO) JSONFacade.getObjectFromJSON(clientRequest.getSessionId(), parentMdBusinessType, jsonObject.toString());

      try
      {
        // Validate the value
        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getId(), results.get(0));

        // Validate the metadata
        Assert.assertTrue(test.getAttributeMd(attributeName) instanceof AttributeMultiReferenceMdDTO);
      }
      finally
      {
        clientRequest.lock(test);
        clientRequest.delete(test.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeTerm() throws JSONException
  {
    String attributeName = "aTerm";

    BusinessDTO term = clientRequest.newBusiness(termType);
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = (BusinessDTO) clientRequest.newMutable(parentMdBusinessType);
      instance.setValue(attributeName, term.getId());

      String businessJSON = JSONFacade.getJSONFromComponentDTO(instance).toString();

      JSONObject response = new JSONObject(JSONController.createBusiness(clientRequest.getSessionId(), businessJSON));

      BusinessDTO test = (BusinessDTO) JSONFacade.getObjectFromJSON(clientRequest.getSessionId(), parentMdBusinessType, response.getString(JSONReturnObject.RETURN_VALUE));

      try
      {
        // Validate the value
        Assert.assertEquals(term.getId(), test.getValue(attributeName));

        // Validate the metadata
        Assert.assertTrue(test.getAttributeMd(attributeName) instanceof AttributeTermMdDTO);
      }
      finally
      {
        clientRequest.lock(test);
        clientRequest.delete(test.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeMultiReferenceGeneration() throws JSONException
  {
    String javascript = JSONController.importTypes(clientRequest.getSessionId(), new String[] { parentMdBusinessType });

    Assert.assertTrue(javascript.contains("getAMultiReference : function()"));
    Assert.assertTrue(javascript.contains("removeAMultiReference : function(item)"));
    Assert.assertTrue(javascript.contains("clearAMultiReference : function()"));
    Assert.assertTrue(javascript.contains("addAMultiReference : function(item)"));
  }

  public void testAttributeMultiTerm() throws JSONException
  {
    String attributeName = "aMultiTerm";

    BusinessDTO term = clientRequest.newBusiness(termType);
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = (BusinessDTO) clientRequest.newMutable(parentMdBusinessType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());

      String businessJSON = JSONFacade.getJSONFromComponentDTO(instance).toString();

      JSONObject response = new JSONObject(JSONController.createBusiness(clientRequest.getSessionId(), businessJSON));

      BusinessDTO test = (BusinessDTO) JSONFacade.getObjectFromJSON(clientRequest.getSessionId(), parentMdBusinessType, response.getString(JSONReturnObject.RETURN_VALUE));

      try
      {
        // Validate the value
        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getId(), results.get(0));

        // Validate the metadata
        Assert.assertTrue(test.getAttributeMd(attributeName) instanceof AttributeMultiTermMdDTO);
      }
      finally
      {
        clientRequest.lock(test);
        clientRequest.delete(test.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeMultiTermBrowserResponse() throws JSONException
  {
    String attributeName = "aMultiTerm";

    BusinessDTO term = clientRequest.newBusiness(termType);
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = (BusinessDTO) clientRequest.newMutable(parentMdBusinessType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());

      String businessJSON = JSONFacade.getJSONFromComponentDTO(instance).toString();

      JSONObject response = new JSONObject(JSONController.createBusiness(clientRequest.getSessionId(), businessJSON));

      /*
       * Modify the return object to fake like its been submitted from the
       * browser. When submitted the item ids in multi reference attributes are
       * serailized as a JSONObject where key and value are the same.
       */
      JSONObject jsonObject = response.getJSONObject(JSONReturnObject.RETURN_VALUE);
      JSONObject attributeMap = jsonObject.getJSONObject(JSON.ENTITY_DTO_ATTRIBUTE_MAP.getLabel());
      JSONObject attribute = attributeMap.getJSONObject(attributeName);
      JSONArray items = attribute.getJSONArray(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel());

      JSONObject itemIds = new JSONObject();

      for (int i = 0; i < items.length(); i++)
      {
        String itemId = items.getString(i);
        itemIds.put(itemId, itemId);
      }

      attribute.put(JSON.MULTI_REFERENCE_ITEM_IDS.getLabel(), itemIds);

      BusinessDTO test = (BusinessDTO) JSONFacade.getObjectFromJSON(clientRequest.getSessionId(), parentMdBusinessType, jsonObject.toString());

      try
      {
        // Validate the value
        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getId(), results.get(0));

        // Validate the metadata
        Assert.assertTrue(test.getAttributeMd(attributeName) instanceof AttributeMultiTermMdDTO);
      }
      finally
      {
        clientRequest.lock(test);
        clientRequest.delete(test.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeMultiTermGeneration() throws JSONException
  {
    String javascript = JSONController.importTypes(clientRequest.getSessionId(), new String[] { parentMdBusinessType });

    Assert.assertTrue(javascript.contains("getAMultiTerm : function()"));
    Assert.assertTrue(javascript.contains("removeAMultiTerm : function(item)"));
    Assert.assertTrue(javascript.contains("clearAMultiTerm : function()"));
    Assert.assertTrue(javascript.contains("addAMultiTerm : function(item)"));
  }
}