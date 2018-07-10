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
package com.runwaysdk.dataaccess;

import java.math.BigDecimal;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ExpressionException;
import com.runwaysdk.business.InvalidExpressionSyntaxException;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.InvalidIndicatorDefinition;

import ognl.ExpressionSyntaxException;
import ognl.Ognl;
import ognl.OgnlClassResolver;
import ognl.OgnlContext;
import ognl.OgnlException;

public class IndicatorCompositeDAO extends IndicatorElementDAO implements IndicatorCompositeDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 7013814284000552548L;

  public IndicatorCompositeDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorCompositeDAO()
  {
    super();
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public IndicatorCompositeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new IndicatorCompositeDAO(attributeMap, IndicatorCompositeInfo.CLASS);
  }

  /**
   * Returns a new {@link IndicatorCompositeDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link IndicatorCompositeDAO}>.
   */
  public static IndicatorCompositeDAO newInstance()
  {
    return (IndicatorCompositeDAO) BusinessDAO.newInstance(IndicatorCompositeInfo.CLASS);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static IndicatorCompositeDAOIF get(String id)
  {
    return (IndicatorCompositeDAOIF) BusinessDAO.get(id);
  }

  /**
   * @see IndicatorElementDAOIF#evalNonAggregateValue
   */
  public Object evalNonAggregateValue(MdAttributeIndicatorDAOIF _mdAttributeIndicator, ComponentDAOIF _componentDAOIF)
  {
    IndicatorElementDAOIF leftOperand = this.getLeftOperand();
    IndicatorElementDAOIF rightOperand = this.getRightOperand();

    Object leftObjectValue = leftOperand.evalNonAggregateValue(_mdAttributeIndicator, _componentDAOIF);
    Object rightObjectValue = rightOperand.evalNonAggregateValue(_mdAttributeIndicator, _componentDAOIF);

    if (leftObjectValue != null && rightObjectValue != null)
    {

      EnumerationItemDAOIF operator = this.getOperator();

      String operatorSymbol = operator.getAttributeIF(MathOperatorInfo.OPERATOR_SYMBOL).getValue();

      String expressionString = leftObjectValue.toString() + " " + operatorSymbol + " " + rightObjectValue.toString();

      if (this.isPercentage())
      {
        expressionString = expressionString + " * 100";
      }

      Object returnVal;

      if (this.isZero(rightObjectValue))
      {
        returnVal = null;
      }
      else
      {
        try
        {

          Object expression;

          try
          {
            expression = Ognl.parseExpression(expressionString);
          }
          catch (ExpressionSyntaxException e)
          {
            String devMessage = "The attribute [" + _mdAttributeIndicator.definesAttribute() + "] has an invalid expression syntax:\n" + e.getLocalizedMessage();
            throw new InvalidExpressionSyntaxException(devMessage, _mdAttributeIndicator, e);
          }

          OgnlContext ognlContext = new OgnlContext();

          Object expressionValue;

          try
          {
            // I am offended that I even have to do this. OGNL stores reflection
            // method definitions which cause
            // problems when classes are reloaded.
            OgnlClassResolver.clearOgnlRuntimeMethodCache();
            expressionValue = Ognl.getValue(expression, ognlContext, this);

            returnVal = expressionValue;
          }
          catch (RuntimeException e)
          {
            String devMessage = "The expression on attribute [" + _mdAttributeIndicator.definesAttribute() + "] has an error:\n" + e.getLocalizedMessage();
            throw new ExpressionException(devMessage, _mdAttributeIndicator, e);
          }
        }
        catch (OgnlException e)
        {
          String devMessage = "The expression on attribute [" + _mdAttributeIndicator.definesAttribute() + "] has an error:\n" + e.getLocalizedMessage();
          throw new ExpressionException(devMessage, _mdAttributeIndicator, e);
        }
      }

      return returnVal;
    }
    
    return null;
  }

  private boolean isZero(Object rightObjectValue)
  {
    if (rightObjectValue instanceof BigDecimal)
    {
      BigDecimal bigDecimal = (BigDecimal) rightObjectValue;
      Double doubleVal = bigDecimal.doubleValue();

      if (doubleVal == 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (rightObjectValue instanceof Double)
    {
      Double doubleVal = (Double) rightObjectValue;

      if (doubleVal == 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (rightObjectValue instanceof Boolean)
    {
      Boolean doubleVal = (Boolean) rightObjectValue;
      
      if (!doubleVal)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (rightObjectValue instanceof Float)
    {
      Float floatValue = (Float) rightObjectValue;

      if (floatValue == 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (rightObjectValue instanceof Long)
    {
      Long longValue = (Long) rightObjectValue;

      if (longValue == 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    else if (rightObjectValue instanceof Integer)
    {
      Integer intValue = (Integer) rightObjectValue;

      if (intValue == 0)
      {
        return true;
      }
      else
      {
        return false;
      }
    }
    // Should never reach here
    else
    {
      return false;
    }
  }

  @Override
  public void delete(boolean businessContext)
  {
    // Delete this from the database so that the reference checks on the
    // operands do not prevent
    // them from being deleted because they reference this object.
    super.delete(businessContext);

    // Delete the operands
    this.getLeftOperand().getBusinessDAO().delete();

    this.getRightOperand().getBusinessDAO().delete();
  }

  /**
   * @see IndicatorCompositeDAOIF#getLeftOperand
   */
  public IndicatorElementDAOIF getLeftOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF) this.getAttributeIF(IndicatorCompositeInfo.LEFT_OPERAND);

    return (IndicatorElementDAOIF) attributeReferenceIF.dereference();
  }

  /**
   * @see IndicatorCompositeDAOIF#getOperator
   */
  public EnumerationItemDAOIF getOperator()
  {
    AttributeEnumerationIF attributeEnumerationIF = (AttributeEnumerationIF) this.getAttributeIF(IndicatorCompositeInfo.OPERATOR);

    EnumerationItemDAOIF[] enumerationItemArray = attributeEnumerationIF.dereference();

    return enumerationItemArray[0];
  }

  /**
   * @see IndicatorCompositeDAOIF#getRightOperand
   */
  public IndicatorElementDAOIF getRightOperand()
  {
    AttributeReferenceIF attributeReferenceIF = (AttributeReferenceIF) this.getAttributeIF(IndicatorCompositeInfo.RIGHT_OPERAND);

    return (IndicatorElementDAOIF) attributeReferenceIF.dereference();
  }

  /**
   * @see IndicatorElementDAOIF#getLocalizedLabel
   */
  public String getLocalizedLabel()
  {
    String localizedLabel = "";

    IndicatorElementDAOIF leftOperand = getLeftOperand();
    EnumerationItemDAOIF operator = this.getOperator();
    IndicatorElementDAOIF rightOperand = getRightOperand();

    localizedLabel += "(" + leftOperand.getLocalizedLabel() + ")";

    localizedLabel += " " + operator.getValue(MathOperatorInfo.OPERATOR_SYMBOL) + " ";

    localizedLabel += "(" + rightOperand.getLocalizedLabel() + ")";

    return localizedLabel;
  }

  /**
   * Validates this metadata object.
   * 
   * @throws {@link
   *           InvalidIndicatorDefinition} when this MetaData object is not
   *           valid.
   */
  protected void validate()
  {
    super.validate();

    IndicatorElementDAOIF leftOperand = this.getLeftOperand();

    IndicatorElementDAOIF rightOperand = this.getRightOperand();

    boolean isValid = false;
    // For now, we are only supporting numerical operands and not nested
    // operands, but that should change
    // in the future
    if ( ( leftOperand instanceof IndicatorPrimitiveDAOIF && ( (IndicatorPrimitiveDAOIF) leftOperand ).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF ) && ( rightOperand instanceof IndicatorPrimitiveDAOIF && ( (IndicatorPrimitiveDAOIF) rightOperand ).getMdAttributePrimitive() instanceof MdAttributeNumberDAOIF ))
    {
      isValid = true;
    }
    else if ( ( leftOperand instanceof IndicatorPrimitiveDAOIF && ( (IndicatorPrimitiveDAOIF) leftOperand ).getMdAttributePrimitive() instanceof MdAttributeBooleanDAOIF ) && ( rightOperand instanceof IndicatorPrimitiveDAOIF && ( (IndicatorPrimitiveDAOIF) rightOperand ).getMdAttributePrimitive() instanceof MdAttributeBooleanDAOIF ))
    {
      isValid = true;
    }

    if (isValid == false)
    {
      String localizedLabel = this.getLocalizedLabel();

      String devMessage = "The indicator attribute definition is invalid [" + localizedLabel + "]. " + "The left and right operands must both be either a number or a boolean.";

      throw new InvalidIndicatorDefinition(devMessage, localizedLabel);
    }
  }

  /**
   * @see IndicatorElementDAOIF#javaType
   */
  public String javaType()
  {
    String leftOperandType = this.getLeftOperand().javaType();

    String rightOperandType = this.getRightOperand().javaType();

    // if (leftOperandType.equals(Boolean.class.getName()) &&
    // rightOperandType.equals(Boolean.class.getName()))
    // {
    // return Boolean.class.getName();
    // }

    if (leftOperandType.equals(BigDecimal.class.getName()) || rightOperandType.equals(BigDecimal.class.getName()))
    {
      return BigDecimal.class.getName();
    }
    // When integers or longs are divided, the result is a double.
    else
    {
      return Double.class.getSimpleName();
    }

    // if (leftOperandType.equals(Boolean.class.getName()) &&
    // rightOperandType.equals(Boolean.class.getName()))
    // {
    // return Boolean.class.getName();
    // }
    //
    // if(leftOperandType.equals(BigDecimal.class.getName()) ||
    // rightOperandType.equals(BigDecimal.class.getName()))
    // {
    // return BigDecimal.class.getName();
    // }
    // else if(leftOperandType.equals(Double.class.getName()) ||
    // rightOperandType.equals(Double.class.getName()))
    // {
    // return Double.class.getName();
    // }
    // else if(leftOperandType.equals(Float.class.getName()) ||
    // rightOperandType.equals(Float.class.getName()))
    // {
    // return Float.class.getName();
    // }
    // else if(leftOperandType.equals(Long.class.getName()) ||
    // rightOperandType.equals(Long.class.getName()))
    // {
    // return Long.class.getName();
    // }
    // else if(leftOperandType.equals(Integer.class.getName()) ||
    // rightOperandType.equals(Integer.class.getName()))
    // {
    // return Integer.class.getName();
    // }
    // // When integers or longs are divided, the result is a double.
    // else
    // {
    // return Double.class.getName();
    // }
  }

  /**
   * @see IndicatorCompositeDAOIF#accept
   */
  public void accept(IndicatorVisitor indicatorVisitor)
  {
    indicatorVisitor.visit(this);

    IndicatorElementDAOIF leftOperand = getLeftOperand();
    leftOperand.accept(indicatorVisitor);

    IndicatorElementDAOIF rightOperand = getRightOperand();
    rightOperand.accept(indicatorVisitor);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorCompositeDAO getBusinessDAO()
  {
    return (IndicatorCompositeDAO) super.getBusinessDAO();
  }

  public String toString()
  {
    return this.getLocalizedLabel();
  }

  @Override
  public boolean isPercentage()
  {
    return Boolean.parseBoolean(this.getValue(IndicatorCompositeInfo.PERCENTAGE));
  }
}
