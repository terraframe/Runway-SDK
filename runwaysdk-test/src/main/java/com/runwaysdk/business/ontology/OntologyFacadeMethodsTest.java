/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.business.ontology;

import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdTerm;
import com.runwaysdk.system.ontology.TermUtilDTO;

public class OntologyFacadeMethodsTest extends TestCase
{
  protected static ClientSession     systemSession                  = null;

  protected static ClientRequestIF   clientRequest                  = null;
  
  public OntologyFacadeMethodsTest() throws Exception
  {
    super();
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(OntologyFacadeMethodsTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() throws Exception
      {
         createTypes();
         systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
         clientRequest = systemSession.getRequest();
      }

      protected void tearDown() throws Exception
      {
        systemSession.logout();
        destroyTypes();
      }
    };

    return wrapper;
  }
  
  private static BusinessDAO letterA;
  private static BusinessDAO letterB;
  private static BusinessDAO letterC;
  private static BusinessDAO letterCC;
  
  private static BusinessDAO oldParent;
  private static BusinessDAO child;
  private static RelationshipDAO relat;
  private static BusinessDAO newParent;
  private static MdTermDAO mdTerm;
  private static MdTermRelationshipDAO mdTermRelationship;
  
  @Request
  protected static void createTypes() {
    doCreateTypes();
  }
  
  @Transaction
  private static void doCreateTypes() {
    final String PACKAGE = "com.runwaysdk.test.business.ontology";
    
    mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Alphabet");
    mdTerm.setValue(MdTermInfo.PACKAGE, PACKAGE);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();
    
    mdTermRelationship = MdTermRelationshipDAO.newInstance();
    mdTermRelationship.setValue(MdTreeInfo.NAME, "Sequential");
    mdTermRelationship.setValue(MdTreeInfo.PACKAGE, PACKAGE);
    mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sequential Relationship");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Previous Letter");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
    mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Next Letter");
    mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
    mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
    mdTermRelationship.setGenerateMdController(false);
    mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
    mdTermRelationship.apply();
    
    oldParent = BusinessDAO.newInstance(mdTerm.definesType());
    oldParent.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "oldParent");
    oldParent.apply();
    
    child = BusinessDAO.newInstance(mdTerm.definesType());
    child.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child");
    child.apply();
    
    relat = oldParent.addChild(child, mdTermRelationship.definesType());
    relat.apply();
    
    newParent = BusinessDAO.newInstance(mdTerm.definesType());
    newParent.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "newParent");
    newParent.apply();
    
    letterA = BusinessDAO.newInstance(mdTerm.definesType());
    letterA.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "letterA");
    letterA.apply();
    letterB = BusinessDAO.newInstance(mdTerm.definesType());
    letterB.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "letterB");
    letterB.apply();
    letterC = BusinessDAO.newInstance(mdTerm.definesType());
    letterC.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "letterC");
    letterC.apply();
    letterCC = BusinessDAO.newInstance(mdTerm.definesType());
    letterCC.setStructValue(MdTerm.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "letterCC");
    letterCC.apply();
    
    letterA.addChild(letterB, mdTermRelationship.definesType()).apply();
    letterB.addChild(letterC, mdTermRelationship.definesType()).apply();
    letterB.addChild(letterCC, mdTermRelationship.definesType()).apply();
  }
  
  @Request
  protected static void destroyTypes() {
    TestFixtureFactory.delete(oldParent);
    TestFixtureFactory.delete(child);
    TestFixtureFactory.delete(relat);
    TestFixtureFactory.delete(newParent);
    TestFixtureFactory.delete(letterA);
    TestFixtureFactory.delete(letterB);
    TestFixtureFactory.delete(letterC);
    TestFixtureFactory.delete(letterCC);
    TestFixtureFactory.delete(mdTerm);
    TestFixtureFactory.delete(mdTermRelationship);
  }
  
  public void testMoveBusiness() {
    RelationshipDTO rel = Facade.moveBusiness(clientRequest.getSessionId(), newParent.getId(), child.getId(), relat.getId(), mdTermRelationship.definesType());
    assertFalse(rel.isNewInstance());
  }
  
  public void testGetTermAllChildren() {
//    List<TermAndRelDTO> listTNR = Facade.getTermAllChildren(sessionId, letterA.getId(), 0, 0);
//    
//    assertEquals(3, listTNR.size());
  }
  
  public void testTermUtilGetDirectChildren() {
    TermAndRelDTO[] listTNR = TermUtilDTO.getDirectDescendants(clientRequest, letterA.getId(), new String[]{mdTermRelationship.definesType()});
    
    assertEquals(1, listTNR.length);
  }
  
  public void testTermUtilGetDirectAncestors() {
    TermAndRelDTO[] listTNR = TermUtilDTO.getDirectAncestors(clientRequest, letterC.getId(), new String[]{mdTermRelationship.definesType()});
    
    assertEquals(1, listTNR.length);
  }
  
  // These will fail right now because the DefaultStrategy doesn't implement the getAll methods.
//  public void testTermUtilGetAllChildren() {
//    TermDTO[] terms = TermUtilDTO.getAllDescendants(clientRequest, letterA.getId(), new String[]{mdTermRelationship.definesType()});
//    
//    assertEquals(3, terms.length);
//  }
//  
//  public void testTermUtilGetAllAncestors() {
//    TermDTO[] terms = TermUtilDTO.getAllAncestors(clientRequest, letterC.getId(), new String[]{mdTermRelationship.definesType()});
//    
//    assertEquals(3, terms.length);
//  }
}
