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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.dto.BusinessDTOStubGenerator;
import com.runwaysdk.business.generation.dto.BusinessQueryDTOGenerator;
import com.runwaysdk.business.generation.ontology.TermBaseGenerator;
import com.runwaysdk.business.generation.ontology.TermDTOBaseGenerator;
import com.runwaysdk.business.generation.ontology.TermStubGenerator;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdTermDAO extends MdBusinessDAO implements MdTermDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 4151317755345791369L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdTermDAO()
  {
    super();
  }

  /**
   * Constructs a <code>MdTermDAO</code> from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdTermDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable, java.util.String, ComponentDTOIF, Map)
   */
  public MdTermDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdTermDAO(attributeMap, MdTermInfo.CLASS);
  }

  /**
   * Returns a new <code>MdTermDAO</code>. Some attributes will contain default values, as defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of <code>MdTermDAO</code>.
   */
  public static MdTermDAO newInstance()
  {
    return (MdTermDAO) BusinessDAO.newInstance(MdTermInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdTermDAOIF get(String id)
  {
    return (MdTermDAOIF) BusinessDAO.get(id);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTermDAO getBusinessDAO()
  {
    return (MdTermDAO) super.getBusinessDAO();
  }

  /**
   * Returns a MdTermIF instance of the metadata for the given class.
   * 
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("") <br/>
   * <b>Precondition:</b> classType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdTermIF instance of the metadata for the given class (MdTermIF().definesType().equals(classType)
   * 
   * @param classType
   *          class type
   * @return MdTermIF instance of the metadata for the given class type.
   */
  public static MdTermDAOIF getMdTermDAO(String classType)
  {
    return ObjectCache.getMdTermDAO(classType);
  }

  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Dont generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

    list.add(new TermBaseGenerator(this));
    list.add(new TermStubGenerator(this));
    list.add(new TermDTOBaseGenerator(this));
    list.add(new BusinessDTOStubGenerator(this));

    if (!GenerationUtil.isHardcodedType(this))
    {
      list.add(new BusinessQueryAPIGenerator(this));
      list.add(new BusinessQueryDTOGenerator(this));
    }

    return list;
  }

  /*
   * @see com.runwaysdk.dataaccess.metadata.MdBusinessDAO#save(boolean)
   */
  @Override
  public String save(boolean flag)
  {
    boolean firstApply = this.isNew() && !this.isAppliedToDB() && !this.isImport();

    String retval = super.save(flag);

    Attribute keyAttribute = this.getAttribute(MdTermInfo.KEY);

    if (keyAttribute.isModified())
    {
      AttributeReference stratagyRef = (AttributeReference) this.getAttribute(MdTermInfo.STRATEGY);

      // Update the key of the Strategy
      if (stratagyRef.getValue() != null && !stratagyRef.getValue().trim().equals(""))
      {
        stratagyRef.dereference().getBusinessDAO().apply();
      }
    }

    // If its a Term that extends a Term then we don't want to create all this stuff again because it will already exist on the super Term.
    if (firstApply && this.getSuperClass() == null)
    {

      // Add display label to metadata.
      MdAttributeLocalCharacterDAO displayLabel = MdAttributeLocalCharacterDAO.newInstance();
      displayLabel.setValue(MdAttributeLocalCharacterInfo.NAME, MdTermInfo.DISPLAY_LABEL);
      displayLabel.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, this.getId());
      displayLabel.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
      displayLabel.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      displayLabel.apply();

      this.createRootRelationship(this.getTermAttributeRootsRelationshipName(), MdBusinessDAO.getMdBusinessDAO(MdAttributeTermInfo.CLASS));
      this.createRootRelationship(this.getMultiTermAttributeRootsRelationshipName(), MdBusinessDAO.getMdBusinessDAO(MdAttributeMultiTermInfo.CLASS));
    }

    return retval;
  }

  protected void createRootRelationship(String typeName, MdBusinessDAOIF parent)
  {
    String mdTermLabel = this.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE);
    // Define the attribute term roots relationship
    MdRelationshipDAO attributeTermRoots = MdRelationshipDAO.newInstance();
    attributeTermRoots.setValue(MdRelationshipInfo.NAME, typeName);
    attributeTermRoots.setValue(MdRelationshipInfo.PACKAGE, this.getAttribute(MdBusinessInfo.PACKAGE).getValue());
    attributeTermRoots.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdTermLabel + " Attribute Root");
    attributeTermRoots.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    attributeTermRoots.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, this.getId());
    attributeTermRoots.setValue(MdRelationshipInfo.CHILD_METHOD, typeName + "s");
    attributeTermRoots.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdTermLabel);
    attributeTermRoots.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    attributeTermRoots.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, parent.getId());
    attributeTermRoots.setValue(MdRelationshipInfo.PARENT_METHOD, typeName + "s");
    attributeTermRoots.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdTermLabel);
    attributeTermRoots.setGenerateMdController(false);
    attributeTermRoots.apply();

    MdAttributeBooleanDAO selectable = MdAttributeBooleanDAO.newInstance();
    selectable.setValue(MdAttributeBooleanInfo.NAME, MdAttributeTermInfo.SELECTABLE);
    selectable.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, attributeTermRoots.getId());
    selectable.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Selectable");
    selectable.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Yes");
    selectable.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "No");
    selectable.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    selectable.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    selectable.apply();
  }

  /**
   * @return
   */
  private String getTermAttributeRootsRelationshipName()
  {
    return this.getAttribute(MdBusinessInfo.NAME).getValue() + "TermAttributeRoot";
  }

  @Override
  public String getTermAttributeRootsRelationshipType()
  {
    return this.getAttribute(MdBusinessInfo.PACKAGE).getValue() + "." + this.getTermAttributeRootsRelationshipName();
  }

  /**
   * @return
   */
  private String getMultiTermAttributeRootsRelationshipName()
  {
    return this.getAttribute(MdBusinessInfo.NAME).getValue() + "MultiTermAttributeRoot";
  }

  @Override
  public String getMultiTermAttributeRootsRelationshipType()
  {
    return this.getAttribute(MdBusinessInfo.PACKAGE).getValue() + "." + this.getMultiTermAttributeRootsRelationshipName();
  }

}
