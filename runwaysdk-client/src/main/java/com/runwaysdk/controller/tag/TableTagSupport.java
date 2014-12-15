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
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.controller.ServletDispatcher;
import com.runwaysdk.controller.table.ColumnableIF;
import com.runwaysdk.controller.table.Table;
import com.runwaysdk.controller.table.TableVisitor;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;
import com.runwaysdk.controller.tag.develop.TagAnnotation;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.transport.attributes.AttributeDTO;

@TagAnnotation(name = "table", bodyContent = "scriptless", description = "Runway table")
public class TableTagSupport extends ClassTagSupport implements ColumnableTagIF
{
  /**
   * Query object used to generate the Table
   */
  private ClassQueryDTO query;

  /**
   * Table data-structure build by this tag
   */
  private Table         table;

  /**
   * Name of the scoped attribute for the current MutableDTO
   */
  private String        var;

  /**
   * Index of the current item in the collection
   */
  private int           index;

  private String odd;

  private String even;


  public TableTagSupport()
  {
    table = new Table();
    odd = null;
    even = null;
  }

  @AttributeAnnotation(description = "Query used to generate the table", required = true, rtexprvalue = true)
  public ClassQueryDTO getQuery()
  {
    return query;
  }

  public void setQuery(ClassQueryDTO query)
  {
    this.query = query;
  }

  @AttributeAnnotation(description = "The name of the scoped variable to which each entry is assigned.")
  public String getVar()
  {
    return var;
  }

  public void setVar(String var)
  {
    this.var = var;
  }

  @AttributeAnnotation(description = "The class of odd rows")
  public String getOdd()
  {
    return odd;
  }

  public void setOdd(String odd)
  {
    this.odd = odd;
  }

  @AttributeAnnotation(description = "The class of even rows")
  public String getEven()
  {
    return even;
  }

  public void setEven(String even)
  {
    this.even = even;
  }

  public boolean isAsynchronous()
  {
    Object asynchronous = this.getJspContext().findAttribute(ServletDispatcher.IS_ASYNCHRONOUS);

    if (asynchronous != null)
    {
      return (Boolean) asynchronous;
    }

    return false;
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#getColumnar()
   */
  public ColumnableIF getColumnar()
  {
    return table;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    int count = query.getResultSet().size();

    table.setRowCount(count);
    table.setClasses(this.getClasses());
    table.setEven(this.getEven());
    table.setOdd(this.getOdd());

    if(this.getId() != null)
    {
      table.setId(this.getId());
    }

    // Parse the body of the table and generate a table mapping
    if(this.getJspBody() != null)
    {
      this.getJspBody().invoke(table.getWriter());
    }

    // Convert the table mapping to HTML
    JspWriter out = this.getJspContext().getOut();

    table.getContext().setQuery(query);
    table.accept(new TableVisitor(out, table.getId(), isAsynchronous()));
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#next(java.lang.String)
   */
  public void next(String attributeName)
  {
    // Increment the index
    index++;

    if (this.hasNext())
    {
      setScopedAttributes(attributeName);
    }
  }

  /**
   * Sets the scoped attributes.  The scoped attribute 'value' is set
   * to the value of the given attribute on the current MutableDTO.
   * If the given attribute name is null then the scoped attribute 'value'
   * is not set. The scoped attribute var is set to the current MutableDTO
   *
   * @param attributeName
   */
  private void setScopedAttributes(String attributeName)
  {
    // Set the value of the context
    if (this.hasNext())
    {
      if (attributeName != null)
      {
        Object value = this.getValue(attributeName);
        this.getJspContext().setAttribute("value", value);
      }

      // Set the var index
      if (var != null)
      {
        List<? extends ComponentDTOIF> items = query.getResultSet();
        this.getJspContext().setAttribute(var, items.get(index));
      }
    }
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#getValue(java.lang.String)
   */
  public Object getValue(String attributeName)
  {
    try
    {
      MutableDTO item = this.getCurrent();
      Class<?> clazz = item.getClass();
      String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);

      return clazz.getMethod(methodName).invoke(item);
    }
    catch (Exception e)
    {
      // FIXME Fix excpetion type
      MutableDTO item = this.getCurrent();
      Class<?> clazz = item.getClass();

      String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);
      String msg = "The method [" + methodName + "] does not exist on the type [" + clazz.getName()
          + "]";

      throw new RuntimeException(msg);
    }
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#hasNext()
   */
  public boolean hasNext()
  {
    return ( ( query != null ) && ( index < query.getResultSet().size() ) );
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#reset(java.lang.String)
   */
  public void reset(String attributeName)
  {
    index = 0;

    this.setScopedAttributes(attributeName);
  }

  /**
   * @see com.runwaysdk.controller.tag.ColumnableTagIF#getCurrent()
   */
  public MutableDTO getCurrent()
  {
    return (MutableDTO) query.getResultSet().get(index);
  }

  public AttributeDTO getAttributeDTO(String name)
  {
    return query.getAttributeDTO(name);
  }
}
