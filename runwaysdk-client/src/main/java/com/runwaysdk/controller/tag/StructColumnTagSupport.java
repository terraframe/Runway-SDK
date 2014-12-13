/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.controller.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.table.Column;
import com.runwaysdk.controller.table.ColumnableIF;
import com.runwaysdk.controller.table.Entry;
import com.runwaysdk.controller.table.StructColumn;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;

@TagAnnotation(name = "structColumn", bodyContent = "scriptless", description = "Column representing a struct datatype")
public class StructColumnTagSupport extends SimpleTagSupport implements ColumnableTagIF, ColumnTagIF
{
  /**
   * Name of the struct attribte
   */
  private String       attributeName;

  /**
   * StructColumn data-structure generated when digesting this tag
   */
  private StructColumn column;

  /**
   * The name of the scoped struct variable to which each entry is assigned.
   */
  private String       var;

  public StructColumnTagSupport()
  {
    this.column = new StructColumn();
  }

  @AttributeAnnotation(required = true, description = "The attribute of the column")
  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName(String attributeName)
  {
    this.attributeName = attributeName;
  }

  @AttributeAnnotation(description = "The name of the scoped struct variable to which each entry is assigned.")
  public String getVar()
  {
    return var;
  }

  public void setVar(String var)
  {
    this.var = var;
  }

  /**
   * (non-Javadoc)
   *
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#getColumnar()
   */
  public ColumnableIF getColumnar()
  {
    return column;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    if (this.getJspBody() != null)
    {
      this.getJspBody().invoke(column.getWriter());
    }

    JspTag parent = findAncestorWithClass(this, TableTagSupport.class);

    if (parent != null)
    {
      TableTagSupport table = (TableTagSupport) parent;

      AttributeDTO attribute = table.getAttributeDTO(attributeName);
      // AttributeDTO attribute =
      // table.getQuery().getAttributeDTO(attributeName);

      // attrib will be null if this attribute is not readable
      if (attribute == null || !attribute.isReadable())
      {
        return;
      }

      // If the header has not been populated then generate a default header
      if (!column.isHeaderPopulated())
      {
        generateHeader(attribute);
      }

      column.setAttributeName(attributeName);
      table.getColumnar().addColumn(column);
    }
  }

  /**
   * Generate a header
   */
  private void generateHeader(AttributeDTO attribute)
  {
    String label = attributeName;

    if (attribute != null)
    {
      label = attribute.getAttributeMdDTO().getDisplayLabel();
    }

    Entry header = column.getHeader();
    header.getWriter().write(label);
  }

  /**
   * (non-Javadoc)
   *
   * @see com.runwaysdk.controller.tag.ColumnTagIF#getColumn()
   */
  public Column getColumn()
  {
    return column;
  }

  /**
   * (non-Javadoc)
   *
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#hasNext()
   */
  public boolean hasNext()
  {
    JspTag table = findAncestorWithClass(this, TableTagSupport.class);

    if (table != null)
    {
      TableTagSupport tableTagSupport = ( (TableTagSupport) table );

      return tableTagSupport.hasNext();
    }

    return false;
  }

  public ClassQueryDTO getQuery()
  {
    JspTag table = findAncestorWithClass(this, TableTagSupport.class);

    if (table != null)
    {
      return ( (TableTagSupport) table ).getQuery();
    }

    return null;
  }

  /**
   * (non-Javadoc)
   *
   * @see
   * com.runwaysdk.controller.tag.ColumnableTagIF#next(java.lang.String)
   */
  public void next(String attributeName)
  {
    JspTag table = findAncestorWithClass(this, TableTagSupport.class);

    if (table != null)
    {
      TableTagSupport tableTagSupport = ( (TableTagSupport) table );
      tableTagSupport.next(null);

      // Set current value
      // TODO use type safe getter
      if (this.hasNext())
      {
        setScopedAttributes(attributeName);
      }
    }
  }

  private void setScopedAttributes(String attributeName)
  {
    if (this.hasNext())
    {
      MutableDTO struct = this.getCurrent();

      if (attributeName != null)
      {
        Object object = this.getValue(struct, attributeName);
        this.getJspContext().setAttribute("value", object);
      }

      if (var != null)
      {
        this.getJspContext().setAttribute(var, struct);
      }
    }
  }

  /**
   * (non-Javadoc)
   *
   * @see
   * com.runwaysdk.controller.tag.ColumnableTagIF#reset(java.lang.String)
   */
  public void reset(String attributeName)
  {
    JspTag table = findAncestorWithClass(this, TableTagSupport.class);

    if (table != null)
    {
      // Reset the collection defined in the parent table
      TableTagSupport tableTagSupport = ( (TableTagSupport) table );
      tableTagSupport.reset(null);

      this.setScopedAttributes(attributeName);
    }

  }

  /**
   * @param item
   *          MutableDTO item to get the value from
   * @param attributeName
   *          Name of the attribute
   *
   * @return The value of a given attribute on the given MutableDTO
   */
  private Object getValue(MutableDTO item, String attributeName)
  {
    try
    {
      Class<?> clazz = item.getClass();
      String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);

      return clazz.getMethod(methodName).invoke(item);
    }
    catch (Exception e)
    {
      // FIXME Fix exception type
      String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);
      String msg = "The method [" + methodName + "] does not exist on type [" + item.getClass().getName() + "]";

      throw new RuntimeException(msg);
    }
  }

  /**
   * (non-Javadoc)
   *
   * @see
   * com.runwaysdk.controller.tag.ColumnableTagIF#getValue(java.lang.String
   * )
   */
  public Object getValue(String attributeName)
  {
    MutableDTO struct = this.getCurrent();
    return this.getValue(struct, attributeName);
  }

  /**
   * (non-Javadoc)
   *
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#getCurrent()
   */
  public MutableDTO getCurrent()
  {
    // Get the value of the current MutableDTO in the table
    JspTag table = findAncestorWithClass(this, TableTagSupport.class);
    TableTagSupport tableTagSupport = ( (TableTagSupport) table );

    // Return the attribute StructDTO of the current MutableDTO
    return (MutableDTO) this.getValue(tableTagSupport.getCurrent(), attributeName);
  }

  public AttributeDTO getAttributeDTO(String name)
  {
    JspTag ancestor = findAncestorWithClass(this, TableTagSupport.class);
    TableTagSupport table = ( (TableTagSupport) ancestor );

    if (table != null)
    {
      AttributeStructDTO structDTO = (AttributeStructDTO) table.getAttributeDTO(attributeName);

      if (structDTO != null)
      {
        return structDTO.getAttributeDTO(name);
      }
    }

    return null;
  }
}
