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
package com.runwaysdk.business.ontology;

import java.util.List;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.SessionFacade;

public class OntologyFacadeMethodsTest extends TestCase
{
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
         sessionId = SessionFacade.logIn("SYSTEM", "SYSTEM", new Locale[]{Locale.CANADA});
      }

      protected void tearDown() throws Exception
      {
        SessionFacade.closeSession(sessionId);
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
  private static String sessionId;
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
    oldParent.apply();
    
    child = BusinessDAO.newInstance(mdTerm.definesType());
    child.apply();
    
    relat = oldParent.addChild(child, mdTermRelationship.definesType());
    relat.apply();
    
    newParent = BusinessDAO.newInstance(mdTerm.definesType());
    newParent.apply();
    
    letterA = BusinessDAO.newInstance(mdTerm.definesType());
    letterA.apply();
    letterB = BusinessDAO.newInstance(mdTerm.definesType());
    letterB.apply();
    letterC = BusinessDAO.newInstance(mdTerm.definesType());
    letterC.apply();
    letterCC = BusinessDAO.newInstance(mdTerm.definesType());
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
    RelationshipDTO rel = Facade.moveBusiness(sessionId, newParent.getId(), child.getId(), relat.getId(), mdTermRelationship.definesType());
    assertFalse(rel.isNewInstance());
  }
  
  public void testGetTermAllChildren() {
    List<TermAndRel> listTNR = Facade.getTermAllChildren(sessionId, letterA.getId(), 0, 0);
    
    assertEquals(3, listTNR.size());
    
//    for (int i = 0; i < listTNR.size(); ++i) {
//      TermAndRel tnr = listTNR.get(i);
//      assertEquals(letterB.getId(), tnr.getTerm().getId());
//    }
  }
}
