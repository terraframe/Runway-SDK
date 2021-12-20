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
package com.runwaysdk.transport.metadata;


public class RelationshipTypeMd extends TypeMd
{
  /**
   * 
   */
  private static final long serialVersionUID = -135749498631904618L;

  /**
   * The parentMdBusiness defined by the MdRelationship.
   */
  private String parentMdBusiness;
  
  /**
   * The childMdBusiness defined by the MdRelationship.
   */
  private String childMdBusiness;
  
  /**
   * Default constructor.
   */
  public RelationshipTypeMd()
  {
    super();
    this.parentMdBusiness = "";
    this.childMdBusiness = "";
  }

  /**
   * Constructor to set the basic TypeMd information, along with MdRelationship specific
   * information such as the types defined by the parent and child MdBusinesses.
   * 
   * @param displayLabel
   * @param description
   * @param oid
   * @param parentMdBusiness
   * @param childMdBusiness
   * @param generateSource TODO
   */
  public RelationshipTypeMd(String displayLabel, String description, String oid, String parentMdBusiness, String childMdBusiness, Boolean generateSource)
  {
    super(displayLabel, description, oid, generateSource);
    
    this.parentMdBusiness = parentMdBusiness;
    this.childMdBusiness = childMdBusiness;
  }
  
  /**
   * Returns the qualified parent MdBusiness defined by the MdRelationship.
   * 
   * @return
   */
  public String getParentMdBusiness()
  {
    return parentMdBusiness;
  }
  
  /**
   * Sets the parent MdBusiness
   * 
   * @param parentMdBusiness
   */
  protected void setParentMdBusiness(String parentMdBusiness)
  {
    this.parentMdBusiness = parentMdBusiness;
  }
  
  /**
   * Returns the qualified child MdBusiness defined by the MdRelationship.
   * 
   * @return
   */
  public String getChildMdBusiness()
  {
    return childMdBusiness;
  }
  
  /**
   * Sets the child MdBusiness
   */
  protected void setChildMdBusiness(String childMdBusiness)
  {
    this.childMdBusiness = childMdBusiness;
  }
  
  /**
   * Deep clones this TypeMd
   */
  public RelationshipTypeMd clone()
  {
      RelationshipTypeMd relationshipMd = (RelationshipTypeMd) super.clone();
      relationshipMd.setParentMdBusiness(parentMdBusiness);
      relationshipMd.setChildMdBusiness(childMdBusiness);
      
      return relationshipMd;
  }

}
