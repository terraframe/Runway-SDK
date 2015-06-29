/**
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
 */
package com.runwaysdk;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class SmokeTest
{
  private MdBusinessDAO mdBusiness;

  private BusinessDAO   businessDAO;

//  @Test
//  @Request
//  public void testCreateAndExportMetadata()
//  {
//    genSource();
//    // doTestCreateAndExportMetadata();
//  }

  @Transaction
  public void genSource()
  {
    // Delete it if it already exists (from a not cleaned up previous run)
    try {
      MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO("com.runwaysdk.smoke.Smoking");
      mdEntityIF.getEntityDAO().delete();
    }
    catch (DataNotFoundException e) {
      
    }
    
    // Create test object
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "Smoking");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "com.runwaysdk.smoke");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SmokeyBusiness");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Where there's smoke there's fire.");
    mdBusiness.apply();
    
    MdAttributeLocalCharacterDAO mdAttribute = MdAttributeLocalCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeStructInfo.NAME, "smokeCharAttribute");
    mdAttribute.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Where there's smoke there's fire.");
    mdAttribute.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttribute.apply();

    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setStructValue("smokeCharAttribute", MdAttributeLocalInfo.DEFAULT_LOCALE, "Yo Diggidy");
    businessDAO.apply();
  }

  @Transaction
  public void doTestCreateAndExportMetadata()
  {
    // We should now be able to load the class
    LoaderDecorator.load("com.runwaysdk.smoke.Smoking");
    
    try
    {
      // Export the test object to xml
      String path = CommonProperties.getProjectBasedir() + "/target/testxml/testCreateAndExportMetadata.xml";
      SAXExporter.export(path, XMLConstants.DATATYPE_XSD, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, businessDAO }));

      // Delete the test object
      // EntityDAO.get(mdBusiness.getId()).getEntityDAO().delete();
      // mdBusiness.delete();

      // TODO : This code is almost the same as the SAXParseTest, yet the
      // SAXParseTest works and this doesn't.
      // When running the import it fails with DuplicateDataException :
      // com.runwaysdk.smoke.SmokingController already exists

      // Import the test object
      // SAXImporter.runImport(new File(path));
      //
      // // Assert it imported correctly
      // List<String> ids =
      // EntityDAO.getEntityIdsDB("com.runwaysdk.smoke.Smoking");
      //
      // assertEquals(1, ids.size());
      //
      // BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
      // AttributeLocalCharacter attribute = (AttributeLocalCharacter)
      // businessDAOIF.getAttributeIF("smokeCharAttribute");
      //
      // assertEquals("Yo Diggidy", attribute.getValue());
    }
    finally
    {
      MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO("com.runwaysdk.smoke.Smoking");
      mdEntityIF.getEntityDAO().delete();
    }
  }
}
