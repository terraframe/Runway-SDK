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
/**
 *
 */
package com.runwaysdk.dataaccess;

import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.metadata.CannotAddAttriubteToClassException;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinedInSubclassException;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeInInheritedHierarchyException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MetadataException;
import com.runwaysdk.session.Request;

/**
 * @author Eric G
 *
 */
public class MdBusinessTest
{
  /**
   * The name of the new parent class.
   */
  private static final TypeInfo NEW_PARENT_CLASS  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "NewParentClass");

  /**
   * The name of the new class.
   */
  private static final TypeInfo NEW_CLASS         = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "NewClass");

  private static final String   newClassTableName = "new_class";

  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    MdBusinessDAO newParentMdBusiness = MdBusinessDAO.newInstance();
    newParentMdBusiness.setValue(MdBusinessInfo.NAME, NEW_PARENT_CLASS.getTypeName());
    newParentMdBusiness.setValue(MdBusinessInfo.PACKAGE, NEW_PARENT_CLASS.getPackageName());
    newParentMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    newParentMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    newParentMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    newParentMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    newParentMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    newParentMdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    newParentMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    newParentMdBusiness.apply();

    createNewMdBusiness();
  }

  /**
   *
   *
   */
  private static void createNewMdBusiness()
  {
    MdBusinessDAOIF newParentMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_PARENT_CLASS.getType());

    MdBusinessDAO newMdBusiness = MdBusinessDAO.newInstance();
    newMdBusiness.setValue(MdBusinessInfo.NAME, NEW_CLASS.getTypeName());
    newMdBusiness.setValue(MdBusinessInfo.PACKAGE, NEW_CLASS.getPackageName());
    newMdBusiness.setValue(MdBusinessInfo.TABLE_NAME, newClassTableName);
    newMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    newMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    newMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    newMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    newMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    newMdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
    newMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, newParentMdBusiness.getOid());
    newMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    newMdBusiness.apply();
  }

  /**
   * If testObject was applied, it is removed from the database.
   *
   * 
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    MdBusinessDAOIF newParentMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_PARENT_CLASS.getType());
    MdBusinessDAOIF newMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType());

    if (!newMdBusiness.isNew())
    {
      newMdBusiness.getBusinessDAO().delete();
    }
    if (!newParentMdBusiness.isNew())
    {
      newParentMdBusiness.getBusinessDAO().delete();
    }
  }

  @Request
  @Test
  public void testDuplicateAttributeDefinition()
  {
    MdBusinessDAOIF newParentMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_PARENT_CLASS.getType());

    MdAttributeCharacterDAO mdAttributeCharacter1 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacter1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newParentMdBusiness.getOid());
    mdAttributeCharacter1.apply();

    try
    {
      MdAttributeCharacterDAO mdAttributeCharacter2 = MdAttributeCharacterDAO.newInstance();
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.SIZE, "64");
      mdAttributeCharacter2.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter2.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newParentMdBusiness.getOid());
      mdAttributeCharacter2.apply();

      Assert.fail("Able to add an attribute to a type that already has an attribute defined with that same name");
    }
    catch (DuplicateAttributeDefinitionException e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      mdAttributeCharacter1.delete();
    }
  }

  @Request
  @Test
  public void testDuplicateAttributeInInheritedHierarchy()
  {
    MdBusinessDAOIF newParentMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_PARENT_CLASS.getType());
    MdBusinessDAOIF newChildMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType());

    MdAttributeCharacterDAO mdAttributeCharacter1 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacter1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newParentMdBusiness.getOid());
    mdAttributeCharacter1.apply();

    try
    {
      MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
      mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newChildMdBusiness.getOid());
      mdAttributeCharacter.apply();

      Assert.fail("Able to add an attribute to a type where the parent type already has an attribute defined with that same name");
    }
    catch (DuplicateAttributeInInheritedHierarchyException e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      mdAttributeCharacter1.delete();
    }
  }

  @Request
  @Test
  public void testDuplicateAttributeDefinedInSubclass()
  {
    MdBusinessDAOIF newParentMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_PARENT_CLASS.getType());

    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    MdBusinessDAOIF newChildMdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType());

    MdAttributeCharacterDAO mdAttributeCharacter1 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacter1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newChildMdBusiness.getOid());
    mdAttributeCharacter1.apply();

    try
    {
      MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
      mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, newParentMdBusiness.getOid());
      mdAttributeCharacter.apply();

      Assert.fail("Able to add an attribute to a type where a child type already has an attribute defined with that same name");
    }
    catch (DuplicateAttributeDefinedInSubclassException e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      mdAttributeCharacter1.delete();
    }
  }

  /**
   * Checks the database directly to make sure that the corresponding table has
   * been created for a new type.
   */
  @Request
  @Test
  public void testTableExistsInDatabase()
  {
    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType());
    if (!Database.tableExists(mdBusiness.getTableName()))
    {
      Assert.fail("Table \"" + mdBusiness.getTableName() + "\" not found");
    }
  }

  private String longPackage  = "com.really.longpackage.name.like.way.frickin.longso.longthat.it.gets.a.little.ridiculous.how.longit.is.because.it.has.to.be.lo";

  private String shortPackage = "com.reasonable.packagename";

  private String shortType    = "ShortType";

  private String maxType      = "ThisVeryCrazyTypeNameIsSoLongInFactItNeedsToBe96Char";

  private String longType     = "ThisVeryCrazyTypeNameIsSoLongInFactItNeedsToBe96CharZ";

  @Request
  @Test
  public void testValidLongPackage()
  {
    MdBusinessDAO template = MdBusinessDAO.newInstance();
    template.setValue(MdBusinessInfo.NAME, shortType);
    template.setValue(MdBusinessInfo.PACKAGE, longPackage);
    template.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    template.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Description");
    template.setValue(MdBusinessInfo.CACHE_SIZE, "0");
    template.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    template.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    template.apply();
    template.delete();
  }

  @Request
  @Test
  public void testValidLongTypeName()
  {
    MdBusinessDAO template = MdBusinessDAO.newInstance();
    template.setValue(MdBusinessInfo.NAME, maxType);
    template.setValue(MdBusinessInfo.PACKAGE, shortPackage);
    template.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    template.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Description");
    template.setValue(MdBusinessInfo.CACHE_SIZE, "0");
    template.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    template.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    template.apply();
    template.delete();
  }

  @Request
  @Test
  public void testInvalidLongType()
  {
    MdBusinessDAO template = MdBusinessDAO.newInstance();
    template.setValue(MdBusinessInfo.NAME, longType);
    template.setValue(MdBusinessInfo.PACKAGE, shortPackage);
    template.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    template.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Description");
    template.setValue(MdBusinessInfo.CACHE_SIZE, "0");
    template.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    template.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      template.apply();
      template.delete();
    }
    catch (AttributeLengthCharacterException e)
    {
      // We expect this. The type is too dang long.
    }
  }

  /**
   * Sets the caching on a type to CACHE_EVERYTHING and checks that a Collection
   * Class has been made for the type.
   */
  @Request
  @Test
  public void testCheckCollectionClass()
  {
    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    for (Iterator<String> i = ObjectCache.getCollectionMapKeys(); i.hasNext();)
    {
      if (i.next().equalsIgnoreCase(NEW_CLASS.getType()))
        return;
    }

    // if we get to here, we didn't find the collection
    Assert.fail("No Cache Collection found for " + NEW_CLASS.getType());
  }

  /**
   * Sets the caching on a type to CACHE_NOTHING and checks that a Collection
   * Class has NOT been made for the type.
   */
  @Request
  @Test
  public void testCheckCollectionClassNoCache()
  {
    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO() );

    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    AttributeEnumeration attributeEnumeration = (AttributeEnumeration) newMdBusiness.getAttributeIF(MdElementInfo.CACHE_ALGORITHM);
    BusinessDAOIF selectedEnumItem = attributeEnumeration.dereference()[0];

    boolean valueSet = false;
    try
    {
      newMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      newMdBusiness.apply();
      valueSet = true;

      for (Iterator<String> i = ObjectCache.getCollectionMapKeys(); i.hasNext();)
      {
        if (i.next().equalsIgnoreCase(NEW_CLASS.getType()))
        {
          Assert.fail("Cache Collection found for " + NEW_CLASS.getType());
        }
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      // Restore original value
      if (valueSet)
      {
        newMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, selectedEnumItem.getOid());
        newMdBusiness.apply();
      }
    }
  }

  /**
   * Checks the existence of a CLASS_INHERITANCE relationship between NewClass
   * and MD_CLASS at the database level
   */
  @Request
  @Test
  public void testDatabaseInheritanceInfo()
  {
    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO() );

    List<RelationshipDAOIF> inheritanceRelationships = RelationshipDAOFactory.getParents(newMdBusiness.getOid(), RelationshipTypes.BUSINESS_INHERITANCE.getType());
    if (inheritanceRelationships.size() != 1)
    {
      Assert.fail("Number of parents of " + NEW_CLASS.getType() + " is " + inheritanceRelationships.size() + ", expected 1.");
    }

    if (!inheritanceRelationships.get(0).getParent().getType().equals(MdBusinessInfo.CLASS))
    {
      Assert.fail(NEW_CLASS + " does not inherit from " + MdBusinessInfo.CLASS);
    }
  }

  /**
   * Deletes the new class and ensures that the table in the database is deleted
   * as well.
   */
  @Request
  @Test
  public void testDeleteTable()
  {
    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO() );

    String tableName = newMdBusiness.getTableName();

    newMdBusiness.delete();

    try
    {
      // Reassign newClassMD so the tearDown() method doesn't try to delete it
      // again and crash
      newMdBusiness = MdBusinessDAO.newInstance();

      if (Database.tableExists(tableName))
      {
        Assert.fail("Table \"" + tableName + "\" not deleted.");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (!Database.tableExists(tableName))
      {
        createNewMdBusiness();
      }
    }
  }

  /**
   * Deletes the NEW_CLASS class, and checks to make sure the core cleans up all
   * references in the database and in the cache
   */
  @Request
  @Test
  public void testDeleteInstances()
  {
    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO() );

    // Instantiate a few copies of the new Class
    BusinessDAO instances[] = new BusinessDAO[12];
    for (int i = 0; i < instances.length; i++)
    {
      instances[i] = BusinessDAO.newInstance(NEW_CLASS.getType());
      instances[i].apply();
    }

    // Make sure the new instances are in the database
    List<String> ids = BusinessDAO.getEntityIdsFromDB(newMdBusiness);
    for (BusinessDAO d : instances)
    {
      if (!ids.contains(d.getOid()))
        Assert.fail("New instance not created correctly.");
    }

    String tableName = newMdBusiness.getTableName();

    // Delete the class definition
    newMdBusiness.delete();

    try
    {
      newMdBusiness = MdBusinessDAO.newInstance();

      // Check to see if the database is clean
      if (Database.tableExists(tableName))
      {
        Assert.fail("Table \"" + tableName + "\" not deleted");
      }

      // Check the cached collection
      for (Iterator<String> i = ObjectCache.getCollectionMapKeys(); i.hasNext();)
      {
        if (i.next().equalsIgnoreCase(NEW_CLASS.getType()))
        {
          Assert.fail("Cache Collection found for " + NEW_CLASS.getType() + " after deletion.");
        }
      }

      // Ensure that the CLASS_INHERITANCE relationship is gone from the
      // database
      List<RelationshipDAOIF> inheritanceRelationships = RelationshipDAOFactory.getParents(newMdBusiness.getOid(), RelationshipTypes.BUSINESS_INHERITANCE.getType());
      if (!inheritanceRelationships.isEmpty())
      {
        Assert.fail("Inheritance relationship not deleted from the database.");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      // Check to see if the database is clean
      if (!Database.tableExists(tableName))
      {
        createNewMdBusiness();
      }
    }
  }

  /**
   * Creates a reference attribute on a class and tests two deletion scenarios:
   * the instance being referred to is deleted, which should result in the
   * attribute being set to blank, and the class that defines the reference type
   * is deleted, which should drop the reference field from the other class
   * completely.
   */
  @Request
  @Test
  public void testDeleteReference()
  {
    if (!Database.tableExists(newClassTableName))
    {
      createNewMdBusiness();
    }

    MdBusinessDAO newMdBusiness = (MdBusinessDAO) ( MdBusinessDAO.getMdBusinessDAO(NEW_CLASS.getType()).getBusinessDAO() );

    TypeInfo deleteMeClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "DeleteMe");

    // Create a new type that we can point to
    MdBusinessDAO mdBusinessTarget = MdBusinessDAO.newInstance();
    mdBusinessTarget.setValue(MdBusinessInfo.NAME, deleteMeClass.getTypeName());
    mdBusinessTarget.setValue(MdBusinessInfo.PACKAGE, deleteMeClass.getPackageName());
    mdBusinessTarget.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary Testing Class");
    mdBusinessTarget.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusinessTarget.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessTarget.apply();

    // Add a reference attribute that points to the new class
    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "foTest");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusinessTarget.getOid());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, newMdBusiness.getOid());
    mdAttributeReference.apply();

    // Create new instances of each class
    BusinessDAO target = BusinessDAO.newInstance(deleteMeClass.getType());
    target.apply();
    BusinessDAO pointer = BusinessDAO.newInstance(NEW_CLASS.getType());
    pointer.setValue("foTest", target.getOid());
    pointer.apply();

    BusinessDAOIF pointerIF = BusinessDAO.get(pointer.getOid());
    // Make sure the reference is being pointed to
    if (!pointerIF.getAttributeIF("foTest").getValue().equals(target.getOid()))
    {
      Assert.fail("Reference Attribute does not point to the expected target.");
    }

    // Now try deleting the target instance - should blank the reference on the
    // pointer
    target.delete();
    pointerIF = BusinessDAO.get(pointer.getOid());
    if (!pointerIF.getAttributeIF("foTest").getValue().equals(""))
    {
      Assert.fail("Reference Attribute should be blank after target is deleted.");
    }

    try
    {
      // Delete the entire type - the foTest attribtue should be dropped
      // completely
      mdBusinessTarget.delete();
      if (newMdBusiness.hasAttribute("foTest"))
      {
        Assert.fail("foTest is still an attribute on " + NEW_CLASS.getType());
      }
    }
    catch (Throwable e)
    {
      // drop the class and rebuild it, thus eliminating the attribute.
      newMdBusiness.delete();
      createNewMdBusiness();
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests if we can create a class with the same name as another class, but use
   * a different package.
   *
   */
  @Request
  @Test
  public void testTwoClassesWithSameClassType()
  {
    TypeInfo someNewClass = new TypeInfo(NEW_CLASS.getPackageName() + ".different", "NewClass");

    MdBusinessDAO newClassWithSameName = null;
    try
    {
      newClassWithSameName = MdBusinessDAO.newInstance();
      newClassWithSameName.setValue(MdBusinessInfo.NAME, someNewClass.getTypeName());
      newClassWithSameName.setValue(MdBusinessInfo.PACKAGE, someNewClass.getPackageName());
      newClassWithSameName.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class different package");
      newClassWithSameName.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      newClassWithSameName.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      newClassWithSameName.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.apply();

      BusinessDAO businessDAO = BusinessDAO.newInstance(someNewClass.getType());
      businessDAO.apply();

    }
    catch (Exception e)
    {
      Assert.fail("Falied to create a class with the same class name as an existing class, but with a different package.");
    }
    finally
    {
      if (newClassWithSameName != null && !newClassWithSameName.isNew())
      {
        newClassWithSameName.delete();
      }
    }
  }

  /**
   * Tests if we can create a class with the same type as another class.
   *
   */
  @Request
  @Test
  public void testTwoClassesWithSameType()
  {
    MdBusinessDAO newClassWithSameName = null;
    try
    {
      newClassWithSameName = MdBusinessDAO.newInstance();
      newClassWithSameName.setValue(MdBusinessInfo.NAME, NEW_CLASS.getTypeName());
      newClassWithSameName.setValue(MdBusinessInfo.PACKAGE, NEW_CLASS.getPackageName());
      newClassWithSameName.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class different package");
      newClassWithSameName.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      newClassWithSameName.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      newClassWithSameName.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.apply();

      Assert.fail("Created a class with the same type an existing class.");
    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
    finally
    {
      if (newClassWithSameName != null && !newClassWithSameName.isNew())
      {
        newClassWithSameName.delete();
      }
    }
  }

  /**
   * Tests if we can create a class where the parent type is invalid.
   *
   */
  @Request
  @Test
  public void testInvalidParentEntityType()
  {
    TypeInfo someNewClass = new TypeInfo(NEW_CLASS.getPackageName() + ".different", "NewClassInvalidParent");

    MdBusinessDAO newClassWithSameName = null;
    try
    {
      MdRelationshipDAOIF mdRelationshipIFmetadata = MdRelationshipDAO.getMdRelationshipDAO(RelationshipTypes.METADATA_RELATIONSHIP.getType());

      newClassWithSameName = MdBusinessDAO.newInstance();
      newClassWithSameName.setValue(MdBusinessInfo.NAME, someNewClass.getTypeName());
      newClassWithSameName.setValue(MdBusinessInfo.PACKAGE, someNewClass.getPackageName());
      newClassWithSameName.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class different package");
      newClassWithSameName.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      newClassWithSameName.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      newClassWithSameName.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdRelationshipIFmetadata.getOid());
      newClassWithSameName.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      newClassWithSameName.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      newClassWithSameName.apply();

      Assert.fail("Created a class where the parent type is a relationship.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
    finally
    {
      if (newClassWithSameName != null && !newClassWithSameName.isNew())
      {
        newClassWithSameName.delete();
      }
    }
  }

  /**
   * Tests that an MdBusiness cannot define an MRU cache without a valid size.
   *
   */
  @Request
  @Test
  public void testMdBusinessUsingMRUNoCacheSize()
  {
    TypeInfo newMdBusiness = new TypeInfo(NEW_CLASS.getPackageName() + ".different", "InvalidMdBusinessCache");

    MdBusinessDAO mdBusiness = null;

    try
    {
      mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, newMdBusiness.getTypeName());
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, newMdBusiness.getPackageName());
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class different package");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.CACHE_SIZE, "0");
      mdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getOid());
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      Assert.fail("Created an MdBusiness that defined an MRU cache without a valid size");
    }
    catch (MetadataException e)
    {
      // we want to land here
    }
    finally
    {
      if (mdBusiness != null && mdBusiness.isAppliedToDB())
        mdBusiness.delete();
    }
  }

  /**
   * Test to make sure you cannot add an attribute to the Component class.
   *
   */
  @Request
  @Test
  public void testAddingAttributeComponent()
  {
    try
    {
      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);

      MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();

      mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "author");
      mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "128");
      mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Author");
      mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
      mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
      mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusinessIF.getOid());
      mdAttrChar.apply();
    }
    catch (CannotAddAttriubteToClassException e)
    {
      // This is expected
    }
  }
}
