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
package com.runwaysdk.business;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.BusinessQueryDTOInfo;


public class BusinessQueryDTO extends ElementQueryDTO
{
  
  public static final String CLASS = BusinessQueryDTOInfo.CLASS;
  
  /**
   * 
   */
  private static final long serialVersionUID = 7759909654819453354L;

  public class TypeInMdRelationship implements Serializable
  {
    /**
     * 
     */
    private static final long serialVersionUID = 8822239251444537767L;
    
    private String relationshipType;
    
    private TypeInMdRelationship(String type)
    {
      this.relationshipType = type;
    }
    
    public String getRelationshipType()
    {
      return relationshipType;
    }
  }
  
  /**
   * Inner class to specify MdRelationship information when this type
   * is a child.
   */
  public class TypeInMdRelationshipAsChild extends TypeInMdRelationship
  {
    /**
     * 
     */
    private static final long serialVersionUID = 2085369255826036303L;
    
    private String childDisplayLabel;
    
    private TypeInMdRelationshipAsChild(String type, String childDisplayLabel)
    {
      super(type);
      this.childDisplayLabel = childDisplayLabel;
    }
    
    public String getChildDisplayLabel()
    {
      return childDisplayLabel;
    }
  }
  
  public class TypeInMdRelationshipAsParent extends TypeInMdRelationship
  {
    /**
     * 
     */
    private static final long serialVersionUID = -8152970666416497106L;
    
    private String parentDisplayLabel;
    
    private TypeInMdRelationshipAsParent(String type, String parentDisplayLabel)
    {
      super(type);
      this.parentDisplayLabel = parentDisplayLabel;
    }
    
    public String getParentDisplayLabel()
    {
      return parentDisplayLabel;
    }
  }
  
  /**
   * A list of TypeInMdRelationshipAsChild objects that
   * describe the query type as a child in an MdRelationship.
   */
  private List<TypeInMdRelationshipAsChild> mdRelationshipsChildList;
  
  /**
   * A list of TypeInMdRelationshipAsParent objects that
   * describe the query type as a parent in an MdRelationship.
   */
  private List<TypeInMdRelationshipAsParent> mdRelationshipsParentList;
  
  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected BusinessQueryDTO(String type)
  {
    super(type);
    
    mdRelationshipsChildList = new LinkedList<TypeInMdRelationshipAsChild>();
    mdRelationshipsParentList = new LinkedList<TypeInMdRelationshipAsParent>();
  }
  
  /**
   * Copies properties from the given componentQueryDTO into this one.
   * @param componentQueryDTO
   */
  public void copyProperties(BusinessQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
    this.mdRelationshipsChildList = componentQueryDTO.mdRelationshipsChildList;
    this.mdRelationshipsParentList = componentQueryDTO.mdRelationshipsParentList;
  }
  
  /**
   * Adds this query type as a child in an MdRelationship.
   * 
   * @param relationshipType
   * @param childDisplayLabel
   */
  public void addTypeInMdRelationshipAsChild(String relationshipType, String childDisplayLabel)
  {
    mdRelationshipsChildList.add(new TypeInMdRelationshipAsChild(relationshipType, childDisplayLabel));
  }
  
  /**
   * Adds this query type as a parent in an MdRelationship.
   * 
   * @param relationshipType
   * @param parentDisplayLabel
   */
  public void addTypeInMdRelationshipAsParent(String relationshipType, String parentDisplayLabel)
  {
    mdRelationshipsParentList.add(new TypeInMdRelationshipAsParent(relationshipType, parentDisplayLabel));
  }
  
  /**
   * Returns the list of TypeInMdRelationshipAsChild objects which describe
   * all MdRelationships for which this query type is a child type.
   * 
   * @return
   */
  public List<TypeInMdRelationshipAsChild> getTypeInMdRelationshipAsChildList()
  {
    return mdRelationshipsChildList;
  }
  
 /**
  * Returns the list of TypeInMdRelationshipAsParent objects which describe
  * all MdRelationships for which this query type is a parent type.
  * 
  * @return
  */
  public List<TypeInMdRelationshipAsParent> getTypeInMdRelationshipAsParentList()
  {
    return mdRelationshipsParentList;
  }
  
  @SuppressWarnings("unchecked")
  public List<? extends BusinessDTO> getResultSet()
  {
    return (List<? extends BusinessDTO>) super.getResultSet();
  }
}
