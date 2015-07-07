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
package com.runwaysdk.jstest;

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.scheduler.ExecutableJob;
import com.runwaysdk.system.scheduler.QualifiedTypeJob;

public class BuildDomain
{
  public static final String PACKAGE = "com.runwaysdk.jstest.business.ontology";
  
  @Request
  public static void main(String[] args)
  {
//    String s = "2005-07-15T08:59:14";
//    String s = "2008-01-25T19:36:44Z";
//    DateUtilities.parseDate(s);
    
    doIt();
  }
  
  @Transaction
  public static void doIt() {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Alphabet");
    mdTerm.setValue(MdTermInfo.PACKAGE, PACKAGE);
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JS Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JS Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();
    
    MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
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
    
    QualifiedTypeJob job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Play With Fido");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Shoots a disc out of the cd tray for fido to go chase. Takes 4 seconds to complete.");
    job.setWorkTotal(4);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
    
    job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Bake Cookies");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tells the computer to begin a delicious batch of chocolate chip cookies immediately. Takes 8 seconds to complete.");
    job.setWorkTotal(8);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
    
    job = new QualifiedTypeJob();
    job.setClassName(WaitSecondJob.class.getName());
    job.setJobId("Clean House");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Turns on the Roomba and waits for its completion. Takes 16 seconds to complete.");
    job.setWorkTotal(16);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
    
    job = new QualifiedTypeJob();
    job.setClassName(TestErrorJob.class.getName());
    job.setJobId("Throw a Server Error");
    job.setStructValue(ExecutableJob.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an error immediately every time you start the job.");
    job.setWorkTotal(16);
    job.setWorkProgress(0);
    job.setPauseable(true);
    job.setCancelable(true);
    job.apply();
  }
}
