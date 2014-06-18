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
package com.runwaysdk.form.web;

import com.runwaysdk.form.web.condition.AndFieldCondition;
import com.runwaysdk.form.web.condition.CharacterCondition;
import com.runwaysdk.form.web.condition.DateCondition;
import com.runwaysdk.form.web.condition.DoubleCondition;
import com.runwaysdk.form.web.condition.LongCondition;
import com.runwaysdk.form.web.field.WebBoolean;
import com.runwaysdk.form.web.field.WebBreak;
import com.runwaysdk.form.web.field.WebCharacter;
import com.runwaysdk.form.web.field.WebComment;
import com.runwaysdk.form.web.field.WebDate;
import com.runwaysdk.form.web.field.WebDateTime;
import com.runwaysdk.form.web.field.WebDecimal;
import com.runwaysdk.form.web.field.WebDouble;
import com.runwaysdk.form.web.field.WebFloat;
import com.runwaysdk.form.web.field.WebGeo;
import com.runwaysdk.form.web.field.WebHeader;
import com.runwaysdk.form.web.field.WebInteger;
import com.runwaysdk.form.web.field.WebLong;
import com.runwaysdk.form.web.field.WebMultipleTerm;
import com.runwaysdk.form.web.field.WebReference;
import com.runwaysdk.form.web.field.WebSingleTerm;
import com.runwaysdk.form.web.field.WebSingleTermGrid;
import com.runwaysdk.form.web.field.WebText;
import com.runwaysdk.form.web.field.WebTime;

public interface WebFormVisitor
{
  public void visit(WebFormObject formObject); 

  public void visit(WebCharacter webCharacter); 
  
  public void visit(WebText webText); 
  
  public void visit(WebBoolean webBoolean);
  
  public void visit(WebDouble webDouble);

  public void visit(WebDecimal webDecimal);
  
  public void visit(WebFloat webFloat);
  
  public void visit(WebInteger webInteger);

  public void visit(WebLong webLong);
  
  public void visit(WebGeo webGeo);
  
  public void visit(WebDate webDate);
  
  public void visit(WebDateTime webDateTime);
  
  public void visit(WebTime webTime);
  
  public void visit(WebSingleTerm webSingleTerm);
  
  public void visit(WebMultipleTerm webMultipleGrid);
  
  public void visit(WebSingleTermGrid grid);
  
  public void visit(WebHeader header);

  public void visit(WebBreak webBreak);

  public void visit(WebComment comment);
  
  public void visit(WebReference webReference);
  
  public void visit(CharacterCondition characterCondition);
  
  public void visit(DateCondition dateCondition);
  
  public void visit(DoubleCondition doubleCondition);
  
  public void visit(LongCondition longCondition);

  public void visit(AndFieldCondition andFieldCondition);
  
  /**
   * Generic fallback for plugin fields and components.
   */
  public void visit(WebFormComponent component);
  
}
