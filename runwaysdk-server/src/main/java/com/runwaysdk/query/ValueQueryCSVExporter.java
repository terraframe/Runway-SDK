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
package com.runwaysdk.query;

import java.text.DateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.attributes.value.Attribute;
import com.runwaysdk.session.Session;

public class ValueQueryCSVExporter extends CSVExporter
{

  /**
   * The list of aliases to include in the export.
   */
  private Set<String> includeAliases;

  /**
   * <code>ValueQuery</code> to export
   */
  private ValueQuery  valueQuery;

  public ValueQueryCSVExporter(ValueQuery valueQuery, Set<String> includeAliases)
  {
    super();
    this.includeAliases = includeAliases;

    this.valueQuery = valueQuery;

    this.buildBuffer();

  }

  public ValueQueryCSVExporter(ValueQuery valueQuery, DateFormat dateFormat, DateFormat dateTimeFormat, DateFormat timeFormat, Set<String> includeAliases)
  {
    super(dateFormat, dateTimeFormat, timeFormat);
    this.includeAliases = includeAliases;

    this.valueQuery = valueQuery;

    this.buildBuffer();

  }

  /**
   * Prepares a new sheet (which represents a type) in the workbook. Fills in
   * all necessary information for the sheet.
   * 
   * @return
   */
  private final void buildBuffer()
  {
    OIterator<ValueObject> iterator = this.valueQuery.getIterator();

    StringBuffer header = new StringBuffer();
    List<Selectable> selectableList = this.valueQuery.getSelectableRefs();

    // Create the header row
    for (Selectable selectable : selectableList)
    {
      if (this.includeAliases == null || this.includeAliases.size() == 0 || this.includeAliases.contains(selectable.getUserDefinedAlias()))
      {
        MdAttributeConcreteDAOIF mdAttribute = selectable.getMdAttributeIF();
        header.append(DELIMETER + "\"" + mdAttribute.getDisplayLabel(Session.getCurrentLocale()) + "\"");
      }
    }

    this.addRow(header);

    for (ValueObject valueObject : iterator)
    {
      StringBuffer row = new StringBuffer("\n");

      Map<String, Attribute> attributeMap = valueObject.getAttributeMap();

      for (Selectable selectable : selectableList)
      {
        if (this.includeAliases == null || this.includeAliases.size() == 0 || this.includeAliases.contains(selectable.getUserDefinedAlias()))
        {

          String attributeName = selectable.getResultAttributeName();

          Attribute attribute = attributeMap.get(attributeName);

          String value = attribute.getValue();

          if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeBoolean)
          {
            com.runwaysdk.dataaccess.attributes.value.AttributeBoolean attributeBoolean = (com.runwaysdk.dataaccess.attributes.value.AttributeBoolean) attribute;

            MdAttributeBooleanDAOIF mdAttributeBooleanDAOIF = attributeBoolean.getMdAttribute();

            this.populateBooleanCell(row, value, mdAttributeBooleanDAOIF);
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeNumber)
          {
            this.populateNumberCell(row, value);
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeDate)
          {
            this.populateDateCell(row, value);
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeDateTime)
          {
            this.populateDateTimeCell(row, value);
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeTime)
          {
            this.populateTimeCell(row, value);
          }
          else if (attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeChar || attribute instanceof com.runwaysdk.dataaccess.attributes.value.AttributeReference)
          {
            this.populateCharacterCell(row, value);
          }
        }
      }

      this.addRow(row);
    }
  }

}
