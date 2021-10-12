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

import com.runwaysdk.constants.MdAttributeClassificationInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeGraphRef;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.database.ValidateRootCommand;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdClassificationDAO;
import com.runwaysdk.system.AbstractClassification;

public class MdAttributeClassificationDAO extends MdAttributeGraphRefDAO implements MdAttributeClassificationDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2184573204236397718L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeClassificationDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeClassificationDAO} from the given hashtable
   * of {@link Attribute}s.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   *
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeClassificationDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Creates an empty {@link MdAttributeClassificationDAO}. For subclasses
   * creates a subtype based on the classType, and fills the attributes with the
   * attribute map
   * 
   * @param attributeMap
   *          The attribute mappings of the class
   * @return The new class created
   */
  public MdAttributeClassificationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeClassificationDAO(attributeMap, classType);
  }

  /**
   * Returns a new {@link MdAttributeClassificationDAO}. Some attributes will
   * contain default values, as defined in the attribute metadata. Otherwise,
   * the attributes will be blank.
   *
   * @return {@link MdAttributeClassificationDAO}.
   */
  public static MdAttributeClassificationDAO newInstance()
  {
    return (MdAttributeClassificationDAO) BusinessDAO.newInstance(MdAttributeClassificationInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeClassificationDAOIF.class.getName();
  }

  /**
   * Returns the <code>MdClassDAOIF</code> that defines the class used to store
   * the values of the struct attribute.
   *
   * @return the <code>MdStructDAOIF</code> that defines the class used to store
   *         the values of the struct attribute.
   */
  @Override
  public MdClassificationDAOIF getMdClassificationDAOIF()
  {
    if (this.getAttributeIF(MdAttributeClassificationInfo.REFERENCE_MD_CLASSIFICATION).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeClassificationInfo.REFERENCE_MD_CLASSIFICATION);
      MdClassificationDAO mdClassification = (MdClassificationDAO) attributeReference.dereference();

      return mdClassification;
    }
  }

  /**
   * Returns the <code>MdClassDAOIF</code> that defines the class used to store
   * the values of the struct attribute.
   *
   * @return the <code>MdStructDAOIF</code> that defines the class used to store
   *         the values of the struct attribute.
   */
  @Override
  public MdVertexDAOIF getReferenceMdVertexDAOIF()
  {
    MdClassificationDAOIF mdClassification = this.getMdClassificationDAOIF();

    if (mdClassification != null)
    {
      return mdClassification.getReferenceMdVertexDAO();
    }

    return null;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeClassificationDAO getBusinessDAO()
  {
    return (MdAttributeClassificationDAO) super.getBusinessDAO();
  }

  @Override
  protected void validate()
  {
    new ValidateRootCommand(this).doIt();

    super.validate();
  }

  public VertexObjectDAOIF getRoot()
  {
    if (!this.getAttributeIF(MdAttributeClassificationInfo.ROOT).getValue().trim().equals(""))
    {
      AttributeGraphRef attributeReference = (AttributeGraphRef) this.getAttributeIF(MdAttributeClassificationInfo.ROOT);

      return attributeReference.dereference(attributeReference.getValue());
    }

    return null;
  }
}
