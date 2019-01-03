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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.Business;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeVirtualDAO extends MdAttributeDAO implements MdAttributeVirtualDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 4827303404522702546L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeVirtualDAO()
  {
    super();
  }

  @Override
  public String getColumnName()
  {
    return this.getMdAttributeConcrete().getColumnName();
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Name:" + this.definesAttribute() + " Concrete:" + this.getMdAttributeConcrete().getSignature();
  }

  /**
   * Constructs a BusinessDAO from the given <code>Map</code> of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   * 
   * @param attributeMap
   * @param classType
   */
  public MdAttributeVirtualDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Creates an empty BusinessDAO. For subclasses creates a subtype based on the
   * classType, and fills the attributes with the attribute map
   * 
   * @param attributeMap
   *          The attribute mappings of the class
   * @return The new class created
   */
  public MdAttributeVirtualDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeVirtualDAO(attributeMap, classType);
  }

  /**
   * Creates a new instance of the <code>MdAttributeVirtual</code> class.
   * 
   * @return
   */
  public static MdAttributeVirtualDAO newInstance()
  {
    return (MdAttributeVirtualDAO) BusinessDAOFactory.newInstance(MdAttributeVirtualInfo.CLASS);
  }

  /**
   * Returns the MdViewIF that defines this MdAttribute.
   * 
   * @return the MdViewIF that defines this MdAttribute.
   */
  public MdViewDAOIF definedByClass()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeVirtualInfo.DEFINING_MD_VIEW);
    return (MdViewDAOIF) attributeReference.dereference();
  }

  /**
   * Returns the MdAttribute that this attribute references.
   * 
   * @return MdAttribute that this attribute references.
   */
  @Override
  public MdAttributeConcreteDAOIF getMdAttributeConcrete()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE);

    try
    {
      return (MdAttributeConcreteDAOIF) attributeReference.dereference();
    }
    catch (InvalidReferenceException e)
    {
      // do not dereference the reference (as you normally would), as it
      // produces an infinite loop within this method
      MdAttributeReferenceDAOIF mdAttribute = attributeReference.getMdAttribute();
      MdBusinessDAOIF refMdBusinessIF = MdBusinessDAO.get(mdAttribute.getAttributeIF(MdAttributeReferenceInfo.REF_MD_ENTITY).getValue());
      String displayLabel = ( (AttributeLocalIF) this.getAttributeIF(MdAttributeVirtualInfo.DISPLAY_LABEL) ).getValue(CommonProperties.getDefaultLocale());

      String errMsg = "Attribute [" + displayLabel + "] on type [" + this.definedByClass().definesType() + "] does not reference a valid [" + refMdBusinessIF.definesType() + "]";
      throw new InvalidReferenceException(errMsg, mdAttribute);
    }
  }

  /**
   * Returns the display label of this metadata object. If this metadata has no
   * display label, it returns the one from the
   * <code>MdAttributeConcreteIF</code>.
   * 
   * @param locale
   * 
   * @return the display label of this metadata object.
   */
  public String getDisplayLabel(Locale locale)
  {
    String thisDisplayLabel = ( (AttributeLocalIF) this.getAttributeIF(MdAttributeVirtualInfo.DISPLAY_LABEL) ).getValue(locale);

    if (thisDisplayLabel.trim().equals(""))
    {
      return this.getMdAttributeConcrete().getDisplayLabel(locale);
    }
    else
    {
      return thisDisplayLabel;
    }
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
   * Returns true if the metadata can be removed, false otherwise.
   * 
   * @return true if the metadata can be removed, false otherwise.
   */
  public boolean isRemovable()
  {
    // This was delegating, but resulted in virtual attributes (and, by
    // extenstion, their enclosing types) becoming nonremovable. Should virtual
    // attributes ever be nonremovable? Perhaps a case exists, but until we add
    // removable to virtual attributes, we need to be able to delete them.
    return true;
    // return this.getMdAttributeConcrete().isRemovable();
  }

  /**
   * Returns a description of this metadata;
   * 
   * @param locale
   * 
   * @return a description of this metadata;
   */
  public String getDescription(Locale locale)
  {
    String thisDescription = ( (AttributeLocalIF) this.getAttributeIF(MdAttributeVirtualInfo.DESCRIPTION) ).getValue(locale);

    if (thisDescription.trim().equals(""))
    {
      return this.getMdAttributeConcrete().getDescription(locale);
    }
    else
    {
      return thisDescription;
    }
  }

  /**
   * Returns a string representing the query attribute class for attributes of
   * this type.
   * 
   * @return string representing the query attribute class for attributes of
   *         this type.
   */
  public String queryAttributeClass()
  {
    return this.getMdAttributeConcrete().queryAttributeClass();
  }

  /**
   * Returns the name of the Attribute that this Attribute.ATTRIBUTE defines.
   * 
   * @return the name of the Attribute that this Attribute.ATTRIBUTE defines.
   */
  public String definesAttribute()
  {
    String thisAttributeName = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAttributeName.trim().length() == 0)
    {
      return this.getMdAttributeConcrete().definesAttribute();
    }
    else
    {
      return thisAttributeName;
    }
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   * 
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return this.getMdAttributeConcrete().getDefaultValue();
  }

  /**
   * If a default value has been defined for a dimension attached to this
   * session, then that value is returned, otherwise the default value assigned
   * to the attribute definition is returned.
   * 
   * @return default value
   */
  public String getAttributeInstanceDefaultValue()
  {
    return this.getMdAttributeConcrete().getAttributeInstanceDefaultValue();
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
    return this.getMdAttributeConcrete().isImmutable();
  }

  /**
   * Always returns false because virtual attributes that reference system
   * attributes must be able to have their values set programmatically.
   * 
   * @return false
   */
  public boolean isSystem()
  {
    // return this.getMdAttributeConcrete().isSystem();
    return false;
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
    String thisRequired = this.getAttributeIF(MdAttributeVirtualInfo.REQUIRED).getValue();

    if (thisRequired.trim().equals(""))
    {
      return this.getMdAttributeConcrete().isRequired();
    }
    else
    {
      return ( (AttributeBooleanIF) this.getAttributeIF(MdAttributeVirtualInfo.REQUIRED) ).isTrue();
    }
  }

  /**
   * Returns the visibility modifier of the getter.
   * 
   * @return the visibility modifier of the getter.
   */
  public VisibilityModifier getGetterVisibility()
  {
    return this.getMdAttributeConcrete().getGetterVisibility();
  }

  /**
   * Returns the visibility modifier of the setter.
   * 
   * @return the visibility modifier of the setter.
   */
  public VisibilityModifier getSetterVisibility()
  {
    return this.getMdAttributeConcrete().getSetterVisibility();
  }

  /**
   * Called for java class generation. Returns the java type of this attribute,
   * which is used in the generated classes for type safety.
   * 
   * @param isDTO
   *          indicates if the generation is for a DTO-layer object
   * @return The java type of this attribute
   */
  public String javaType(boolean isDTO)
  {
    return this.getMdAttributeConcrete().javaType(isDTO);
  }

  /**
   * Called for java class DTO generation. Returns the type of AttributeMd (DTO
   * layer) this MdAttribute requires to represent its metadata on a DTO.
   * 
   * @return the class name of the AttributeMd type needed.
   */
  public String attributeMdDTOType()
  {
    return this.getMdAttributeConcrete().attributeMdDTOType();
  }

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the server object that fetches the desired attribute
   * (through getValue methods inherited from {@link Business}), and converts it
   * from String into the appropriate type.
   * 
   * @return java code for typesafe getValue
   */
  public String generatedServerGetter()
  {
    String thisAttributeName = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAttributeName.trim().equals(""))
    {
      return this.getMdAttributeConcrete().generatedServerGetter();
    }
    else
    {
      // This cast is OK, as we are not modifying the object.
      return ( (MdAttributeConcreteDAO) this.getMdAttributeConcrete() ).generatedServerGetter(thisAttributeName);
    }
  }

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the client object that fetches the desired attribute
   * (through getValue methods inherited from {@link Business}), and converts it
   * from String into the appropriate type.
   * 
   * @return java code for typesafe getValue
   */
  public String generatedClientGetter()
  {
    String thisAttributeName = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAttributeName.trim().equals(""))
    {
      return this.getMdAttributeConcrete().generatedClientGetter();
    }
    else
    {
      // This cast is OK, as we are not modifying the object.
      return ( (MdAttributeConcreteDAO) this.getMdAttributeConcrete() ).generatedClientGetter(thisAttributeName);
    }
  }

  /**
   * Called for java class generation, this method returns a String repesenting
   * the java code for the object on the server that sets the desired attribute
   * (through setValue methods inherited from {@link Business}) by converting a
   * typsafe parameter into a String.
   * 
   * @return java code for typesafe setValue
   */
  public String generatedServerSetter()
  {
    String thisAttributeName = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAttributeName.trim().equals(""))
    {
      return this.getMdAttributeConcrete().generatedServerSetter();
    }
    else
    {
      // This cast is OK, as we are not modifying the object.
      MdAttributeConcreteDAO concrete = (MdAttributeConcreteDAO) this.getMdAttributeConcrete();
      return concrete.generatedServerSetter(thisAttributeName);
    }
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
    String thisAttributeName = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAttributeName.trim().equals(""))
    {
      return this.getMdAttributeConcrete().generatedClientSetter();
    }
    else
    {
      // This cast is OK, as we are not modifying the object.
      MdAttributeConcreteDAO concrete = (MdAttributeConcreteDAO) this.getMdAttributeConcrete();
      return concrete.generatedClientSetter(thisAttributeName);
    }
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
    String thisAccessor = this.getAttributeIF(MdAttributeVirtualInfo.NAME).getValue();

    if (thisAccessor.trim().equals(""))
    {
      return this.getMdAttributeConcrete().setterWrapper(value);
    }
    else
    {
      // This cast is OK, as we are not modifying the object.
      return ( (MdAttributeConcreteDAO) this.getMdAttributeConcrete() ).setterWrapper(thisAccessor, value);
    }
  }

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    boolean isApplied = isAppliedToDB();

    String oid = super.save(validateRequired);

    if (this.isNew() && !isApplied)
    {
      if (!this.isImport())
      {
        // This cast is OK, because we are not modifying the MdView
        MdViewDAO mdView = (MdViewDAO) this.definedByClass();
        mdView.addAttributeVirtual(this);

        // This cast is OK, as we are not modifying the object, but just adding a
        // relationship
        ( (MdAttributeConcreteDAO) this.getMdAttributeConcrete() ).addAttributeVirtual(this);
      }
    }
    else
    {
      Attribute keyAttribute = this.getAttribute(MdAttributeVirtualInfo.KEY);
      if (keyAttribute.isModified())
      {
        List<RelationshipDAOIF> classAttrVirtRels = this.getParents(RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType());
        for (RelationshipDAOIF classAttrVirtRelDAOIF : classAttrVirtRels)
        {
          RelationshipDAO classAttrVirtRelDAO = classAttrVirtRelDAOIF.getRelationshipDAO();
          classAttrVirtRelDAO.setKey(buildClassAttrVirtualKey(this));
          classAttrVirtRelDAO.save(true);
        }
          
        List<RelationshipDAOIF> virtualAttrRels = this.getParents(RelationshipTypes.VIRTUALIZE_ATTRIBUTE.getType());
        for (RelationshipDAOIF virtualAttrRelDAOIF : virtualAttrRels)
        {
          RelationshipDAO virtualAttrRelDAO = virtualAttrRelDAOIF.getRelationshipDAO();
          virtualAttrRelDAO.setKey(buildVirtualizeAttrKey(this));
          virtualAttrRelDAO.save(true);
        }
      }
    }

    return oid;
  }

  /**
   * 
   * @param mdAttributeVirtualDAO
   * @return
   */
  protected static String buildClassAttrVirtualKey(MdAttributeVirtualDAOIF mdAttributeVirtualDAOIF)
  {    
    return mdAttributeVirtualDAOIF.getKey()+"."+RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getTableName();
  }
  
  /**
   * 
   * @param mdAttributeVirtualDAO
   * @return
   */
  protected static String buildVirtualizeAttrKey(MdAttributeVirtualDAOIF mdAttributeVirtualDAOIF)
  {    
    return mdAttributeVirtualDAOIF.getKey()+"."+RelationshipTypes.VIRTUALIZE_ATTRIBUTE.getTableName();
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
    if (attributeName.getValue().trim().length() != 0 && attributeName.isModified())
    {
      this.validateAttributeName(attributeName.getValue());
    }

    if (this.getAttributeIF(MdAttributeConcreteInfo.NAME).isModified())
    {
      MdViewDAOIF definingEntity = this.definedByClass();
      List<? extends MdAttributeDAOIF> attributeList = null;
      List<? extends MdViewDAOIF> parentsList = definingEntity.getSuperClasses();

      // loop through parents and check for an attribute of the same name
      for (MdViewDAOIF parent : parentsList)
      {
        attributeList = parent.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being
          // compared with itself
          if (this.definesAttribute().equals(attribute.definesAttribute()) && !parent.definesType().equals(definingEntity.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.definesAttribute() + "] to class [" + definingEntity.definesType() + "] because its parent class [" + parent.definesType() + "] already defines an attribute with that name.";
            throw new DuplicateAttributeInInheritedHierarchyException(msg, this, definingEntity, parent);
          }
        }
      }

      List<? extends MdViewDAOIF> childrenList = definingEntity.getAllSubClasses();
      // loop through children and check for an attribute of the same name
      for (MdViewDAOIF child : childrenList)
      {
        attributeList = child.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being
          // compared with itself
          if (this.definesAttribute().equals(attribute.definesAttribute()) && !child.definesType().equals(definingEntity.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.definesAttribute() + "] to class [" + definingEntity.definesType() + "] because a child class [" + child.definesType() + "] already defines an attribute with that name.";
            throw new DuplicateAttributeDefinedInSubclassException(msg, this, definingEntity, child);
          }
        }
      }
    }

    super.validate();
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitVirtual(this);
  }

  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeVirtualDAOIF.class.getName();
  }
}
