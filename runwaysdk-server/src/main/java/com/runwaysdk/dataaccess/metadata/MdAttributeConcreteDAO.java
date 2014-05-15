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
package com.runwaysdk.dataaccess.metadata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import com.runwaysdk.business.Business;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.IndexAttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public abstract class MdAttributeConcreteDAO extends MdAttributeDAO implements MdAttributeConcreteDAOIF
{

  /**
   *
   */
  private static final long             serialVersionUID = 7403740879364877525L;

  protected MdAttributeConcreteStrategy mdAttributeStrategy;

  /**
   * Sometimes temporary columns are created in the middle of a transaction.
   */
  private String                        hashedTempColumnName;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeConcreteDAO()
  {
    super();
    this.init();
  }

  /**
   * Constructs a {@link MdAttributeConcreteDAO} from the given {@link Map} of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeConcreteDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
    this.init();
  }

  /**
   * Initializes some invariants.
   */
  private void init()
  {
    this.hashedTempColumnName = null;
  }

  /**
   * The object's key is the hash code.
   * 
   */
  public int hashCode()
  {
    return this.getKey().hashCode();
  }

  /**
   * Returns a MdAttributeIF that uses the database index of a given name.
   * 
   * <br/>
   * <b>Precondition:</b> indexName != null <br/>
   * <b>Precondition:</b> !indexName.trim().equals("") <br/>
   * <b>Postcondition:</b> Returns a MdAttributeIF where
   * (MdAttributeIF.getIndexName().equals(indexName)
   * 
   * @param indexName
   *          Name of the database index.
   * @return MdAttributeIF that uses the database index of a given name.
   */
  public static MdAttributeConcreteDAOIF getMdAttributeWithIndex(String indexName)
  {
    return (MdAttributeConcreteDAOIF) ObjectCache.getMdAttributeDAOWithIndex(indexName);
  }

  /**
   * Returns the <code>MdClassDAOIF</code> that defines this <code>MdAttributeDAO</code>.
   * 
   * @return the <code>MdClassDAOIF</code> that defines this <code>MdAttributeDAO</code>.
   */
  public MdClassDAOIF definedByClass()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeConcreteInfo.DEFINING_MD_CLASS);
    return (MdClassDAOIF) attributeReference.dereference();
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
    return ( (AttributeLocalIF) this.getAttributeIF(MdAttributeConcreteInfo.DISPLAY_LABEL) ).getValue(locale);
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
   * Returns the concrete attribute representing this attribute. This is a
   * concrete attribute so itself is returned.
   * 
   * @return concrete attribute representing this attribute.
   */
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    return this;
  }

  /**
   * Creates the relationship such that the given virtual attribute virtualizes
   * this attribute.
   * 
   * @param mdAttributeVirtualIF
   *          attribute that virtualizes this one.
   */
  protected void addAttributeVirtual(MdAttributeVirtualDAOIF mdAttributeVirtualIF)
  {
    RelationshipDAO newChildRelDAO = this.addChild(mdAttributeVirtualIF, RelationshipTypes.VIRTUALIZE_ATTRIBUTE.getType());
    newChildRelDAO.setKey(mdAttributeVirtualIF.getKey());
    newChildRelDAO.save(true);
  }

  /**
   * Returns the strategy object with specific CRUD logic.
   * 
   * @return strategy object with specific CRUD logic.
   */
  protected MdAttributeConcreteStrategy getMdAttributeStrategy()
  {
    if (this.mdAttributeStrategy == null)
    {
      if (this.getAttributeIF(MdAttributeConcreteInfo.DEFINING_MD_CLASS).getValue().trim().equals(""))
      {
        String errorMessage = "[" + this.getClass().getName() + "] has not properly initialized the field that contains the strategy object.  " + "The field [" + MdAttributeConcreteInfo.DEFINING_MD_CLASS + "] needs a valid value before the strategy object can be initialized.";
        throw new ProgrammingErrorException(errorMessage);
      }
      else
      {
        initializeStrategyObject();
      }
    }

    return this.mdAttributeStrategy;
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttribute requires at the DTO
   * Layer.
   * 
   * @return class name of the AttributeMdDTO to represent this MdAttribute
   */
  public abstract String attributeMdDTOType();

  /**
   * Returns a list of group <code>MdIndexDAOIF</code> that this attribute participates in.
   * 
   * @return
   */
  public List<MdIndexDAOIF> getMdIndecies()
  {
    List<MdIndexDAOIF> mdIndexIFList = new LinkedList<MdIndexDAOIF>();

    List<RelationshipDAOIF> relationships = this.getParents(IndexAttributeInfo.CLASS);

    for (RelationshipDAOIF relationship : relationships)
    {
      mdIndexIFList.add((MdIndexDAOIF) relationship.getParent());
    }

    return mdIndexIFList;
  }
  
  /**
   * Returns a list of group <code>IndexAttributeIF</code> that this attribute participates in.
   * 
   * @return
   */
  public List<IndexAttributeIF> getIndexAttributeRels()
  {
    List<IndexAttributeIF> indexAttributeRels = new LinkedList<IndexAttributeIF>();

    List<RelationshipDAOIF> relationships = this.getParents(IndexAttributeInfo.CLASS);

    for (RelationshipDAOIF relationship : relationships)
    {
      indexAttributeRels.add((IndexAttributeIF) relationship);
    }

    return indexAttributeRels;
  }

  /**
   * Returns the name of the Attribute that this Attribute.ATTRIBUTE defines.
   * 
   * @return the name of the Attribute that this Attribute.ATTRIBUTE defines.
   */
  public String definesAttribute()
  {
    return this.getAttributeIF(MdAttributeConcreteInfo.NAME).getValue();
  }

  /**
   * Returns the name of the database index used to index this attribute, or an
   * empty String if no index exists.
   * 
   * @return name of the database index used to index this attribute, or an
   *         empty String if no index exists.
   */
  public String getIndexName()
  {
    return this.getAttributeIF(MdAttributeConcreteInfo.INDEX_NAME).getValue();
  }

  /**
   * Sets the name of the database index used to index this attribute.
   */
  protected void setIndexName(String indexName)
  {
    this.getAttribute(MdAttributeConcreteInfo.INDEX_NAME).setValue(indexName);
  }

  /**
   * Returns true if the attribute is immutable, false otherwise. Immutable
   * attributes cannot have their value changed after they receive an initial
   * value.
   * 
   * @return true if the attribute is immutable, false otherwise.
   */
  public boolean isImmutable()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeConcreteInfo.IMMUTABLE) ).isTrue();
  }

  /**
   * Returns true if the attribute is a system attribute, false otherwise.
   * System attributes can only be modified by the core.
   * 
   * @return true if the attribute is a system attribute, false otherwise.
   */
  public boolean isSystem()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeConcreteInfo.SYSTEM) ).isTrue();
  }

  /**
   * Returns true if instances of this attribute require a value, false
   * otherwise.
   * 
   * @return true if instances of this attribute require a value, false
   *         otherwise.
   */
  public boolean isRequired()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).isTrue();
  }

  /**
   * Returns the name of the column in the database, which may be a temporary
   * hashed column during the middle of a transaction.
   * 
   * @return the name of the column in the database, which may be a temporary
   *         hashed column during the middle of a transaction.
   */
  public String getColumnName()
  {
    if (this.hashedTempColumnName != null)
    {
      return this.hashedTempColumnName;
    }
    else
    {
      return this.getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    }
  }

  /**
   * Returns the name of the column in the database as it is in this metadata.
   * 
   * @return name of the column in the database as it is in this metadata.
   */
  public String getDefinedColumnName()
  {
    return this.getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
  }

  /**
   * Sets the name of a uniquely hashed temporary column for this attribute.
   * Sometimes during the middle of a transaction a temporary column is used,
   * but then cleaned up at the end of the transaction.
   * 
   * @param _hashedTempColumnName
   */
  public void setHashedTempColumnName(String _hashedTempColumnName)
  {
    this.hashedTempColumnName = _hashedTempColumnName;
  }

  /**
   * SEts the name of the column in the database.
   * 
   * @param name
   *          of the column in the database.
   */
  public void setColumnName(String columnName)
  {
    this.getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValue(columnName);
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   * 
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return "";
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   * 
   * @return the default value for the attribute that this metadata defines.
   */
  protected static String getDefaultValue(String attrDefaultValue)
  {
    if (attrDefaultValue == null || attrDefaultValue.equalsIgnoreCase("NULL"))
    {
      attrDefaultValue = "";
    }
    return attrDefaultValue;
  }

  /**
   * Returns the visibility modifier of the getter.
   * 
   * @return the visibility modifier of the getter.
   */
  public VisibilityModifier getGetterVisibility()
  {
    AttributeEnumerationIF attrEnum = (AttributeEnumerationIF) this.getAttributeIF(MdAttributeConcreteInfo.GETTER_VISIBILITY);

    BusinessDAOIF modifier = attrEnum.dereference()[0];

    String enumName = modifier.getAttributeIF(EnumerationMasterInfo.NAME).getValue();

    return VisibilityModifier.valueOf(enumName);
  }

  /**
   * Returns the visibility modifier of the setter.
   * 
   * @return the visibility modifier of the setter.
   */
  public VisibilityModifier getSetterVisibility()
  {
    AttributeEnumerationIF attrEnum = (AttributeEnumerationIF) this.getAttributeIF(MdAttributeConcreteInfo.SETTER_VISIBILITY);

    BusinessDAOIF modifier = attrEnum.dereference()[0];

    String enumName = modifier.getAttributeIF(EnumerationMasterInfo.NAME).getValue();

    return VisibilityModifier.valueOf(enumName);
  }

  /**
   * Takes the provided value string, and wraps it in the java code to call
   * {@link Business#setValue(String, String)}. The value String is actually a
   * String representing java code that can be inserted into generated classes,
   * converting typesafe input into the required String type.
   * 
   * The standard usage model is that concrete MdAttribute classes will generate
   * the code to converate their typesafe input into a String, then pass that
   * code to this method, which generates the call the generic setter.
   * 
   * @param value
   *          Code that converts typesafe input into a String
   * @return Code to set this attribute
   */
  public String setterWrapper(String value)
  {
    return "setValue(" + this.definesAttribute().toUpperCase() + ", " + value + ")";
  }

  /**
   * Takes the provided value string, and wraps it in the java code to call
   * {@link Business#setValue(String, String)}. The value String is actually a
   * String representing java code that can be inserted into generated classes,
   * converting typesafe input into the required String type.
   * 
   * The standard usage model is that concrete MdAttribute classes will generate
   * the code to converate their typesafe input into a String, then pass that
   * code to this method, which generates the call the generic setter.
   * 
   * @param attributeName
   *          Name of the attribute
   * @param value
   *          Code that converts typesafe input into a String
   * @return Code to set this attribute
   */
  protected String setterWrapper(String attributeName, String value)
  {
    return "setValue(" + attributeName.toUpperCase() + ", " + value + ")";
  }

  /**
   * Generates the code to format type unsafe input (A String) into the correct
   * type. The general case makes no changes, but children override to make
   * changes. It is imporant to rememebr that this is for code generation -
   * we're creating strings that contain java code that does the formatting.
   * 
   * @param formatMe
   *          java code that returns (or is) a String
   * @return Code that returns type-safe representation of formatMe
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    return formatMe;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#generatedServerGetter()
   */
  public String generatedServerGetter()
  {
    return this.generatedServerGetter(this.definesAttribute());
  }

  protected String generatedServerGetter(String attributeName)
  {
    return generateTypesafeFormatting("getValue(" + attributeName.toUpperCase() + ")");
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#generatedServerGetter()
   */
  public String generatedClientGetter()
  {
    return this.generatedClientGetter(this.definesAttribute());
  }

  protected String generatedClientGetter(String attributeName)
  {
    return this.generatedServerGetter(attributeName);
  }

  /**
   * Generates the default setValue method for the server, which passes the
   * parameter directly to {@link Business#setValue(String, String)}. Clearly
   * this only works when the parameter is already a String. Attributes that
   * require parsing of typesafe inputs override this method.
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#generatedServerSetter()
   */
  public String generatedServerSetter()
  {
    return this.generatedServerSetter(this.definesAttribute());
  }

  protected String generatedServerSetter(String attributeName)
  {
    return this.setterWrapper(attributeName, "value");
  }

  /**
   * Generates the default setValue method for the client, which passes the
   * parameter directly to {@link Business#setValue(String, String)}. Clearly
   * this only works when the parameter is already a String. Attributes that
   * require parsing of typesafe inputs override this method.
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeDAOIF#generatedClientSetter()
   */
  public String generatedClientSetter()
  {
    return this.generatedClientSetter(this.definesAttribute());
  }

  protected String generatedClientSetter(String attributeName)
  {
    return this.generatedServerSetter(attributeName);
  }

  public String toString()
  {
    return "MdAttribute " + this.getAttributeIF(MdAttributeConcreteInfo.NAME).getValue();
  }

  /**
   * Validates this metadata object.
   * 
   * @throws DataAccessException
   *           when this MetaData object is not valid.
   */
  protected void validate()
  {
    AttributeIF attributeName = this.getAttributeIF(MdAttributeConcreteInfo.NAME);
    if (attributeName.isModified())
    {
      this.validateAttributeName(attributeName.getValue());
    }

    AttributeIF columnName = this.getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME);
    if (!this.isImport() && columnName.isModified())
    {
      validateColumnName(columnName.getValue());
    }

    super.validate();

    // make sure that no attributes are added to MdAttributes
    MdClassDAOIF mdClassIF = this.definedByClass();
    List<? extends MdClassDAOIF> superClasses = mdClassIF.getSuperClasses();
    for (MdClassDAOIF superClass : superClasses)
    {
      String type = superClass.definesType();

      if (type.equals(MdAttributeConcreteInfo.CLASS))
      {
        MdClassDAOIF definingClass = this.definedByClass();
        String error = "Attribute [" + definesAttribute() + "] cannot be modified because its defining type, [" + definingClass.definesType() + "], is an [" + MdAttributeInfo.CLASS + "].";
        throw new CannotAddAttriubteToClassException(error, this, definingClass);
      }
    }
  }

  /**
   * Returns true if the attribute should be unique, false otherwise.
   * 
   * @return true if the attribute should be unique, false otherwise.
   */
  public boolean isUnique()
  {
    AttributeEnumerationIF unique = (AttributeEnumerationIF) this.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
    if (unique.dereference()[0].getId().equalsIgnoreCase(IndexTypes.UNIQUE_INDEX.getId().toLowerCase()))
      return true;
    else
      return false;
  }

  /**
   * Returns true if the attribute participates in an individual non-unique
   * column index, false otherwise.
   * 
   * @return true if the attribute participates in an individual non-unique
   *         column index, false otherwise.
   */
  public boolean hasNonUniqueIndex()
  {
    AttributeEnumerationIF unique = (AttributeEnumerationIF) this.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
    if (unique.dereference()[0].getId().equalsIgnoreCase(IndexTypes.NON_UNIQUE_INDEX.getId().toLowerCase()))
      return true;
    else
      return false;
  }

  /**
   * Returns true if the attribute participates in an individual column index,
   * false otherwise.
   * 
   * @return true if the attribute participates in an individual column index,
   *         false otherwise.
   */
  public boolean hasIndividualIndex()
  {
    return this.isUnique() || this.hasNonUniqueIndex();
  }

  /**
   * Returns true if the attribute is part of a group of indexed attributes,
   * false otherwise.
   * 
   * @return true if the attribute is part of a group of indexed attributes,
   *         false otherwise.
   */
  public boolean isPartOfIndexedAttributeGroup()
  {
    if (this.getParents(IndexAttributeInfo.CLASS).size() > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Sets isNew = false and sets all attributes to isModified = false.
   * 
   */
  public void setCommitState()
  {
    super.setCommitState();
    this.getMdAttributeStrategy().setCommitState();
    this.hashedTempColumnName = null;
  }

  /**
   * Applies the state of this BusinessDAO to the database. If this is a new
   * BusinessDAO, then records are created in the database and an ID is created.
   * If this is not a new BusinessDAO, then records are modified in the
   * database.
   * 
   * <br/>
   * <b>Precondition:</b> Attribues must have correct values as defined in their
   * meta data. <br/>
   * <b>Postcondition:</b> state of the BusinessDAO is preserved in the
   * database. <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @param validateRequired
   *          ture if attributes should be checked for required values, false
   *          otherwise. StructDAOs used for struct attributes may or may not
   *          need required attributes validated.
   * @return ID of the BusinessDAO.
   * @throws DataAccessException
   *           if an attribute contains a value that is not correct with respect
   *           to the metadata.
   */
  public String save(boolean validateRequired)
  {
    boolean isApplied = this.isAppliedToDB();

    this.initializeStrategyObject();

    MdAttributeConcreteStrategy strategy = this.getMdAttributeStrategy();
    strategy.preSaveValidate();

    MdClassDAOIF definingClass = this.definedByClass();

    if (this.isNew())
    {
      if (definingClass instanceof MdEntityDAOIF)
      {
        String indexName = Database.attributeIndexName( ( (MdEntityDAOIF) definingClass ).getTableName(), this.getColumnName());

        this.setIndexName(indexName);
      }
    }
    else
    {
      if (this.getAttribute(MdAttributeConcreteInfo.NAME).isModified())
      {
        ObjectCache.refreshTheEntireCache();
      }
    }

    String id = super.save(validateRequired);

    strategy.setAppliedToDB(isApplied);

    strategy.save();

    return id;
  }

  /**
   * Initializes the strategy object.
   */
  protected abstract void initializeStrategyObject();

  /**
   * Deletes an attribute from the runway. The BusinessDAO is deleted from the
   * database and removed from the cache. All relationships pertaining to this
   * BusinessDAO are also removed as well.
   * 
   * <br/>
   * <b>Postcondition: </b> BusinessDAO and all dependencies are removed from
   * the runway <br/>
   * <b>Postcondition: </b> Coresponding column from the defining table is
   * dropped
   * 
   * @param p_mdAttribute
   *          Attribute metadata BusinessDAO
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void delete(boolean businessContext)
  {
    // Remove virtual attributes that point to this attribute.
    QueryFactory queryFactory = new QueryFactory();
    BusinessDAOQuery mdAttrVirtualQ = queryFactory.businessDAOQuery(MdAttributeVirtualInfo.CLASS);
    mdAttrVirtualQ.WHERE(mdAttrVirtualQ.aReference(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE).EQ(this.getId()));

    OIterator<BusinessDAOIF> mdAttrVirtIerator = mdAttrVirtualQ.getIterator();
    try
    {
      while (mdAttrVirtIerator.hasNext())
      {
        MdAttributeVirtualDAOIF mdAttributeVirtualIF = (MdAttributeVirtualDAOIF) mdAttrVirtIerator.next();

        mdAttributeVirtualIF.getBusinessDAO().delete();
      }
    }
    finally
    {
      mdAttrVirtIerator.close();
    }

    this.initializeStrategyObject();

    this.getMdAttributeStrategy().delete();

    super.delete(businessContext);

    this.getMdAttributeStrategy().postDelete();
  }

  /**
   * Changes they key of the relationship object that binds the defining class with this metadata attribute.
   * The key of the relationship is the same as the key of this object.
   * 
   * <br/>
   * <b>Precondition:</b> Assumes the key of this object has changed.
   */
  public void changeClassAttributeRelationshipKey()
  {
    Attribute keyAttribute = this.getAttribute(ComponentInfo.KEY);

    List<RelationshipDAOIF> relList = this.getParents(this.definedByClass(), RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
      
    // Making assumptions that an attribute can only be defined by a class once
    RelationshipDAO relationshipDAO = relList.get(0).getRelationshipDAO();   
    relationshipDAO.setKey(keyAttribute.getValue());
    
    relationshipDAO.save(true);
  }
  
  public static MdAttributeConcreteDAOIF get(String id)
  {
    return (MdAttributeConcreteDAOIF) BusinessDAO.get(id);
  }

  /**
   * Returns the <code>MdAttributeIF</code> with the given key.
   * 
   * @param key
   * @return <code>MdAttributeIF</code> with the given key.
   */
  public static MdAttributeConcreteDAOIF getByKey(String key)
  {
    return (MdAttributeConcreteDAOIF) MdAttributeDAO.getByKey(key);
  }

  /**
   * Throws {@link InvalidColumnNameException} exception if the given name is
   * not a valid database column name.
   * 
   * @param validateName
   *          name to validate
   * @throws {@link InvalidColumnNameException} exception if the given name is
   *         not a valid database column name.
   */
  public static void validateColumnName(String columnName)
  {
    Pattern namePattern = Pattern.compile("[\\p{L}][\\p{L}0-9_]*");

    if (!namePattern.matcher(columnName).matches())
    {
      String error = "[" + columnName + "] is not a valid column name.";
      throw new InvalidColumnNameException(error, columnName);
    }

    if (ReservedWords.sqlContains(columnName))
    {
      throw new ReservedWordException("The column name [" + columnName + "] is reserved.", columnName, ReservedWordException.Origin.COLUMN);
    }
  }

}
