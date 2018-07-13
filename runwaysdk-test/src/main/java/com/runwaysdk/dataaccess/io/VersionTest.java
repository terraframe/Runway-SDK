/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeTextDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportMetadata;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionExporter;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;

public class VersionTest
{
  public static final String path   = TestConstants.Path.XMLFiles + "/";

  public static final String SCHEMA = XMLConstants.VERSION_XSD;

  /**
   * Tests deleting and adding an attribute of the same name on the same time
   * within the same transaction.
   */
  @Request
  @Test
  public void testDeleteAndAttributeInTransaction_Enumeration()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    String attrTransPath = path + "/DeleteAndAddAttributeInTransaction_Enumeration/";

    // Import merge file
    UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, "0000000000000006" });

    MdBusinessDAOIF mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class99");

    MdAttributeConcreteDAOIF mdAttribute = mdBusinessDAOIF.definesAttribute("myAttribute");

    try
    {
      if (mdAttribute == null)
      {
        Assert.fail("Attribute was not properly defined");
      }

      if (! ( mdAttribute instanceof MdAttributeEnumerationDAOIF ))
      {
        Assert.fail("Attribute should have been redefined to type [" + MdAttributeEnumerationInfo.CLASS + "] but instead was of type [" + mdAttribute.getType() + "]");
      }

      if (!mdAttribute.getColumnName().equals(mdAttribute.getDefinedColumnName()))
      {
        Assert.fail("[" + MdAttributeConcreteInfo.COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
      }

      MdAttributeEnumerationDAOIF mdAttributeEnum = (MdAttributeEnumerationDAOIF) mdAttribute;

      if (!mdAttributeEnum.getCacheColumnName().equals(mdAttributeEnum.getDefinedCacheColumnName()))
      {
        Assert.fail("[" + MdAttributeEnumerationInfo.CACHE_COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
      }

      BusinessDAOIF enumBusinessDAO = BusinessDAO.get("test.xmlclasses.EnumClassTest99", "test.xmlclasses.EnumClassTest99.CO");
      BusinessDAOIF businessDAOIF = BusinessDAO.get("test.xmlclasses.Class99", "test.xmlclasses.Class99.01");

      AttributeEnumerationIF attributeEnum = (AttributeEnumerationIF) businessDAOIF.getAttributeIF("myAttribute");
      if (!attributeEnum.getCachedEnumItemIdSet().contains(enumBusinessDAO.getId()))
      {
        Assert.fail("Hashed temp column for cached enum ids did not transfer over its values to the final column at the end of the transaction.");
      }

    }
    finally
    {
      mdBusinessDAOIF.getBusinessDAO().delete();

      mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.EnumClassTest99");

      mdBusinessDAOIF.getBusinessDAO().delete();

      UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }

    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  /**
   * Tests deleting and adding an attribute of the same name on the same time
   * within the same transaction.
   */
  @Request
  @Test
  public void testDeleteAndAttributeInTransaction()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    String attrTransPath = path + "/DeleteAndAddAttributeInTransaction/";

    // Import merge file
    UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, "0000000000000006" });

    MdBusinessDAOIF mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class99");

    MdAttributeConcreteDAOIF mdAttribute = mdBusinessDAOIF.definesAttribute("myAttribute");

    try
    {
      if (mdAttribute == null)
      {
        Assert.fail("Attribute was not properly defined");
      }

      if (! ( mdAttribute instanceof MdAttributeTextDAOIF ))
      {
        Assert.fail("Attribute should have been redefined to type [" + MdAttributeTextInfo.CLASS + "] but instead was of type [" + mdAttribute.getType() + "]");
      }

      if (!mdAttribute.getColumnName().equals(mdAttribute.getDefinedColumnName()))
      {
        Assert.fail("[" + MdAttributeConcreteInfo.COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
      }
    }
    finally
    {
      mdBusinessDAOIF.getBusinessDAO().delete();

      mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.EnumClassTest99");

      mdBusinessDAOIF.getBusinessDAO().delete();

      UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }

    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  /**
   * Tests deleting and adding an attribute of the same name on the same time
   * within the same transaction, but produces an exception and tests to see it
   * rolls back properly.
   */
  @Request
  @Test
  public void testDeleteAndAttributeInTransaction_Exception()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    String attrTransPath = path + "/DeleteAndAddAttributeInTransaction_Exception/";

    UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, "0000000000000001" });

    MdBusinessDAOIF mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class99");

    // Import merge file
    try
    {
      UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, "0000000000000005" });
    }
    catch (XMLParseException e)
    {
      if (e.getCause() instanceof DuplicateAttributeDefinitionException)
      {
        MdAttributeConcreteDAOIF mdAttribute = mdBusinessDAOIF.definesAttribute("myAttribute");

        if (mdAttribute == null)
        {
          Assert.fail("Attribute was not properly rolled back.");
        }

        if (! ( mdAttribute instanceof MdAttributeBooleanDAOIF ))
        {
          Assert.fail("Attribute should have been rolled back to its original type [" + MdAttributeBooleanInfo.CLASS + "] but instead was of type [" + mdAttribute.getType() + "]");
        }

        if (!mdAttribute.getColumnName().equals(mdAttribute.getDefinedColumnName()))
        {
          Assert.fail("[" + MdAttributeConcreteInfo.COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
        }
      }
    }
    finally
    {
      mdBusinessDAOIF.getBusinessDAO().delete();

      UpdateVersion.run(new String[] { attrTransPath, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }

    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  @Request
  @Test
  public void testIncreaseVersion()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, "0001204354800000" });

    try
    {
      // Ensure that the database is at the correct version
      List<String> timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

      Assert.assertTrue(timestamps.contains("0001204354800000"));

      // Ensure that the MdBusiness and its MdAttribute were imported
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");
      Assert.assertNotNull(mdBusiness);

      MdAttributeDAOIF mdAttribute = mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
      Assert.assertNotNull(mdAttribute);
    }
    finally
    {
      UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });

      Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
    }
  }

  @Request
  @Test
  public void testDecremenateVersion()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, "0001207033200000" });

    try
    {
      // Ensure that the database is at the correct version
      List<String> timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

      Assert.assertTrue(timestamps.contains("0001207033200000"));
      Assert.assertTrue(timestamps.contains("0001204354800000"));

      // Ensure that the MdBusiness and its MdAttribute were imported
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");

      Assert.assertNotNull(mdBusiness);
      Assert.assertNotNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertNotNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN));

      UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, "0001204354800000" });

      // Ensure that the database is at the correct version
      timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

      Assert.assertFalse(timestamps.contains("0001207033200000"));
      Assert.assertTrue(timestamps.contains("0001204354800000"));

      // Ensure that the MdBusiness and its MdAttribute were imported
      mdBusiness = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");

      Assert.assertNotNull(mdBusiness);
      Assert.assertNotNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN));
    }
    finally
    {
      UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
      Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
    }

  }

  @Request
  @Test
  public void testMostRecent()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    Versioning.run(new String[] { path, XMLConstants.VERSION_XSD });

    try
    {
      // Ensure that the database is at the correct version
      List<String> timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

      Assert.assertTrue(timestamps.contains("0001247673910176"));

      // Ensure that the MdBusiness and its MdAttribute were imported
      MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO("test.xmlclasses.Class1");

      Assert.assertNotNull(mdBusiness);
      Assert.assertNotNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER));
      Assert.assertNotNull(mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN));

    }
    finally
    {
      UpdateVersion.run(new String[] { path, XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }
    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  /**
   * Test the ability to create and then subsequently delete metadata in a
   * single transaction
   */
  @Request
  @Test
  public void testCreateAndDeleteInOneTransaction()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    UpdateVersion.run(new String[] { path + "createAndDelete/", XMLConstants.VERSION_XSD, "0000000000000002" });

    try
    {
      // Ensure that the database is at the correct version
      List<String> timestamps = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY);

      Assert.assertTrue(timestamps.contains("0000000000000002"));

      // Ensure that the MdException was not imported
      MdExceptionDAOIF mdException = MdExceptionDAO.getMdException("test.xmlclasses.TestException");
      mdException.getBusinessDAO().delete();
      Assert.fail("Expected exception to be delete, but was still found");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
    finally
    {
      UpdateVersion.run(new String[] { path + "createAndDelete/", XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }

    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  /**
   * Test the ability to create and delete metadata then roll back the
   * transaction
   */
  @Request
  @Test
  public void testCreateAndDeleteRollback()
  {
    int original = Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size();

    UpdateVersion.run(new String[] { path + "createAndDelete/", XMLConstants.VERSION_XSD, "0000000000000002" });

    Assert.assertEquals(original + 2, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());

    try
    {
      Versioning.run(new String[] { path + "versionFailFiles/", XMLConstants.VERSION_XSD });
    }
    catch (XMLParseException e)
    {
      // this is expected

      if (! ( e.getCause() instanceof DataNotFoundException ))
      {
        throw e;
      }
    }

    try
    {
      // Ensure that the database rolled back correctly
      Assert.assertEquals(original + 2, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());

      // Ensure that the MdException was not imported
      MdExceptionDAOIF mdException = MdExceptionDAO.getMdException("test.xmlclasses.TestException");
      TestFixtureFactory.delete(mdException);
      Assert.fail("Expected exception to be delete, but was still found");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
    finally
    {
      UpdateVersion.run(new String[] { path + "createAndDelete/", XMLConstants.VERSION_XSD, Database.INITIAL_VERSION });
    }

    Assert.assertEquals(original, Database.getPropertyValue(Database.VERSION_TIMESTAMP_PROPERTY).size());
  }

  @Request
  @Test
  public void testCreateDeleteCreateAttribute() throws IOException
  {
    String dir = path + "CreateDeleteAdd/";

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness);
      mdAttribute.apply();

      CreateDomainModel model = new CreateDomainModel(dir);

      VersionExporter.export(model.create(), SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness }));
      VersionExporter.export(model.create(), SCHEMA, ExportMetadata.buildDelete(new ComponentIF[] { mdAttribute }));

      TestFixtureFactory.delete(mdAttribute);

      ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness });
      metadata.addNewMdAttribute(mdBusiness, TestFixtureFactory.addLocalCharacterAttribute(mdBusiness, TestFixConst.ATTRIBUTE_CHARACTER));

      VersionExporter.export(model.create(), SCHEMA, metadata);

      TestFixtureFactory.delete(mdBusiness);

      // Import version directory
      Versioning.run(new String[] { dir, XMLConstants.VERSION_XSD, "false" });

      MdBusinessDAOIF mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO(mdBusiness.definesType());

      try
      {
        Assert.assertNotNull(mdBusinessDAOIF.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER));
      }
      finally
      {
        TestFixtureFactory.delete(mdBusinessDAOIF);
      }
    }
    finally
    {
      FileIO.deleteDirectory(new File(dir));
    }
  }

  /**
   * Tests creating a type, attribute, and object of that type and then later
   * adding a localized character and updating the object all within the same
   * transaction.
   * 
   * @throws IOException
   */
  @Request
  @Test
  public void testCreateAndUpdateOfObject() throws IOException
  {
    String dir = path + "/CreateDeleteAdd/";

    try
    {
      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      MdAttributeCharacterDAO mdAttribute = TestFixtureFactory.addCharacterAttribute(mdBusiness);
      mdAttribute.apply();

      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.setValue(mdAttribute.definesAttribute(), "test value");
      business.apply();

      CreateDomainModel model = new CreateDomainModel(dir);

      VersionExporter.export(model.create(), SCHEMA, ExportMetadata.buildCreate(new ComponentIF[] { mdBusiness, business }));

      MdAttributeLocalCharacterDAO mdAttributeLocal = TestFixtureFactory.addLocalCharacterAttribute(mdBusiness);
      mdAttributeLocal.apply();

      business = BusinessDAO.get(business.getId()).getBusinessDAO();
      business.setStructValue(mdAttributeLocal.definesAttribute(), "defaultLocale", "Localized Value");
      business.apply();

      ExportMetadata metadata = ExportMetadata.buildUpdate(new ComponentIF[] { mdBusiness, business });
      metadata.addNewMdAttribute(mdBusiness, mdAttributeLocal);

      VersionExporter.export(model.create(), SCHEMA, metadata);

      TestFixtureFactory.delete(mdBusiness);

      // Import merge file
      Versioning.run(new String[] { dir, XMLConstants.VERSION_XSD, "false" });

      MdBusinessDAOIF mdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO(mdBusiness.definesType());

      try
      {
        Assert.assertNotNull(mdBusinessDAOIF.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER));
      }
      finally
      {
        TestFixtureFactory.delete(mdBusinessDAOIF);
      }
    }
    finally
    {
      FileIO.deleteDirectory(new File(dir));
    }
  }

}
