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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.FileIO;

public abstract class MdClassDAO extends MdTypeDAO implements MdClassDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 253972855560556745L;

  /**
   * Name of the type that is the root of the hierarchy that this type is a
   * member of.
   */
  private String            rootType         = null;

  private List<String>      superTypeNames   = null;

  /**
   * The default constructor, does not set any attributes
   */
  public MdClassDAO()
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
    String signature = super.getSignature() + " Attributes[";

    boolean firstIteration = true;
    for (MdAttributeDAOIF mdAttributeDAOIF : this.definesAttributesOrdered())
    {
      if (!firstIteration)
      {
        signature += ", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdAttributeDAOIF.getSignature();
    }
    signature += "]";

    signature += " Methods[";
    firstIteration = true;
    for (MdMethodDAOIF mdMethodDAOIF : this.getMdMethodsOrdered())
    {
      if (!firstIteration)
      {
        signature += ", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdMethodDAOIF.getSignature();
    }
    signature += "]";

    return signature;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdTypeDAO
   */
  public MdClassDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  @Override
  public String apply()
  {
    MdClassDAOIF superMdClass = this.getSuperClass();

    if (superMdClass != null)
    {
      String value = superMdClass.getAttributeIF(MdClassInfo.PUBLISH).getValue();
      this.getAttribute(MdClassInfo.PUBLISH).setValue(value);
    }

    if (superMdClass != null)
    {
      this.getAttribute(MdClassInfo.PUBLISH).setModified(false);
    }
    
    String id = super.apply();
    
    return id;
  }

  @Override
  public String save(boolean validateRequired)
  {
    boolean isAppliedToDB = this.isAppliedToDB();
    
    boolean first = this.isNew() && !isAppliedToDB && !this.isImport();

    String id = super.save(validateRequired);

    // Add columns to the local struct classes
    if (first)
    {
      // Add dimensions to all attributes
      QueryFactory qf = new QueryFactory();
      BusinessDAOQuery q = qf.businessDAOQuery(MdDimensionInfo.CLASS);

      OIterator<BusinessDAOIF> i = q.getIterator();

      // Add attribute dimensions for
      try
      {
        for (BusinessDAOIF businessDAOIF : i)
        {
          MdDimensionDAOIF mdDimensionDAOIF = (MdDimensionDAOIF) businessDAOIF;

          MdClassDimensionDAO mdClassDimensionDAO = MdClassDimensionDAO.newInstance();
          mdClassDimensionDAO.setDefiningMdDimension(mdDimensionDAOIF);
          mdClassDimensionDAO.setDefiningMdClass(this);
          mdClassDimensionDAO.apply();
        }
      }
      finally
      {
        i.close();
      }
    }
    
    if (!this.isNew() || isAppliedToDB)
    {
      Attribute keyAttribute = this.getAttribute(MdClassInfo.KEY);
       
      if (keyAttribute.isModified())
      {
        List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

        for (MdClassDimensionDAOIF mdClassDimensionDAOIF : mdClassDimensions)
        {
          // The apply method will update the key
          (mdClassDimensionDAOIF.getBusinessDAO()).apply();
        }
      }
    }

    return id;
  }

  /**
   * Creates the relationship such that this type defines the given attribute.
   * 
   * @param mdAttributeConcreteIF
   *          the attribute to add to this type.
   */
  protected void addAttributeConcrete(MdAttributeConcreteDAOIF mdAttributeConcreteIF)
  {
    if (this.definesType().equals(ElementInfo.CLASS) || this.definesType().equals(RelationshipTypes.METADATA_RELATIONSHIP.getType()) || this.definesType().equals(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType()))
    {
      String error = "Attributes cannot be added to class [" + definesType() + "] at runtime.";
      throw new CannotAddAttriubteToClassException(error, mdAttributeConcreteIF, this);
    }
    RelationshipDAO newChildRelDAO = this.addChild(mdAttributeConcreteIF, RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
    newChildRelDAO.setKey(mdAttributeConcreteIF.getKey());   
    newChildRelDAO.apply();
  }

  /**
   * Returns true if an inheritance relationship can be created for this class,
   * false otherwise.
   * 
   * @return true if an inheritance relationship can be created for this class,
   *         false otherwise.
   */
  protected boolean createInheritanceRelationship()
  {
    boolean createInheritanceRelationship = false;

    MdClassDAOIF superMdClassIF = this.getSuperClass();
    // Create an inheritance relationship, if one is specified.
    if (superMdClassIF != null)
    {
      if (!superMdClassIF.isExtendable())
      {
        String error = "Super class [" + superMdClassIF.definesType() + "] cannot be extended.";
        throw new InheritanceException(error);
      }

      createInheritanceRelationship = true;
    }

    return createInheritanceRelationship;
  }

  /**
   * Structs only get the ID attribute copied from the Entity class.
   */
  protected void copyDefaultAttributes()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ComponentInfo.CLASS);

    List<? extends MdAttributeDAOIF> mdAttributeIFList = mdBusinessIF.definesAttributes();

    for (MdAttributeDAOIF mdAttributeIF : mdAttributeIFList)
    {
      this.copyAttribute(mdAttributeIF);
    }
  }

  /**
   * Copies the given attribute so that it is also defined by this entity.
   * 
   * @param mdAttributeIFOriginal
   */
  public void copyAttribute(MdAttributeDAOIF mdAttributeIFOriginal)
  {
    MdAttributeDAO newMdAttribute = (MdAttributeDAO) mdAttributeIFOriginal.copy();

    if (mdAttributeIFOriginal instanceof MdAttributeConcreteDAOIF)
    {
      // The copied attribute is now defined by this entity.
      newMdAttribute.getAttribute(MdAttributeConcreteInfo.DEFINING_MD_CLASS).setValue(this.getId());
      // Make sure that the unique database constraint for the key attribute is
      // enabled.
      if (newMdAttribute.definesAttribute().equals(ComponentInfo.KEY))
      {
        newMdAttribute.getAttribute(MdAttributeConcreteInfo.INDEX_TYPE).setModified(true);
      }
    }
    else if (mdAttributeIFOriginal instanceof MdAttributeVirtualDAOIF)
    {
      // The copied attribute is now defined by this entity.
      newMdAttribute.getAttribute(MdAttributeVirtualInfo.DEFINING_MD_VIEW).setValue(this.getId());
    }

    newMdAttribute.apply();
  }

  /**
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public void delete(boolean businessContext)
  {
    // Delete all of the MdAttributeDimension definitions
    List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      mdClassDimension.getBusinessDAO().delete(businessContext);
    }

    super.delete(businessContext);
  }

  /**
   * Returns all attribute dimensions for this attribute.
   * 
   * @return all attribute dimensions for this attribute.
   */
  public List<MdClassDimensionDAOIF> getMdClassDimensions()
  {
    List<MdClassDimensionDAOIF> list = new ArrayList<MdClassDimensionDAOIF>();
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.CLASS_HAS_DIMENSION.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      list.add((MdClassDimensionDAOIF) relationship.getChild());
    }

    return list;
  }

  /**
   * Returns the class dimension for the given dimension.
   * 
   * @param mdDimension
   *          dimension
   * 
   * @return attribute dimension for the given dimension.
   */
  public MdClassDimensionDAOIF getMdClassDimension(MdDimensionDAOIF mdDimension)
  {
    String id = mdDimension.getId();
    List<MdClassDimensionDAOIF> mdClassDimensions = this.getMdClassDimensions();

    for (MdClassDimensionDAOIF mdClassDimension : mdClassDimensions)
    {
      String _id = mdClassDimension.definingMdDimension().getId();

      if (_id.equals(id))
      {
        return mdClassDimension;
      }
    }

    return null;
  }

  /**
   * Deletes all child classes.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   */
  protected void deleteAllChildClasses(boolean businessContext)
  {
    // delete all childclasses
    for (MdClassDAOIF subMdClassIF : getSubClasses())
    {
      if (!subMdClassIF.definesType().equals(this.definesType()))
      {
        MdClassDAO subMdClass = subMdClassIF.getBusinessDAO();
        subMdClass.delete(businessContext);
      }
    }
  }

  /**
   * Drops all attributes defined by this class.
   */
  protected void dropAllAttributes(boolean businessContext)
  {
    List<? extends MdAttributeDAOIF> mdAttributeList = this.definesAttributes();

    for (MdAttributeDAOIF mdAttributeIF : mdAttributeList)
    {
      MdAttributeDAO mdAttribute = (MdAttributeDAO) mdAttributeIF.getBusinessDAO();
      mdAttribute.getAttribute(MetadataInfo.REMOVE).setValue(MdAttributeBooleanInfo.TRUE);
      mdAttribute.delete(businessContext);
    }
  }

  /**
   * Deletes any TypeTuples in which this Class is part of.
   */
  protected void dropTuples()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(TypeTupleDAOIF.CLASS);
    query.WHERE(query.aReference(TypeTupleDAOIF.METADATA).EQ(this.getId()));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      BusinessDAOIF businessDAOIF = iterator.next();
      businessDAOIF.getBusinessDAO().delete(false);
    }
  }

  /**
   * Returns a MdClassIF instance that defines the class of the given type.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * (mdClass.getType().equals(type)
   * 
   * @param type
   *          Name of the class.
   * @return MdClassIF instance that defines the class of the given type.
   */
  public static MdClassDAOIF getMdClassDAO(String classType)
  {
    return ObjectCache.getMdClassDAO(classType);
  }

  /**
   * Returns a MdClassIF instance with a root id that matches the given value.
   * 
   * <br/>
   * <b>Precondition:</b> rootId != null <br/>
   * <b>Precondition:</b> !rootId.trim().equals("") <br/>
   * <b>Precondition:</b> rootId is the root of an id that is a valid class
   * defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * IdParser.parseRootFromId(mdClass.getId()).equals(rootId)
   * 
   * @param rootId
   *          of the MdClass.
   * @return MdClassIF instance with a root id that matches the given value.
   */
  public static MdClassDAOIF getMdClassByRootId(String rootId)
  {
    return ObjectCache.getMdClassDAOByRootId(rootId);
  }

  /**
   * Returns true if the class is published, false otherwise. A published class
   * has a representation in the DTO layer.
   * 
   * @return true if the class is published, false otherwise.
   */
  public boolean isPublished()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdClassInfo.PUBLISH) ).isTrue();
  }

  /**
   * Returns true if the type is abstract, false otherwise.
   * 
   * @return true if the type is abstract, false otherwise.
   */
  public abstract boolean isAbstract();

  /**
   * Returns true if the type can be extended, false otherwise.
   * 
   * @return true if the type can be extended, false otherwise.
   */
  public abstract boolean isExtendable();

  /**
   * If true, then this class is not published beyond the server.
   * 
   * @param isPublished
   * @return
   */
  public void setIsPublished(boolean isPublished)
  {
    ( (AttributeBoolean) this.getAttribute(MdClassInfo.PUBLISH) ).setValue(isPublished);
  }

  /**
   * Returns an array of MdClassIF that defines immediate subclasses of this
   * class.
   * 
   * @return an array of MdClassIF that defines immediate subclasses of this
   *         class.
   */
  public abstract List<? extends MdClassDAOIF> getSubClasses();

  /**
   * Returns a list of MdClassIF objects that represent classes that are
   * subclasses of the given class, including this class, including all
   * recursive entities.
   * 
   * @return list of MdClassIF objects that represent classes that are
   *         subclasses of the given class, including this class, including all
   *         recursive entities.
   */
  public List<? extends MdClassDAOIF> getAllSubClasses()
  {
    List<MdClassDAOIF> subClassList = new LinkedList<MdClassDAOIF>();
    subClassList.add(this);

    // Get inheritance relationship
    List<? extends MdClassDAOIF> childSubClassList = this.getSubClasses();
    for (MdClassDAOIF childMdElementIF : childSubClassList)
    {
      subClassList.addAll(childMdElementIF.getAllSubClasses());
    }

    return subClassList;
  }

  /**
   * Returns a list of MdClassIF objects that are subclasses of the given class,
   * including all recursive entities. Only non abstract class are returned
   * (i.e. class that can be instantiated)
   * 
   * @return list of MdClassIF objects that are subclasses of the given class.
   *         Only non abstract entities are returned (i.e. classes that can be
   *         instantiated)
   */
  public List<? extends MdClassDAOIF> getAllConcreteSubClasses()
  {
    List<MdClassDAOIF> concreteSubClassList = new LinkedList<MdClassDAOIF>();

    // Do not add the class to the list if it is abstract
    if (!this.isAbstract())
    {
      concreteSubClassList.add(this);
    }

    // Get inheritance relationship
    List<? extends MdClassDAOIF> childMdList = this.getSubClasses();
    for (MdClassDAOIF childMdClassIF : childMdList)
    {
      concreteSubClassList.addAll(childMdClassIF.getAllConcreteSubClasses());
    }

    return concreteSubClassList;
  }

  /**
   * Returns an MdClassIF representing the super class of this class, or null if
   * it does not have one.
   * 
   * @return an MdClassIF representing the super class of this class, or null if
   *         it does not have one.
   */
  public abstract MdClassDAOIF getSuperClass();

  /**
   * Returns a list of MdClassIF instances representing every parent of this
   * MdClassIF partaking in an inheritance relationship.
   * 
   * @return a list of MdClassIF instances that are parents of this class.
   */
  public List<? extends MdClassDAOIF> getSuperClasses()
  {
    List<MdClassDAOIF> superMdClassIFList = new LinkedList<MdClassDAOIF>();

    List<String> superTypeNameList = this.getSuperTypes();

    for (String superType : superTypeNameList)
    {
      superMdClassIFList.add(MdClassDAO.getMdClassDAO(superType));
    }

    return superMdClassIFList;
  }

  /**
   * Returns the names of all supertypes that this type inherits from, including
   * this type.
   * 
   * @return names of all supertypes that this type inherits from, including
   *         this type.
   */
  public synchronized List<String> getSuperTypes()
  {
    if (this.superTypeNames == null)
    {
      this.superTypeNames = this.getSuperTypes2();
    }
    return this.superTypeNames;
  }

  private List<String> getSuperTypes2()
  {
    List<String> superTypeNameList = new LinkedList<String>();

    superTypeNameList.add(this.definesType());

    // Get super class
    MdClassDAOIF parentMdClass = this.getSuperClass();

    if (parentMdClass != null)
    {
      // This cast is OK, as it hides the method and the object is not being
      // updated.
      superTypeNameList.addAll( ( (MdClassDAO) parentMdClass ).getSuperTypes2());
    }

    return superTypeNameList;
  }

  /**
   * Returns the MdClassIF that is the root of the hierarchy that this type
   * belongs to. returns a reference to itself if it is the root.
   * 
   * @return MdClassIF that is the root of the hierarchy that this type belongs
   *         to. returns a reference to itself if it is the root.
   */
  public MdClassDAOIF getRootMdClassDAO()
  {
    if (this.getSuperClass() == null)
    {
      return this;
    }
    else
    {
      MdClassDAOIF rootMdClassIF = null;
      if (this.rootType == null)
      {
        List<? extends MdClassDAOIF> superEntityList = this.getSuperClasses();
        rootMdClassIF = superEntityList.get(superEntityList.size() - 1);
        this.rootType = rootMdClassIF.definesType();
      }
      else
      {
        rootMdClassIF = MdClassDAO.getMdClassDAO(this.rootType);
      }
      return rootMdClassIF;
    }
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false
   * otherwise.
   * 
   * @return true if this class is the root class of a hierarchy, false
   *         otherwise.
   */
  public abstract boolean isRootOfHierarchy();

  /**
   * Returns a list of <code>MdAttributeDAOIF</code> objects that this
   * <code>MdClassDAOIF</code> defines.
   * 
   * @return an List of <code>MdAttributeDAOIF</code> objects that this
   *         <code>MdClassDAOIF</code> defines.
   */
  public List<? extends MdAttributeDAOIF> definesAttributes()
  {
    List<MdAttributeDAOIF> list = new LinkedList<MdAttributeDAOIF>();

    List<RelationshipDAOIF> relationshipArray = this.getChildren(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());

    for (int i = 0; i < relationshipArray.size(); i++)
    {
      RelationshipDAOIF relationship = relationshipArray.get(i);
      list.add((MdAttributeDAOIF) relationship.getChild());
    }

    relationshipArray = this.getChildren(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType());

    for (int i = 0; i < relationshipArray.size(); i++)
    {
      RelationshipDAOIF relationship = relationshipArray.get(i);
      list.add((MdAttributeDAOIF) relationship.getChild());
    }

    return new LinkedList<MdAttributeDAOIF>(list);
  }

  /**
   * Returns a sorted list of <code>MdAttributeDAOIF</code> objects that this
   * <code>MdClassDAOIF</code> defines. The list is sorted by the alphabetical
   * order of the attribute names
   * 
   * @return an List of <code>MdAttributeDAOIF</code> objects that this
   *         <code>MdClassDAOIF</code> defines.
   */
  public List<? extends MdAttributeDAOIF> definesAttributesOrdered()
  {
    List<? extends MdAttributeDAOIF> list = this.definesAttributes();

    Collections.sort(list, MdAttributeDAO.alphabetical);

    return list;
  }

  /**
   * Returns the MdAttribute that defines the given attribute for the this
   * entity. This method only works if the attribute is explicitly defined by
   * the this. In other words, it will return null if the attribute exits for
   * the given entity, but is inherited from a super entity.
   * 
   * <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("")
   * 
   * @return MdAttribute that defines the given attribute for the this entity.
   */
  public MdAttributeDAOIF definesAttribute(String attributeName)
  {
    try
    {
      return MdAttributeDAO.getByKey(this.getKey() + "." + attributeName);
    }
    catch (DataNotFoundException e)
    {
      return null;
    }
  }

  /**
   * Returns true if this entity defines the given attribute, false otherwise.
   * 
   * @return true if this entity defines the given attribute, false otherwise.
   */
  public boolean definesAttribute(MdAttributeDAOIF mdAttributeIF)
  {
    if (this.getAllDefinedMdAttributeIDMap().get(mdAttributeIF.getId()) == null)
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  /**
   * Returns a complete list of MdAttributeIF objects defined for this instance
   * of Entity. This list includes attributes inherited from supertypes.
   * 
   * @return a list of MdAttributeIF objects
   */
  public List<? extends MdAttributeDAOIF> getAllDefinedMdAttributes()
  {
    List<MdAttributeDAOIF> mdAttributeList = new LinkedList<MdAttributeDAOIF>();

    List<? extends MdClassDAOIF> superMdClassIFList = this.getSuperClasses();

    // get the attribute meta data for each class
    for (MdClassDAOIF mdClassIF : superMdClassIFList)
    {
      // get the all attribute meta data for this class
      mdAttributeList.addAll(mdClassIF.definesAttributes());
    }

    return mdAttributeList;
  }

  /**
   * Returns a map of MdAttributeIF objects defined by this entity. Key:
   * attribute name in lower case Value: MdAttributeIF
   * 
   * @return map of MdAttributeIF objects defined by this entity.
   */
  public Map<String, ? extends MdAttributeDAOIF> getDefinedMdAttributeMap()
  {
    Map<String, MdAttributeDAOIF> mdAttributeMap = new HashMap<String, MdAttributeDAOIF>();

    // get the all attribute meta data for this class
    List<? extends MdAttributeDAOIF> mdAttributeList = this.definesAttributes();
    for (MdAttributeDAOIF mdAttribute : mdAttributeList)
    {
      mdAttributeMap.put(mdAttribute.definesAttribute().toLowerCase(), mdAttribute);
    }

    return mdAttributeMap;
  }

  /**
   * Returns a map of MdAttributeIF objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>
   * Map Key: attribute name in lower case <br/>
   * Map Value: MdAttributeIF
   * <p/>
   * 
   * @return map of MdAttributeIF objects defined by this entity type plus all
   *         attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeDAOIF> getAllDefinedMdAttributeMap()
  {
    Map<String, MdAttributeDAOIF> mdAttributeMap = new HashMap<String, MdAttributeDAOIF>();

    mdAttributeMap.putAll(this.getDefinedMdAttributeMap());

    List<? extends MdClassDAOIF> superMdClassIFList = this.getSuperClasses();

    // get the attribute meta data for each class
    for (MdClassDAOIF mdClassIF : superMdClassIFList)
    {
      // get the all attribute meta data for this class
      List<? extends MdAttributeDAOIF> mdAttributeList = mdClassIF.definesAttributes();
      for (MdAttributeDAOIF mdAttribute : mdAttributeList)
      {
        mdAttributeMap.put(mdAttribute.definesAttribute().toLowerCase(), mdAttribute);
      }
    }

    return mdAttributeMap;
  }

  /**
   * Returns a map of MdAttributeIF objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>
   * Map Key: mdAttributeID <br/>
   * Map Value: MdAttributeIF
   * <p/>
   * 
   * @return map of MdAttributeIF objects defined by this entity type plus all
   *         attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeDAOIF> getAllDefinedMdAttributeIDMap()
  {
    Map<String, MdAttributeDAOIF> mdAttributeMap = new HashMap<String, MdAttributeDAOIF>();

    List<? extends MdClassDAOIF> superMdClassIFList = this.getSuperClasses();

    // get the attribute meta data for each class
    for (MdClassDAOIF mdClassIF : superMdClassIFList)
    {
      // get the all attribute meta data for this class
      List<? extends MdAttributeDAOIF> mdAttributeList = mdClassIF.definesAttributes();
      for (MdAttributeDAOIF mdAttribute : mdAttributeList)
      {
        mdAttributeMap.put(mdAttribute.getId(), mdAttribute);
      }
    }

    return mdAttributeMap;
  }

  /**
   * Copies all Java source and class files from this object into files on the
   * file system.
   */
  public void writeJavaToFile()
  {
    // Write the stub and base .class files to the filesystem
    byte[] stubclass = this.getBlob(MdClassInfo.STUB_CLASS);
    byte[] baseclass = this.getBlob(MdTypeInfo.BASE_CLASS);
    byte[] dtoBaseClass = this.getBlob(MdElementInfo.DTO_BASE_CLASS);
    byte[] dtoStubClass = this.getBlob(MdClassInfo.DTO_STUB_CLASS);

    String stubsource = this.getAttribute(MdClassInfo.STUB_SOURCE).getValue();
    String basesource = this.getAttribute(MdElementInfo.BASE_SOURCE).getValue();
    String dtoBaseSource = this.getAttribute(MdElementInfo.DTO_BASE_SOURCE).getValue();
    String dtoStubSource = this.getAttribute(MdClassInfo.DTO_STUB_SOURCE).getValue();

    try
    {
      ClassManager.writeClasses(TypeGenerator.getStubClassDirectory(this), stubclass);
      ClassManager.writeClasses(TypeGenerator.getBaseClassDirectory(this), baseclass);
      ClassManager.writeClasses(TypeGenerator.getDTObaseClassDirectory(this), dtoBaseClass);
      ClassManager.writeClasses(TypeGenerator.getDTOstubClassDirectory(this), dtoStubClass);

      FileIO.write(TypeGenerator.getJavaSrcFilePath(this), stubsource);
      FileIO.write(TypeGenerator.getBaseSrcFilePath(this), basesource);
      FileIO.write(TypeGenerator.getDTObaseSrcFilePath(this), dtoBaseSource);
      FileIO.write(TypeGenerator.getDTOstubSrcFilePath(this), dtoStubSource);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
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
  @Override
  public void writeFileArtifactsToDatabaseAndObjects(Connection conn)
  {
    if (!this.isSystemPackage())
    {
      String baseSource = null;

      File baseSourceFile = new File(TypeGenerator.getBaseSrcFilePath(this));

      try
      {
        baseSource = FileIO.readString(baseSourceFile);
      }
      catch (IOException e)
      {
        if (!LocalProperties.isDevelopEnvironment())
        {
          throw new SystemException(e);
        }
      }

      // Update the business and dto base class and source
      byte[] baseClassBytes = GenerationFacade.getBaseClass(this);

      byte[] dtoBaseClass = new byte[0];
      String dtoBaseSource = "";
      String dtoStubSource = "";
      byte[] dtoStubClass = new byte[0];

      // Only update DTO Java artifacts for classes that are published.
      if (this.isPublished())
      {
        dtoBaseClass = GenerationFacade.getDTObaseClass(this);
        dtoBaseSource = GenerationFacade.getDTObaseSource(this);
        dtoStubSource = GenerationFacade.getDTOstubSource(this);
        dtoStubClass = GenerationFacade.getDTOstubClass(this);
      }

      this.updateBaseClassAndSource(conn, baseSource, baseClassBytes, dtoBaseClass, dtoBaseSource);

      if (baseSource != null && baseClassBytes != null && dtoBaseClass != null && dtoBaseSource != null)
      {
        // Only update the source. The blob attributes just point to the
        // database anyway.
        this.getAttribute(MdClassInfo.BASE_SOURCE).setValue(baseSource);
        this.getAttribute(MdClassInfo.DTO_BASE_SOURCE).setValue(dtoBaseSource);
        this.getAttribute(MdClassInfo.BASE_CLASS).setModified(true);
        this.getAttribute(MdClassInfo.DTO_BASE_CLASS).setModified(true);
      }

      String stubSource = GenerationFacade.getStubSource(this);
      byte[] stubClassBytes = GenerationFacade.getStubClass(this);
      String dtoStubClassColumnName = MdClassDAOIF.DTO_STUB_CLASS_COLUMN;
      String dtoStubSourceColumnName = MdClassDAOIF.DTO_STUB_SOURCE_COLUMN;

      String stubClassColumnName = MdClassDAOIF.STUB_CLASS_COLUMN;
      String stubSourceColumnName = MdClassDAOIF.STUB_SOURCE_COLUMN;

      if (stubSource != null && stubClassBytes != null && dtoStubClass != null && dtoStubSource != null)
      {
        Database.updateClassAndSource(this.getId(), MdClassDAOIF.TABLE, stubClassColumnName, stubClassBytes, stubSourceColumnName, stubSource, conn);
        Database.updateClassAndSource(this.getId(), MdClassDAOIF.TABLE, dtoStubClassColumnName, dtoStubClass, dtoStubSourceColumnName, dtoStubSource, conn);

        this.getAttribute(MdClassInfo.STUB_SOURCE).setValue(stubSource);
        this.getAttribute(MdClassInfo.DTO_STUB_SOURCE).setValue(dtoStubSource);
        this.getAttribute(MdClassInfo.STUB_CLASS).setModified(true);
        this.getAttribute(MdClassInfo.DTO_STUB_CLASS).setModified(true);
      }
    }
  }

  /**
   * Returns true if an attribute that stores source or class has been modified.
   * 
   * @return true if an attribute that stores source or class has been modified.
   */
  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    if (!this.isSystemPackage())
    {
      if (this.getAttribute(MdClassInfo.STUB_SOURCE).isModified() || this.getAttribute(MdClassInfo.DTO_STUB_SOURCE).isModified() || this.getAttribute(MdClassInfo.STUB_CLASS).isModified() || this.getAttribute(MdClassInfo.DTO_STUB_CLASS).isModified())
      {
        return true;
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdClassDAO getBusinessDAO()
  {
    return (MdClassDAO) super.getBusinessDAO();
  }

  public MdMethodDAOIF getMdMethod(String methodName)
  {
    List<MdMethodDAOIF> methodList = this.getMdMethods();
    for (MdMethodDAOIF mdMethod : methodList)
    {
      if (mdMethod.getName().equals(methodName))
      {
        return mdMethod;
      }
    }

    MdClassDAOIF superClass = this.getSuperClass();
    if (superClass != null)
    {
      return superClass.getMdMethod(methodName);
    }
    else
    {
      String error = "A MdMethod of the method name [" + methodName + "] does not exist on the MdEntity [" + this.definesType() + "]";
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdMethodInfo.CLASS));
    }
  }

  public List<MdMethodDAOIF> getAllMdMethods()
  {
    List<MdMethodDAOIF> mdMethods = new LinkedList<MdMethodDAOIF>();
    List<? extends MdClassDAOIF> superClasses = this.getSuperClasses();
    for (MdClassDAOIF superEntity : superClasses)
    {
      mdMethods.addAll(superEntity.getMdMethods());
    }
    return mdMethods;
  }
}
