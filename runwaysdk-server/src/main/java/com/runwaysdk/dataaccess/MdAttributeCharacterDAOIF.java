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
/**
 * Created on Aug 29, 2005
 *
 */
package com.runwaysdk.dataaccess;

import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;


/**
 * @author nathan
 *
 */
public interface MdAttributeCharacterDAOIF extends MdAttributeCharDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_character";
  
  /**
   *Returns the total maximum length of this character field.
   * @return total maximum length of this character field.
   */
  public String getSize();

  /**
   * Precondition: assumes this character attribute is an ID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   */
  public MdAttributeReferenceDAOIF convertToReference();
  
  /**
   * This is used by the query API to allow for parent ids and child ids of relationships to
   * be used in queries.
   * 
   * Precondition: assumes this character attribute is an ID. The collection of
   * <code>AttributeDAO</code> objects do not have their containing reference updated to
   * the returned <code>MdAttributeReferenceDAO</code> 
   * 
   * @param the code>MdBusinessDAOIF</code> of the referenced type in the relationship.
   */
  public MdAttributeReferenceDAOIF convertToReference(MdBusinessDAOIF mdReferenecedBusinessDAOIF);
  
  /**
   * 
   * @return
   */
  public MdAttributeCharacterDAO getBusinessDAO();
}
