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

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.InvalidExpressionSyntaxException;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.metadata.MdAttributePrimitive;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdWebForm;
import com.runwaysdk.system.metadata.MdWebPrimitive;
import com.runwaysdk.system.metadata.MdWebPrimitiveQuery;

import ognl.ExpressionSyntaxException;
import ognl.Ognl;
import ognl.OgnlException;

public abstract class MdAttributePrimitiveDAO extends MdAttributeConcreteDAO implements MdAttributePrimitiveDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -212665433619931419L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributePrimitiveDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributePrimitiveDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   *
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return getDefaultValue(this.getAttributeIF(MdAttributePrimitiveInfo.DEFAULT_VALUE).getValue());
  }
  
  /**
   * True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   * 
   * @return True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   */
  public boolean isExpression()
  {
    return ((AttributeBooleanIF)this.getAttributeIF(MdAttributePrimitiveInfo.IS_EXPRESSION)).getBooleanValue();
  }
  
  /**
   * Returns the user defined expression string. If none is defined then an empty string is returned.
   * 
   * @return user defined expression string. If none is defined then an empty string is returned.
   */
  public String getExpression()
  {
    return this.getAttributeIF(MdAttributePrimitiveInfo.EXPRESSION).getValue();
  }
  
  /**
   * 
   */
  public String save(boolean validateRequired)
  {
    this.validateExpression();
    
    return super.save(validateRequired);
  }
  
  
  @Override
  public void delete(boolean businessContext)
  {
    
    
    super.delete(businessContext);
  }
  
  /**
   * Creates an <code>EmptyValueProblem</code> if this is set to be an expression attribute,
   * yet no expression is defined.
   * 
   */
  private void validateExpression()
  {
    if (this.isExpression())
    {
      if (( this.getExpression() == null || this.getExpression().trim().equals("") ))
      {
        AttributeIF expressionAttribute = this.getAttributeIF(MdAttributePrimitiveInfo.EXPRESSION);
        MdAttributeDAOIF mdAttributeDAOIF = expressionAttribute.getMdAttribute();
      
        String error = "Attribute [" + MdAttributePrimitiveInfo.EXPRESSION + "] on type [" + MdAttributePrimitiveInfo.CLASS + "] requires a value because [" + MdAttributePrimitiveInfo.IS_EXPRESSION + "] is set to ["+MdAttributeBooleanInfo.TRUE+"]";
        EmptyValueProblem problem = new EmptyValueProblem(this.getProblemNotificationId(), mdAttributeDAOIF.definedByClass(), mdAttributeDAOIF, error, expressionAttribute);
        problem.throwIt();
      }
      else
      {
        String expressionString = this.getExpression();
        
        try
        {
          Ognl.parseExpression(expressionString);
        }
        catch (ExpressionSyntaxException e)
        {
          String devMessage = "The attribute ["+this.definesAttribute()+"] has an invalid expression defined:\n"+e.getLocalizedMessage();
          throw new InvalidExpressionSyntaxException(devMessage, this, e);
        }
        catch (OgnlException e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }
  }
  
  @Override
  public String apply()
  {
    boolean isModified = this.getAttributeIF(MdAttributePrimitive.EXPRESSION).isModified();
    
    String newId = super.apply();
    
    if (isModified)
    {
      this.updateExistingCalculatedAttrs();
    }
    
    return newId;
  }
  
  /**
   * Queries to find all businesses that use this calculated expression and updates the values.
   */
  public void updateExistingCalculatedAttrs()
  {
    if (this.isExpression())
    {
      MdWebPrimitiveQuery webPrimQ = new MdWebPrimitiveQuery(new QueryFactory());
      webPrimQ.WHERE(webPrimQ.getDefiningMdAttribute().getOid().EQ(this.getOid()));
      OIterator<? extends MdWebPrimitive> mdWebPrimIt = webPrimQ.getIterator();
      
      while (mdWebPrimIt.hasNext())
      {
        MdWebPrimitive mdWebPrim = mdWebPrimIt.next();
        MdWebForm mdForm = mdWebPrim.getDefiningMdForm();
        MdClass mdClass = mdForm.getFormMdClass();
        
        QueryFactory fac = new QueryFactory();
        BusinessQuery bq = fac.businessQuery(mdClass.definesType());
        OIterator<Business> it = bq.getIterator();
        
        while (it.hasNext())
        {
          Business form = it.next();
          form.appLock();
          form.apply();
        }
      }
    }
  }
}
