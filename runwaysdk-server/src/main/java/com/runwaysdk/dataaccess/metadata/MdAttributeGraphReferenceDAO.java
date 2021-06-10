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

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeGraphReferenceDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeGraphReference_G;
import com.runwaysdk.transport.metadata.caching.AttributeGraphReferenceMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public class MdAttributeGraphReferenceDAO extends MdAttributeConcreteDAO implements MdAttributeGraphReferenceDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2184573204236397718L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeGraphReferenceDAO()
  {
    super();
  }
  
  /**
   * Constructs a {@link MdAttributeGraphReferenceDAO} from the given hashtable of {@link Attribute}s.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   *
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeGraphReferenceDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /**
   * Creates an empty {@link MdAttributeGraphReferenceDAO}. For subclasses creates a subtype based on the
   * classType, and fills the attributes with the attribute map
   * 
   * @param attributeMap
   *          The attribute mappings of the class
   * @return The new class created
   */
  public MdAttributeGraphReferenceDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdAttributeGraphReferenceDAO(attributeMap, classType);
  }

  /**
   * Returns a new {@link MdAttributeGraphReferenceDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   *
   * @return {@link MdAttributeGraphReferenceDAO}.
   */
  public static MdAttributeGraphReferenceDAO newInstance()
  {
    return (MdAttributeGraphReferenceDAO) BusinessDAO.newInstance(MdAttributeGraphReferenceInfo.CLASS);
  }
  
  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return super.getSignature() + " LinkType:" + this.getReferenceMdVertexDAOIF().definesType();
  }
  
  /**
   * This method balks ... does nothing... as it is not applicable for embedded types.
   */
  @Override
  public void setRandomValue(EntityDAO object) {}
  
  /**
   * Returns the <code>MdClassDAOIF</code> that defines the class used to store
   * the values of the struct attribute.
   *
   * @return the <code>MdStructDAOIF</code> that defines the class used to store
   *         the values of the struct attribute.
   */
  @Override
  public MdClassDAOIF getReferenceMdVertexDAOIF()
  {
    if (this.getAttributeIF(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX);

      return (MdClassDAOIF) attributeReference.dereference();
    }
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeGraphReferenceDAO getBusinessDAO()
  {
    return (MdAttributeGraphReferenceDAO) super.getBusinessDAO();
  }  
  
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeGraphReferenceMdSession attrSes = new AttributeGraphReferenceMdSession(this.getReferenceMdVertexDAOIF().definesType());
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * Does nothing. TThe priority for supporting this has not been defined.
   */
  @Override
  public String queryAttributeClass()
  {
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeIF#javaType()
   */
  public String javaType(boolean isDTO)
  {
    String structId = this.getAttributeIF(MdAttributeGraphReferenceInfo.REFERENCE_MD_VERTEX).getValue();

    MdClassDAOIF mdClassIF = MdClassDAO.get(structId);
// Heads up: Smethie Fix
    if (isDTO)
    {
      if (mdClassIF.isGenerateSource())
      {
//        return mdClassIF.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
        return ComponentDTO.class.getName();
      }
      else
      {
        return ComponentDTO.class.getName();
      }
    }
    else
    {
      if (mdClassIF.isGenerateSource())
      {
//        return mdClassIF.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
        return mdClassIF.definesType();
      }
      else
      {
        return ComponentIF.class.getName();
      }
    }
  }

  /**
   * Does nothing. TThe priority for supporting this has not been defined.
   */
  @Override
  public void accept(MdAttributeDAOVisitor visitor) { }

  @Override
  public String attributeMdDTOType()
  {
 // Heads up: Smethie fix
    return null;
  }

  
  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdGraphClassDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeGraphReference_G(this));
    }
    else
    {
      throw new ProgrammingErrorException("Link attributes are currently only supported a ["+MdGraphClassInfo.CLASS+"]");
    }
  }

}
