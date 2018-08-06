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
package com.runwaysdk.query;

import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public abstract class GeneratedRelationshipQuery extends GeneratedEntityQuery
{
  /**
   *
   */
  protected GeneratedRelationshipQuery ()
  {
    super();
  }

  /**
   * Returns the <code>MdRelationshipDAOIF</code> of components that are queried by this object.
   * 
   * @return <code>MdRelationshipDAOIF</code> of components that are queried by this object.
   */
  public MdRelationshipDAOIF getMdClassIF()
  {
    return (MdRelationshipDAOIF)super.getMdClassIF();
  }
  
  /**
   * Returns the <code>MdRelationshipDAOIF</code> of components that are queried by this object.
   * 
   * @return <code>MdRelationshipDAOIF</code> of components that are queried by this object.
   */
  public MdRelationshipDAOIF getMdRelationshipIF()
  {
    return (MdRelationshipDAOIF)super.getMdClassIF();
  }  
  
  /**
   * Returns RelationshipQuery that all generated query methods delegate to.
   * @return RelationshipQuery that all generated query methods delegate to.
   */
  protected RelationshipQuery getRelationshipQuery()
  {
    return (RelationshipQuery)this.getComponentQuery();
  }

  /**
   * Sets the RelationshipQuery that all generated query methods delegate to.
   */
  protected void setRelationshipQuery(RelationshipQuery relationshipQuery)
  {
    this.setComponentQuery(relationshipQuery);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentOid()
  {
    return this.getRelationshipQuery().parentOid();
  }
 
  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentOid(String userDefinedAlias)
  {
    return this.getRelationshipQuery().parentOid(userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentOid(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getRelationshipQuery().parentOid(userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @return Attribute reference statement object.
   */
  public SelectableReference getParent()
  {
    return this.getParent(null, null);
  }

  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute reference statement object.
   */
  public SelectableReference getParent(String userDefinedAlias)
  {
    return this.getParent(userDefinedAlias, null);
  }
  
  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.PARENT_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute reference statement object.
   */
  public SelectableReference getParent(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdRelationshipDAOIF mdRelationshipDAOIF = this.getMdRelationshipIF();
    MdBusinessDAOIF parentMdBusinessDAOIF = mdRelationshipDAOIF.getParentMdBusiness();
    
    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  PARENT_OID has no medadata that defines it.
    // So, I just gave it the one for the OID field, and then hardcoded the name of the attribute to PARENT_OID.
    MdAttributeCharacterDAOIF mdAttributeCharacterDAOIF = (MdAttributeCharacterDAOIF)this.getComponentQuery().getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.OID);
    MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF = mdAttributeCharacterDAOIF.convertToReference(parentMdBusinessDAOIF);
    
    AttributeReference attributeReference = (AttributeReference) 
        this.getComponentQuery().internalAttributeFactory(mdAttributeCharacterDAOIF.definesAttribute(), mdAttributeReferenceDAOIF, this, userDefinedAlias, userDefinedDisplayLabel);   
    
    attributeReference.setAttributeName(RelationshipInfo.PARENT_OID);
    attributeReference.setColumnName(RelationshipDAOIF.PARENT_OID_COLUMN);
    attributeReference.recomputeColumnAlias();

    return attributeReference;
  }
  
  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter childOid()
  {
    return this.getRelationshipQuery().childOid();
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter childOid(String userDefinedAlias)
  {
    return this.getRelationshipQuery().childOid(userDefinedAlias, null);
  }


  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter childOid(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getRelationshipQuery().childOid(userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   *
   * @return Attribute reference statement object.
   */
  public SelectableReference getChild()
  {
    return this.getChild(null, null);
  }
  
  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @param userDefinedAlias
   * @return Attribute reference statement object.
   */
  public SelectableReference getChild(String userDefinedAlias)
  {
    return this.getChild(userDefinedAlias, null);
  }

  /**
   * Returns an attribute reference statement object where the name of the
   * attribute is the Relationship.CHILD_OID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute reference statement object.
   */
  public SelectableReference getChild(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    MdRelationshipDAOIF mdRelationshipDAOIF = this.getMdRelationshipIF();
    MdBusinessDAOIF parentMdBusinessDAOIF = mdRelationshipDAOIF.getParentMdBusiness();
    
    // Major Hack here.  The query API requires that all Attributes have an MdAttribute.  PARENT_OID has no metadata that defines it.
    // So, I just gave it the one for the OID field, and then hardcoded the name of the attribute to PARENT_OID.
    MdAttributeCharacterDAOIF mdAttributeCharacterDAOIF = (MdAttributeCharacterDAOIF)this.getComponentQuery().getMdEntityIF().getRootMdClassDAO().definesAttribute(EntityInfo.OID);
    MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF = mdAttributeCharacterDAOIF.convertToReference(parentMdBusinessDAOIF);
    
    AttributeReference attributeReference = (AttributeReference) 
        this.getComponentQuery().internalAttributeFactory(mdAttributeCharacterDAOIF.definesAttribute(), mdAttributeReferenceDAOIF, this, userDefinedAlias, userDefinedDisplayLabel);   
   
    attributeReference.setAttributeName(RelationshipInfo.CHILD_OID);
    attributeReference.setColumnName(RelationshipDAOIF.CHILD_OID_COLUMN);
    attributeReference.recomputeColumnAlias();

    return attributeReference;
  }
  
}
