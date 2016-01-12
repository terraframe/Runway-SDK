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

import java.util.Map;

import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityGenerator;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeStructMdSession;

public class MdAttributeStructDAO extends MdAttributeConcreteDAO implements MdAttributeStructDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -2107754731598644344L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeStructDAO()
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
    return super.getSignature()+" StructType:"+this.getMdStructDAOIF().definesType();
  }

  /**
   * Constructs a MdAttributeStruct from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null <br/><b>Precondition:</b>
   * classType != null
   *
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeStructDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdAttributeStructDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeStructDAO(attributeMap, classType);
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeStruct_E(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_S(this));
    }
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.MdAttributeIF#javaType()
   */
  public String javaType(boolean isDTO)
  {
    String structId = this.getAttributeIF(MdAttributeStructInfo.MD_STRUCT).getValue();

    MdStructDAOIF mdStructIF = MdStructDAO.get(structId);

    if (isDTO)
      return mdStructIF.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    return mdStructIF.definesType();
  }

  @Override
  protected String generatedServerGetter(String attributeName)
  {
    return '(' + this.javaType(false) + ") " + attributeName;
  }

  /**
   * Typically called for java class generation, but Enum atributes require
   * special logic, which is contained in the generator. Included only to
   * satisfy the interface, this method should never be called, and will throw
   * an exception if it is.
   *
   * @return nothing
   * @throws ForbiddenMethodException if called
   */
  @Override
  public String generatedServerSetter()
  {
    throw new ForbiddenMethodException(MdAttributeStructDAO.class.getName()+"MdAttributeStruct.generatedSetter() should never be called.");
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    throw new ForbiddenMethodException(MdAttributeStructDAO.class.getName()+"MdAttributeStruct.generatedSetter() should never be called.");
  }

  /**
   * Returns a string representing the query attribute class for attributes of this type.
   *
   * @return string representing the query attribute class for attributes of this type.
   */
  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableStruct.class.getName();
  }

  public void setRandomValue(EntityDAO object)
  {
    BusinessDAO standalone = EntityGenerator.generateInstance(this.getMdStructDAOIF().definesType());
    String name = this.definesAttribute();
    for (Attribute attribute : standalone.getAttributeArray())
    {
      object.setStructValue(name, attribute.getName(), attribute.getValue());
    }
  }

  /**
   * Returns the type of AttributeMdDTO this MdAttributeStruct requires at the DTO Layer.
   *
   * @return class name of the AttributeMdDTO to represent this MdAttributeStruct
   */
  @Override
  public String attributeMdDTOType()
  {
    return AttributeStructMdDTO.class.getName();
  }

  /**
   * Returns the <code>MdStructDAOIF</code> that defines the class used to store the values of
   * the struct attribute.
   *
   * @return the <code>MdStructDAOIF</code> that defines the class used to store the values of
   *         the struct attribute.
   */
  public MdStructDAOIF getMdStructDAOIF()
  {
    if (this.getAttributeIF(MdAttributeStructInfo.MD_STRUCT).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this
          .getAttributeIF(MdAttributeStructInfo.MD_STRUCT);

      return (MdStructDAOIF) attributeReference.dereference();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeStructDAO getBusinessDAO()
  {
    return (MdAttributeStructDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new MdAttributeStruct. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   *
   * @return MdAttributeStruct.
   */
  public static MdAttributeStructDAO newInstance()
  {
    return (MdAttributeStructDAO) BusinessDAO.newInstance(MdAttributeStructInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String,
   *      java.lang.String)
   */
  public static MdAttributeStructDAOIF get(String id)
  {
    return (MdAttributeStructDAOIF) BusinessDAO.get(id);
  }

  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
    visitor.visitStruct(this);
  }
  
  /**
   * Used for client-side metadata caching.
   */
  @Override
  public AttributeMdSession getAttributeMdSession() 
  {
    AttributeStructMdSession attrSes = new AttributeStructMdSession(this.getMdStructDAOIF().definesType());
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeStructDAOIF.class.getName();
  }
}
