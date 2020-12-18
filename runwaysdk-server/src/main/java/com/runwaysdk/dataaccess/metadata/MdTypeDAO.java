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
package com.runwaysdk.dataaccess.metadata;

import java.sql.Connection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.transaction.TransactionState;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * Marker class
 * 
 * @author nathan
 * 
 */
public abstract class MdTypeDAO extends MetadataDAO implements MdTypeDAOIF
{
  /**
   * 
   */
  private static final long    serialVersionUID = 5306047828291803927L;

  // /**
  // * Regex pattern to match type names.
  // */
  // private static final Pattern namePattern =
  // Pattern.compile("[A-Z]|([A-Z]+(\\w)*[^_$])");

  /**
   * Regex pattern to match package names.
   */
  // private static final Pattern packagePattern =
  // Pattern.compile("((\\w)+\\.?)*(\\w)+");
  private static final Pattern packagePattern   = Pattern.compile("(([_a-zA-Z]+(\\w)*)+\\.?)*([_a-zA-Z]+(\\w)*)+");

  /**
   * The default constructor, does not set any attributes
   */
  public MdTypeDAO()
  {
    super();
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Type:" + this.definesType();
  }

  /**
   * Constructs a MdType from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   * 
   * @param attributeMap
   * @param type
   */
  public MdTypeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public static MdTypeDAOIF getMdTypeDAO(String type)
  {
    MdTypeDAOIF mdType = ObjectCache.getMdTypeDAO(type);

    if (mdType == null)
    {
      String error = "MdType [" + type + "] was not found.";
      throw new DataNotFoundException(error, getMdTypeDAO(MdTypeInfo.CLASS));
    }

    return mdType;
  }

  /**
   * Returns the name of the type that this MdType definess.
   * 
   * @return the name of the type that this MdType definess.
   */
  public String getTypeName()
  {
    return this.getAttributeIF(MdTypeInfo.NAME).getValue();
  }

  public String getRootId()
  {
    Attribute attribute = this.getAttribute(MdTypeInfo.ROOT_ID);
    String value = attribute.getValue();

    if (value.length() == 0)
    {
      value = this.generateRootId();

      attribute.setValue(value);
    }

    return value;
  }

  /**
   * Returns the name of the package of the type that this object defines.
   * 
   * @return name of the package of the type that this object defines.
   */
  public String getPackage()
  {
    return this.getAttributeIF(MdTypeInfo.PACKAGE).getValue();
  }

  /**
   * Returns the name of the package of the type that this object defines.
   * 
   * @return name of the package of the type that this object defines.
   */
  public Boolean isGenerateSource()
  {
    if (this.hasAttribute(MdTypeInfo.GENERATE_SOURCE))
    {
      String value = this.getAttributeIF(MdTypeInfo.GENERATE_SOURCE).getValue();

      if (value != null && value.length() > 0)
      {
        return new Boolean(value);
      }
    }

    return new Boolean(true);
  }

  /**
   * Returns true if this object defines a type in the system package, false
   * otherwise.
   * 
   * @return true if this object defines a type in the system package, false
   *         otherwise.
   */
  public boolean isSystemPackage()
  {
    return MdTypeDAO.isSystemPackageMetadata(this.getPackage());
  }

  /**
   * Returns true if the object is exported, false otherwise.
   * 
   * @return true if the object is exported, false otherwise.
   */
  public boolean isExported()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdTypeInfo.EXPORTED) ).getBooleanValue();
  }

  /**
   * Returns true if the package name contains system metadata classes, false
   * otherwise. Assumes the package name does not represent a package on the
   * file system. Assumes "."'s instead of "/"'s.
   * 
   * @param packageName
   * 
   * @return true if the package contains system metadata classes, false
   *         otherwise.
   */
  public static boolean isSystemPackageMetadata(String packageName)
  {
    if (packageName.indexOf(Constants.SYSTEM_PACKAGE) == 0 || packageName.indexOf(Constants.SYSTEM_BUSINESS_PACKAGE) == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns true if the package directory contains system metadata classes,
   * false otherwise. Assumes the package has been converted to a directory,
   * where the "."'s have been replaced with "/"'s.
   * 
   * @param fileSystemPackage
   * 
   * @return true if the package contains system metadata classes, false
   *         otherwise.
   */
  public static boolean isSystemPackageFileSystem(String fileSystemPackage)
  {
    String systemPackage = CommonGenerationUtil.replacePackageDotsWithSlashes(Constants.SYSTEM_PACKAGE);
    String systemBusinessPackage = CommonGenerationUtil.replacePackageDotsWithSlashes(Constants.SYSTEM_BUSINESS_PACKAGE);
    if (fileSystemPackage.indexOf(systemPackage) == 0 || fileSystemPackage.indexOf(systemBusinessPackage) == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the type that this object defines. The type consits of the package
   * plus the type name.
   * 
   * @return the type that this object defines.
   */
  public String definesType()
  {
    return EntityDAOFactory.buildType(this.getPackage(), this.getTypeName());
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param locale
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /**
   * This is a hook method for aspects. We do not want to regenerate code on
   * save() vs. apply() because code generation is not necessary for locking and
   * or unlocking an MdType instance.
   */
  public String apply()
  {
    this.setKey(buildKey(this.definesType()));

    return super.apply();
  }

  public static String buildKey(String type)
  {
    return type;
  }

  public static String buildKey(String packageName, String typeName)
  {
    return EntityDAOFactory.buildType(packageName, typeName);
  }

  /**
   * Validates this metadata object.
   * 
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    if (this.isNew())
    {
      this.validateNew();
    }
    else
    {
      this.validateExisting();
    }

    super.validate();
  }

  /**
   * Validates a metadata object if it is new.
   */
  protected void validateNew()
  {
    if (!this.isAppliedToDB() && EntityDAOFactory.isValidType(this.definesType()))
    {
      MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(MdTypeInfo.CLASS);
      List<MdAttributeDAOIF> attributeIFList = new LinkedList<MdAttributeDAOIF>();
      attributeIFList.add(this.getAttributeIF(MdTypeInfo.PACKAGE).getMdAttribute());
      attributeIFList.add(this.getAttributeIF(MdTypeInfo.NAME).getMdAttribute());

      List<String> values = new LinkedList<String>();
      values.add(this.getAttributeIF(MdTypeInfo.PACKAGE).getValue());
      values.add(this.getAttributeIF(MdTypeInfo.NAME).getValue());

      String error = "A type named [" + this.definesType() + "] already exists";
      throw new DuplicateDataException(error, mdEntityIF, attributeIFList, values);
    }

    // class name
    String typeName = this.getTypeName();
    boolean invalidTypeName = false;
    for (int i = 0; i < typeName.length(); i++)
    {
      if ( ( i == 0 && !Character.isJavaIdentifierStart(typeName.charAt(i)) ) || !Character.isJavaIdentifierPart(typeName.charAt(i)))
        invalidTypeName = true;
    }
    // if (!namePattern.matcher(typeName).matches())
    if (invalidTypeName)
    {
      String error = "[" + typeName + "] is not a proper type name.";
      throw new NameConventionException(error, typeName);
    }

    // package name
    String pack = this.getPackage();

    if (!packagePattern.matcher(pack).matches())
    {
      String error = "[" + pack + "] is not a proper package name.";
      throw new NameConventionException(error, pack);
    }
  }

  /**
   *
   *
   */
  protected void validateExisting()
  {

  }

  /**
   * Returns true if this MdType object is defined in the database, false
   * otherwise.
   * 
   * @return true if this MdType object is defined in the database, false
   *         otherwise.
   */
  public boolean isDefined()
  {
    return MdTypeDAO.isDefined(this.definesType());
  }

  /**
   * Checks the system to see if metadata is defined for the given type. This
   * basically is a check to see if the type (class, relationship, or
   * enumeration) is already defined in the system. This method queries the
   * database.
   * 
   * @param type
   *          type to check for
   * @return <b>true</b> if the name is already defined
   */
  public static boolean isDefined(String type)
  {
    return EntityDAOFactory.typeExists(type);
  }

  /**
   * Creates the name of a table to store records for the given type.
   * 
   * @param type
   * @return name of a table to store records for the given type.
   */
  protected static String createTableName(String type)
  {
    String tableName = type.toLowerCase();

    // truncate the class name to the maximum size of a table name
    if (tableName.length() > Database.MAX_DB_IDENTIFIER_SIZE)
    {
      tableName = tableName.substring(0, Database.MAX_DB_IDENTIFIER_SIZE);
    }

    if (!Database.tableExists(tableName) && !ObjectCache.hasClassByTableName(tableName))
    {
      return tableName;
    }

    int maxTablePrefix = Database.MAX_DB_IDENTIFIER_SIZE - 3;
    if (tableName.length() > maxTablePrefix)
    {
      tableName = tableName.substring(0, maxTablePrefix);
    }

    String tempTableName = tableName;
    for (int i = 0; i < 1000; i++)
    {
      tempTableName += i;
      if (!Database.tableExists(tempTableName) && !ObjectCache.hasClassByTableName(tempTableName))
      {
        return tempTableName;
      }
      tempTableName = tableName;
    }

    // In this rare case, create a unique string for the table name
    tableName = ServerIDGenerator.nextID().toLowerCase();
    // prepend the table name with an A
    tableName = "a" + tableName.substring(1, Database.MAX_DB_IDENTIFIER_SIZE);

    return tableName;
  }

  /**
   * Returns a command object that either creates or updates Java artifacts for
   * this type or returns null if there are no artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that either creates or updates Java artifacts for
   *         this type.
   */
  public abstract Command getCreateUpdateJavaArtifactCommand(Connection conn);

  /**
   * Returns a command object that deletes Java artifacts for this type or
   * returns null if there are no artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that deletes Java artifacts for this type.
   */
  public abstract Command getDeleteJavaArtifactCommand(Connection conn);

  /**
   * Returns a command object that cleans Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that cleans Java artifacts for this type.
   */
  public abstract Command getCleanJavaArtifactCommand(Connection conn);

  /**
   * Copies all Java source and class files from this object into files on the
   * file system.
   */
  public abstract void writeJavaToFile();

  /**
   * Returns true if this object has been modified such that code should be
   * generated/regenerated, false otherwise.
   * 
   * @return true if this object has been modified such that code should be
   *         generated/regenerated, false otherwise.
   */
  public boolean shouldGenerateCode()
  {
    boolean shouldGenerateCode = false;

    // If this is a new type, then we want to generate code.
    if (this.isNew())
    {
      shouldGenerateCode = true;
    }
    else
    {
      // We do not want to generate code if the only attribute that has been
      // modified is the LOCKED_BY field.
      for (AttributeIF attributeIF : this.getAttributeArrayIF())
      {
        if (! ( attributeIF instanceof AttributeStruct ) && attributeIF.isModified() && !attributeIF.getName().equals(ElementInfo.LOCKED_BY))
        {
          shouldGenerateCode = true;
        }
      }
    }

    return shouldGenerateCode;
  }

  /**
   * Copies all Java source and class files from the file system and stores them
   * in the database.
   * 
   * @param conn
   *          database connection object. This method is used during the a
   *          transaction. Consequently, the transaction must be managed
   *          manually.
   */
  public abstract void writeFileArtifactsToDatabaseAndObjects(Connection conn);

  /**
   * Returns true if an attribute that stores source or class has been modified.
   * 
   * @return true if an attribute that stores source or class has been modified.
   */
  public abstract boolean javaArtifactsModifiedOnObject();

  /**
   * Returns MdMethodIF object of the method with the given name defined by this
   * MdEntityIF.
   * 
   * @return MdMethodIF object of the method with the given name defined by this
   *         MdEntityIF.
   */
  public MdMethodDAOIF getMdMethod(String methodName)
  {
    List<RelationshipDAOIF> relationshipArray = this.getChildren(RelationshipTypes.MD_TYPE_MD_METHOD.getType());

    for (int i = 0; i < relationshipArray.size(); i++)
    {
      RelationshipDAOIF relationship = relationshipArray.get(i);
      MdMethodDAOIF mdMethodIF = (MdMethodDAOIF) relationship.getChild();

      if (mdMethodIF.getName().equalsIgnoreCase(methodName.trim()))
      {
        return mdMethodIF;
      }
    }

    return null;
  }

  /**
   * Returns an List of MdMethodIF objects that this MdEntityIF defines.
   * 
   * @return an List of MdMethodIF objects that this MdEntityIF defines.
   */
  public List<MdMethodDAOIF> getMdMethods()
  {
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.MD_TYPE_MD_METHOD.getType());
    List<MdMethodDAOIF> list = new LinkedList<MdMethodDAOIF>();

    for (RelationshipDAOIF relationship : relationships)
    {
      MdMethodDAOIF mdMethod = (MdMethodDAOIF) relationship.getChild();

      list.add(mdMethod);
    }

    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdTypeIF#getMdMethodsOrdered()
   */
  public List<MdMethodDAOIF> getMdMethodsOrdered()
  {
    List<MdMethodDAOIF> list = this.getMdMethods();

    Collections.sort(list, MdMethodDAO.alphabetical);

    return list;
  }

  /**
   * Deletes all MdMethods defined by this type.
   */
  public void dropAllMdMethods()
  {
    for (MdMethodDAOIF mdMethodDAOIF : this.getMdMethods())
    {
      mdMethodDAOIF.getBusinessDAO().delete();
    }
  }

  /**
   * Updates the java source and class into the database.
   * 
   * @param conn
   * @param baseSource
   * @param baseClassBytes
   * @param dtoBaseClass
   * @param dtoBaseSource
   */
  protected void updateBaseClassAndSource(Connection conn, String baseSource, byte[] baseClassBytes, byte[] dtoBaseClass, String dtoBaseSource)
  {
    if (baseSource != null && baseClassBytes != null && dtoBaseClass != null && dtoBaseSource != null)
    {
      String classColumnName = MdTypeDAOIF.BASE_CLASS_COLUMN;
      String sourceColumnName = MdTypeDAOIF.BASE_SOURCE_COLUMN;
      String dtoClassColumn = MdTypeDAOIF.DTO_BASE_CLASS_COLUMN;
      String dtoSourceColumn = MdTypeDAOIF.DTO_BASE_SOURCE_COLUMN;

      Database.updateClassAndSource(this.getOid(), MdTypeDAOIF.TABLE, classColumnName, baseClassBytes, sourceColumnName, baseSource, conn);
      Database.updateClassAndSource(this.getOid(), MdTypeDAOIF.TABLE, dtoClassColumn, dtoBaseClass, dtoSourceColumn, dtoBaseSource, conn);

      // Only update the source. The blob attributes just point to the database
      // anyway.
      this.getAttribute(MdTypeInfo.BASE_SOURCE).setValue(baseSource);
      this.getAttribute(MdTypeInfo.DTO_BASE_SOURCE).setValue(dtoBaseSource);
    }
  }

  @Override
  // Do not delete even though this just calls super.
  // This is a hook method for aspects.
  public String save(boolean validateRequired)
  {
    if (this.isNew())
    {
      Attribute attribute = this.getAttribute(MdTypeInfo.ROOT_ID);
      String value = attribute.getValue();

      if (value == null || value.length() == 0)
      {
        attribute.setValue(generateRootId());
      }
    }

    if (!this.isNew() || this.isAppliedToDB())
    {
      Attribute keyAttribute = this.getAttribute(MdTypeInfo.KEY);

      // Change the key on method
      if (keyAttribute.isModified())
      {
        List<RelationshipDAOIF> relList = this.getChildren(RelationshipTypes.MD_TYPE_MD_METHOD.getType());
        for (RelationshipDAOIF relationshipDAOIF : relList)
        {
          MdMethodDAO mdMethodDAO = (MdMethodDAO) relationshipDAOIF.getChild().getBusinessDAO();
          mdMethodDAO.apply();
        }
      }
    }

    return super.save(validateRequired);
  }

  private String generateRootId()
  {
    return Database.generateRootId(this);
  }

  public static MdTypeDAOIF get(String oid)
  {
    return (MdTypeDAOIF) BusinessDAO.get(oid);
  }

  /**
   * 
   */
  public void markToWriteNewArtifact()
  {
    TransactionState.getCurrentTransactionState().markToWriteNewArtifact(this);
  }
}
