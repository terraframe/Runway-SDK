/**
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
 */
package com.runwaysdk.controller.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.LoopTagStatus;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.controller.tag.develop.AttributeAnnotation;

public class ListElementTagSupport extends FormElementTagSupport
{
  /**
   * The name of the scoped variable to which each entry is assigned.
   */
  protected String            var;

  /**
   * The name of the scoped variable for the status of the iteration.
   * Object exported is of type {@link LoopTagStatus}.
   */
  protected String            varStatus;

  /**
   * Index of first element to iterate over.
   */
  protected Integer           begin;

  /**
   * Index of the last element to iterate over.  If {@code end > items.size()}
   * then {@code items.size()} is used for the end element.
   */
  protected Integer           end;

  /**
   * Step size
   */
  protected Integer           step;

  /**
   * List of {@link BusinessDTO}s to iterate over.
   */
  protected List<ComponentDTO> items;
  
  /**
   * Name of the accessor from which to retrieve the value
   */
  protected String  valueAttribute;

  /**
   * Status of the iteration.
   */
  private LoopTagStatus       status;
  
  protected String param;
  
  /**
   * Current {@link BusinessDTO} being iterated over.
   */
  private ComponentDTO         current;

  /**
   * The index of the current item begin iterated over.
   */
  private int                 index;

  /**
   * Count of the current number of items which have been iterated over.
   */
  private int                 count;

  public void setParam(String param)
  {
    this.param = param;
  }

  @AttributeAnnotation(required=true, description="The name of the servlet parameter")
  public String getParam()
  {
    return param;
  }

  @AttributeAnnotation(required=true, rtexprvalue=true, description="Ordered list of com.runwaysdk.transport.BusinessDTO objects")
  public List<ComponentDTO> getItems()
  {
    return items;
  }

  public void setItems(List<ComponentDTO> items)
  {
    this.items = items;
  }
  
  @AttributeAnnotation(required=true, description="Name of the accessor of the desired value parameter")
  public String getValueAttribute()
  {
    return valueAttribute;
  }

  public void setValueAttribute(String valueAttribute)
  {
    this.valueAttribute = valueAttribute;
  }

  @AttributeAnnotation(description="The name of the scoped variable to which each entry is assigned.")
  public String getVar()
  {
    return var;
  }

  public void setVar(String var)
  {
    this.var = var;
  }

  @AttributeAnnotation(description="The name of the scoped variable for the status of the iteration. Object exported is of type javax.servlet.jsp.jstl.core.LoopTagStatus")
  public String getVarStatus()
  {
    return varStatus;
  }

  public void setVarStatus(String varStatus)
  {
    this.varStatus = varStatus;
  }

  @AttributeAnnotation(description="Index of first element to iterate over.")
  public Integer getBegin()
  {
    return begin;
  }

  public void setBegin(Integer begin)
  {
    this.begin = begin;
  }

  @AttributeAnnotation(description="Index of the last element to iterate over. The value of 'end' is limited by the size of the item list.")
  public Integer getEnd()
  {
    return end;
  }

  public void setEnd(Integer end)
  {
    this.end = end;
  }

  @AttributeAnnotation(description="Integer specifying the iteration step size.")
  public Integer getStep()
  {
    return step;
  }

  public void setStep(Integer step)
  {
    this.step = step;
  }

  protected ComponentDTO getCurrent()
  {
    return current;
  }

  @Override
  public void doTag() throws JspException, IOException
  {
    init();

    if (varStatus != null)
    {
      this.getJspContext().setAttribute(varStatus, this.getLoopStatus());
    }

    for (; index < Math.min(end, items.size()); index += step)
    {
      current = items.get(index);

      if (var != null)
      {
        this.getJspContext().setAttribute(var, current);
      }

      if(this.getJspBody() != null)
      {
        this.getJspBody().invoke(null);
      }

      count++;
    }
  }

  private void init()
  {
    index = 0;
    count = 1;

    if (begin == null)
    {
      begin = new Integer(0);
    }

    if (end == null)
    {
      end = new Integer(items.size());
    }

    if (step == null)
    {
      step = new Integer(1);
    }
  }

  public LoopTagStatus getLoopStatus()
  {
    class Status implements LoopTagStatus
    {
      public Integer getBegin()
      {
        return begin;
      }

      public int getCount()
      {
        return count;
      }

      public Object getCurrent()
      {
        return current;
      }

      public Integer getEnd()
      {
        return end;
      }

      public int getIndex()
      {
        return begin + index;
      }

      public Integer getStep()
      {
        return step;
      }

      public boolean isFirst()
      {
        return ( index == begin );
      }

      public boolean isLast()
      {
        return ( index == end - 1 );
      }
    }

    if (status == null)
    {
      status = new Status();
    }

    return status;
  }

}
