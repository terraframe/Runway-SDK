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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeGraphRef_G;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.transport.metadata.caching.AttributeGraphRefMdSession;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public abstract class MdAttributeGraphRefDAO extends MdAttributeConcreteDAO implements MdAttributeGraphRefDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2184573204236397718L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeGraphRefDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdAttributeGraphRefDAO} from the given hashtable of
   * {@link Attribute}s.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   *
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeGraphRefDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public abstract MdVertexDAOIF getReferenceMdVertexDAOIF();

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return super.getSignature() + " GraphType:" + this.getReferenceMdVertexDAOIF().definesType();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeGraphRefDAO getBusinessDAO()
  {
    return (MdAttributeGraphRefDAO) super.getBusinessDAO();
  }

  @Override
  public void setRandomValue(EntityDAO object)
  {
    // Do nothing
  }

  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    AttributeGraphRefMdSession attrSes = new AttributeGraphRefMdSession(this.getReferenceMdVertexDAOIF().definesType());
    super.populateAttributeMdSession(attrSes);
    return attrSes;
  }

  /**
   * Does nothing. TThe priority for supporting this has not been defined.
   */
  /**
   * Returns the java class object for the attribute type.
   * 
   * @return the java class object for the attribute type.
   */
  public Class<?> javaClass()
  {
    return String.class;
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
    return com.runwaysdk.query.SelectableUUID.class.getName();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdAttributeDAO#getInterfaceClassName()
   */
  @Override
  public String getInterfaceClassName()
  {
    return MdAttributeGraphRefDAOIF.class.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdAttributeIF#javaType()
   */
  public String javaType(boolean isDTO)
  {
    if (isDTO)
    {
      return null;
    }

    MdVertexDAOIF mdClassIF = this.getReferenceMdVertexDAOIF();

    // Heads up: Smethie Fix
    if (mdClassIF.isGenerateSource())
    {
      // return mdClassIF.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
      return mdClassIF.definesType();
    }
    else
    {
      return ComponentIF.class.getName();
    }
  }

  /**
   * Does nothing. TThe priority for supporting this has not been defined.
   */
  @Override
  public void accept(MdAttributeDAOVisitor visitor)
  {
  }

  @Override
  public String attributeMdDTOType()
  {
    return null;
  }

  @Override
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeGraphRef_E(this));
    }
    else if (this.definedByClass() instanceof MdGraphClassDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeGraphRef_G(this));
    }
    else
    {
      throw new ProgrammingErrorException("Graph reference attributes are currently only supported a [" + MdGraphClassInfo.CLASS + " and " + MdEntityInfo.CLASS + "]");
    }
  }

  @Override
  protected String generatedServerSetter(String attributeName)
  {
    return this.setterWrapper(attributeName, "value.getOid()");
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.metadata.MdAttribute#generateTypesafeFormatting
   * (java.lang.String)
   */
  protected String generateTypesafeFormatting(String formatMe)
  {
    MdVertexDAOIF referenceMdVertex = this.getReferenceMdVertexDAOIF();

    StringBuilder method = new StringBuilder();

    if (referenceMdVertex.isGenerateSource())
    {
      method.append("(" + referenceMdVertex.definesType() + ")");
    }

    method.append(VertexObject.class.getName() + ".get(\"" + referenceMdVertex.definesType() + "\", " + formatMe + ")");

    return method.toString();
  }

}
