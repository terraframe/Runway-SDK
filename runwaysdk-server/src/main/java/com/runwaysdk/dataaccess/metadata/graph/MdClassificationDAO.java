/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.metadata.graph;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeGraphRef;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.system.AbstractClassification;
import com.runwaysdk.system.metadata.MdAttributeBoolean;

public class MdClassificationDAO extends MetadataDAO implements MdClassificationDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5015373068741693970L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdClassificationDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdClassificationDAO} from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdClassificationDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdClassificationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdClassificationDAO(attributeMap, MdClassificationInfo.CLASS);
  }

  /**
   * Returns a new {@link MdClassificationDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link MdClassificationDAO}.
   */
  public static MdClassificationDAO newInstance()
  {
    return (MdClassificationDAO) BusinessDAO.newInstance(MdClassificationInfo.CLASS);
  }

  /**
   * Returns the name of the package of the type that this object defines.
   * 
   * @return name of the package of the type that this object defines.
   */
  public String getPackage()
  {
    return this.getAttributeIF(MdClassificationInfo.PACKAGE).getValue();
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

  public MdVertexDAOIF getReferenceMdVertexDAO()
  {
    if (this.getAttributeIF(MdClassificationInfo.MD_VERTEX).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdClassificationInfo.MD_VERTEX);

      return (MdVertexDAOIF) attributeReference.dereference();
    }
  }

  public MdEdgeDAOIF getReferenceMdEdgeDAO()
  {
    if (this.getAttributeIF(MdClassificationInfo.MD_EDGE).getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdClassificationInfo.MD_EDGE);

      return (MdEdgeDAOIF) attributeReference.dereference();
    }
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Classification:" + this.definesType();
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

  public String getGenerateSource()
  {
    String generateSource = this.getAttribute(MdClassificationInfo.GENERATE_SOURCE).getValue();

    if (generateSource != null)
    {
      return generateSource;
    }

    return MdAttributeBooleanInfo.FALSE;
  }

  /*
   * @see com.runwaysdk.dataaccess.metadata.MdBusinessDAO#save(boolean)
   */
  @Override
  public String save(boolean flag)
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB() && !this.isImport();

    this.setKey(this.definesType());

    if (firstApply && this.getReferenceMdVertexDAO() == null)
    {
      String classificationLabel = this.getStructValue(MdClassificationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);

      String vertexName = this.getAttribute(MdClassificationInfo.TYPE_NAME).getValue() + "Vertex";
      String edgeName = this.getAttribute(MdClassificationInfo.TYPE_NAME).getValue() + "Edge";
      String generateSource = this.getGenerateSource();

      MdVertexDAO mdVertex = MdVertexDAO.newInstance();
      mdVertex.setValue(MdVertexInfo.NAME, vertexName);
      mdVertex.setValue(MdVertexInfo.PACKAGE, this.getPackage());
      mdVertex.setStructValue(MdVertexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, classificationLabel);
      mdVertex.setValue(MdVertexInfo.SUPER_MD_VERTEX, MdVertexDAO.getMdVertexDAO(AbstractClassification.CLASS).getOid());
      mdVertex.setValue(MdVertexInfo.GENERATE_SOURCE, generateSource);
      mdVertex.apply();

      // Create the code attribute for the vertex object
      MdAttributeCharacterDAO code = MdAttributeCharacterDAO.newInstance();
      code.setValue(MdAttributeCharacterInfo.NAME, "code");
      code.setValue(MdAttributeCharacterInfo.SIZE, "255");
      code.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
      code.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Code");
      code.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdVertex.getOid());
      code.apply();

      MdEdgeDAO mdEdge = MdEdgeDAO.newInstance();
      mdEdge.setValue(MdEdgeInfo.NAME, edgeName);
      mdEdge.setValue(MdEdgeInfo.PACKAGE, this.getPackage());
      mdEdge.setStructValue(MdEdgeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, classificationLabel + " Edge");
      mdEdge.setValue(MdEdgeInfo.PARENT_MD_VERTEX, mdVertex.getOid());
      mdEdge.setValue(MdEdgeInfo.CHILD_MD_VERTEX, mdVertex.getOid());
      mdEdge.setValue(MdEdgeInfo.GENERATE_SOURCE, generateSource);
      mdEdge.apply();

      this.setValue(MdClassificationInfo.MD_VERTEX, mdVertex.getOid());
      this.setValue(MdClassificationInfo.MD_EDGE, mdEdge.getOid());
    }

    String retval = super.save(flag);

    return retval;
  }

  @Override
  public void delete()
  {
    MdEdgeDAOIF referenceMdEdgeDAO = this.getReferenceMdEdgeDAO();
    MdVertexDAOIF referenceMdVertexDAO = this.getReferenceMdVertexDAO();

    super.delete();

    if (referenceMdEdgeDAO != null)
    {
      referenceMdEdgeDAO.getBusinessDAO().delete();
    }

    if (referenceMdVertexDAO != null)
    {
      referenceMdVertexDAO.getBusinessDAO().delete();
    }
  }

  @Override
  public VertexObjectDAOIF getRoot()
  {
    if (!this.getAttributeIF(MdClassificationInfo.ROOT).getValue().trim().equals(""))
    {
      AttributeGraphRef attributeReference = (AttributeGraphRef) this.getAttributeIF(MdClassificationInfo.ROOT);

      return attributeReference.dereference(attributeReference.getValue());
    }

    return null;
  }

  @Transaction
  public static MdClassificationDAO create(String packageName, String typeName, String classificationLabel)
  {
    MdClassificationDAO mdClassification = MdClassificationDAO.newInstance();
    mdClassification.setStructValue(MdEdgeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, classificationLabel);
    mdClassification.setValue(MdClassificationInfo.TYPE_NAME, typeName);
    mdClassification.setValue(MdClassificationInfo.PACKAGE, packageName);
    mdClassification.apply();

    return mdClassification;
  }

  public static MdClassificationDAOIF getMdClassificationDAO(String type)
  {
    MdClassificationDAOIF mdType = (MdClassificationDAOIF) MdClassificationDAO.get(MdClassificationInfo.CLASS, type);

    if (mdType == null)
    {
      String error = "MdType [" + type + "] was not found.";
      throw new DataNotFoundException(error, MdTypeDAO.getMdTypeDAO(MdClassificationInfo.CLASS));
    }

    return mdType;
  }

}
