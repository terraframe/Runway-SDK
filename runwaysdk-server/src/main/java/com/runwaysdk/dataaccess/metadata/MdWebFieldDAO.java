/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.WebFormFieldInfo;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.MdWebGroupDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;

public abstract class MdWebFieldDAO extends MdFieldDAO implements MdWebFieldDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -4713909102181764335L;

  public MdWebFieldDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public MdWebFieldDAO()
  {
    super();
  }

  @Override
  public final String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocal) this.getAttributeIF(MdWebFieldInfo.DISPLAY_LABEL) ).getValue(locale);
  }

  @Override
  public final Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocal) this.getAttributeIF(MdWebCharacterInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  @Override
  public final String getFieldName()
  {
    return this.getAttribute(MdWebFieldInfo.FIELD_NAME).getValue();
  }

  @Override
  public final String getFieldOrder()
  {
    return this.getAttribute(MdWebFieldInfo.FIELD_ORDER).getValue();
  }

  @Override
  public final MdWebFormDAOIF getMdForm()
  {
    if (this.getMdFormId() != null && this.getMdFormId().length() > 0)
    {
      return (MdWebFormDAOIF) ( (AttributeReferenceIF) this.getAttribute(MdWebFieldInfo.DEFINING_MD_FORM) ).dereference();
    }

    return null;
  }

  @Override
  public final String getMdFormId()
  {
    return this.getAttribute(MdWebFieldInfo.DEFINING_MD_FORM).getValue();
  }

  @Override
  public final String isRequired()
  {
    return this.getAttribute(MdWebFieldInfo.REQUIRED).getValue();
  }

  /**
   * Applies this MdField and creates the relationship to the MdForm if this
   * instance is new.
   * 
   */
  @Override
  public String apply()
  {
    // FIXME put into superclass with getFormFieldRelationship():Str accessor
    boolean firstApply = ( this.isNew() && !this.isAppliedToDB() && !this.isImport() );

    String oid = super.apply();

    if (firstApply)
    {
      String formId = this.getMdFormId();

      if (formId != null && formId.length() > 0)
      {
        RelationshipDAO rel = RelationshipDAO.newInstance(formId, oid, WebFormFieldInfo.CLASS);
        rel.apply();
      }
    }

    return oid;
  }

  public MdWebGroupDAOIF getContainingGroup()
  {
    QueryFactory factory = new QueryFactory();
    RelationshipDAOQuery query = factory.relationshipDAOQuery(RelationshipTypes.WEB_GROUP_FIELD.getType());

    query.WHERE(query.childOid().EQ(this.getOid()));

    OIterator<RelationshipDAOIF> it = query.getIterator();

    try
    {
      if (it.hasNext())
      {
        RelationshipDAOIF relationship = it.next();

        return (MdWebGroupDAOIF) relationship.getParent();
      }

      return null;
    }
    finally
    {
      it.close();
    }
  }

  @Override
  public List<FieldConditionDAOIF> getConditions()
  {
    List<FieldConditionDAOIF> conditions = super.getConditions();

    MdWebGroupDAOIF group = this.getContainingGroup();

    if (group != null)
    {
      conditions.addAll(group.getConditions());
    }

    return conditions;
  }
}
