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
package com.runwaysdk.dataaccess.attributes.entity;

import java.util.Locale;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueAboveRangeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueBelowRangeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBeNegativeProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBePositiveProblem;
import com.runwaysdk.dataaccess.attributes.AttributeValueCannotBeZeroProblem;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.Format;
import com.runwaysdk.session.Session;

public abstract class AttributeNumber extends Attribute
{
  /**
   * 
   */
  private static final long serialVersionUID = -14704799088314899L;

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.Attribute#Attribute(String,
   *      String)
   */
  protected AttributeNumber(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * @see com.runwaysdk.dataaccess.attributes.entity.Attribute#Attribute(String,
   *      String, String)
   */
  protected AttributeNumber(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }

  /**
   * Validate method common to call classes inheriting this class.
   * 
   * @param mdAttribute
   *          the defining Metadata object of the class that contains this
   *          Attribute
   * @return boolean value representing the validity of the input
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);
    
    String componentId = this.getContainingComponent().getProblemNotificationId();

    // We do not validate the range of empty string, empty string is handled by
    // the required validator
    // This is to allow null to be stored in the db to indicate an unknown value
    if (!valueToValidate.trim().equals(""))
    {
      validateRange(valueToValidate, this, mdAttribute, componentId);
    }
  }
  
  public static void validateRange(String valueToValidate, AttributeIF attributeNumber, MdAttributeDAOIF mdAttribute, String componentId)
  {
    mdAttribute = mdAttribute.getMdAttributeConcrete();
    
    MdAttributeNumberDAOIF mdAttributeNumber = (MdAttributeNumberDAOIF) mdAttribute;
    
    // IMPORTANT: Do not validate the range if the value is null because it will
    // always throw a NumberFormatException which prevents optional number
    // attributes from being able to be set to null. - Justin Smethie
    if (valueToValidate != null && !valueToValidate.equals(""))
    {
      try
      {
        Double value = Double.parseDouble(valueToValidate);
        
        Format<Double> format = AbstractFormatFactory.getFormatFactory().getFormat(Double.class);
        
        Locale locale = Session.getCurrentLocale();
        String formattedValue = format.format(value, locale);
        
        // positive test
        if (Boolean.parseBoolean(mdAttributeNumber.isPositiveRejected()) && value > 0)
        {
          String error = "[" + valueToValidate + "] is not a valid value for Attribute [" + attributeNumber.getName() + "] on type [" + attributeNumber.getDefiningClassType() + "] - the value cannot be positive.";
          AttributeValueCannotBePositiveProblem problem = new AttributeValueCannotBePositiveProblem(componentId, mdAttributeNumber.definedByClass(), mdAttribute, error, attributeNumber, formattedValue);
          problem.throwIt();
        }

        // zero test
        if (Boolean.parseBoolean(mdAttributeNumber.isZeroRejected()) && value == 0)
        {
          String error = "[" + valueToValidate + "] is not a valid value for Attribute [" + attributeNumber.getName() + "] on type [" + attributeNumber.getDefiningClassType() + "] - the value cannot be zero.";
          AttributeValueCannotBeZeroProblem problem = new AttributeValueCannotBeZeroProblem(componentId, mdAttributeNumber.definedByClass(), mdAttribute, error, attributeNumber, formattedValue);
          problem.throwIt();
        }

        // negative test
        if (Boolean.parseBoolean(mdAttributeNumber.isNegativeRejected()) && value < 0)
        {
          String error = "[" + valueToValidate + "] is not a valid value for Attribute [" + attributeNumber.getName() + "] on type [" + attributeNumber.getDefiningClassType() + "] - the value cannot be negative.";
          AttributeValueCannotBeNegativeProblem problem = new AttributeValueCannotBeNegativeProblem(componentId, mdAttributeNumber.definedByClass(), mdAttribute, error, attributeNumber, formattedValue);
          problem.throwIt();
        }

        // range test
        Double startRange = mdAttributeNumber.getStartRange();

        if (startRange != null && value < startRange)
        {
          String formattedStartRange = format.format(startRange, locale);
          String error = "[" + valueToValidate + "] is not a valid value for Attribute [" + attributeNumber.getName() + "] on type [" + attributeNumber.getDefiningClassType() + "] - the value cannot be less than [" + startRange + "].";
          AttributeValueBelowRangeProblem problem = new AttributeValueBelowRangeProblem(componentId, mdAttributeNumber.definedByClass(), mdAttribute, error, attributeNumber, formattedValue, formattedStartRange);
          problem.throwIt();
        }

        Double endRange = mdAttributeNumber.getEndRange();

        if (endRange != null && value > endRange)
        {
          String formattedEndRange = format.format(endRange, locale);
          String error = "[" + valueToValidate + "] is not a valid value for Attribute [" + attributeNumber.getName() + "] on type [" + attributeNumber.getDefiningClassType() + "] - the value cannot be greater than [" + endRange + "].";
          AttributeValueAboveRangeProblem problem = new AttributeValueAboveRangeProblem(componentId, mdAttributeNumber.definedByClass(), mdAttribute, error, attributeNumber, formattedValue, formattedEndRange);
          problem.throwIt();
        }

      }
      catch (NumberFormatException e)
      {
        // Do nothing: An exception will be thrown from the concrete sub-class validation method
      }
    }
  }
}
