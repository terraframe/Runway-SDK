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
package com.runwaysdk.dataaccess.metadata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class TypeTupleDAO extends MetadataDAO implements TypeTupleDAOIF
{
  /**
   * Delimeter for keys on TypeTuples
   */
  protected static final String                       DELIMETER      = "+";

  /**
   * Eclipse auto generated serial ID
   */
  private static final long serialVersionUID = -6190657962345427054L;

  public TypeTupleDAO()
  {
    super();
  }

  public TypeTupleDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String signature = "Metadata["+this.getMetaData().getSignature()+"]"+
    " StateMaster["+this.getStateMaster().getSignature()+"]";
    return signature;
  }

  /**
   * Set the value of the MetaData reference object.
   *
   * @param metadataId Id of the MetaData businessDAO.
   */
  public void setMetaData(String metadataId)
  {
    this.getAttribute(TypeTupleDAOIF.METADATA).setValue(metadataId);
  }

  /**
   * Set the value of the StateMasterIF reference object.
   *
   * @param stateId Id of the StateMaster businessDAO.
   */
  public void setStateMaster(String stateId)
  {
    this.getAttribute(TypeTupleDAOIF.STATE_MASTER).setValue(stateId);
  }

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ((AttributeLocalIF)this.getAttributeIF(TypeTupleDAOIF.DISPLAY_LABEL)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ((AttributeLocalIF)this.getAttributeIF(TypeTupleDAOIF.DISPLAY_LABEL)).getLocalValues();
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.CommonStateIF#getState()
   */
  public MetadataDAOIF getMetaData()
  {
    AttributeReference metadata = (AttributeReference) this.getAttribute(TypeTupleDAOIF.METADATA);

    if(metadata.getValue().equals(""))
    {
      return null;
    }

    return (MetadataDAOIF) metadata.dereference();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.rbac.CommonStateIF#getCommon()
   */
  public StateMasterDAOIF getStateMaster()
  {
    AttributeReference statemaster = (AttributeReference) this.getAttribute(TypeTupleDAOIF.STATE_MASTER);

    if(statemaster.getValue().equals(""))
    {
      return null;
    }

    return (StateMasterDAOIF) statemaster.dereference();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public TypeTupleDAO getBusinessDAO()
  {
    return (TypeTupleDAO) super.getBusinessDAO();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public TypeTupleDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new TypeTupleDAO(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static TypeTupleDAOIF get(String id)
  {
    EntityDAOIF entityDAO = EntityDAO.get(id);
    TypeTupleDAOIF attributeObject = (TypeTupleDAOIF) entityDAO;

    return attributeObject;
  }

  /**
   * Create a new instance of a TypeTuple
   *
   * @return A blank TypeTuple object
   */
  public static TypeTupleDAO newInstance()
  {
    return (TypeTupleDAO) BusinessDAO.newInstance(TypeTupleDAOIF.CLASS);
  }

  /**
   * Finds a TypeTuple with the given StateMasterIF-MetaData pairing.
   * If the pairing does not exist then returns a null value.
   *
   * @param metadataId The id of the MetaData on the pairing
   * @param stateMasterId The id of the StateMasterIF of the pairing
   * @return The TypeTuple which represents the pairing or a null value.
   */
  public static TypeTupleDAOIF findTuple(String metadataId, String stateMasterId)
  {
    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery typeTupleQ = qFactory.businessDAOQuery(TypeTupleDAOIF.CLASS);
    typeTupleQ.WHERE(typeTupleQ.aReference(TypeTupleDAOIF.METADATA).EQ(metadataId));
    typeTupleQ.WHERE(typeTupleQ.aReference(TypeTupleDAOIF.STATE_MASTER).EQ(stateMasterId));

    OIterator<BusinessDAOIF> typeTupleIterator = typeTupleQ.getIterator();

    if (typeTupleIterator.hasNext())
    {
      TypeTupleDAOIF returnTupeTupleIF = (TypeTupleDAOIF) typeTupleIterator.next();
      typeTupleIterator.close();
      return returnTupeTupleIF;
    }

    return null;
  }

  protected void validateRequired()
  {
    String metadataId = this.getAttribute(TypeTupleDAOIF.METADATA).getValue();
    String stateId = this.getAttribute(TypeTupleDAOIF.STATE_MASTER).getValue();
    List<ProblemIF> problems = new LinkedList<ProblemIF>();

    if(metadataId.equals(""))
    {
      String msg = "Attribute [" + TypeTupleDAOIF.METADATA + "] on type [" + this.getType() + "] requires a value";
      problems.add(new EmptyValueProblem(this.getProblemNotificationId(), this.getMdClassDAO(), this.getMdAttributeDAO(TypeTupleDAOIF.METADATA), msg, this.getAttributeIF(TypeTupleDAOIF.METADATA)));
    }

    if(stateId.equals(""))
    {
      String msg = "Attribute [" + TypeTupleDAOIF.STATE_MASTER + "] on type [" + this.getType() + "] requires a value";
      problems.add(new EmptyValueProblem(this.getProblemNotificationId(), this.getMdClassDAO(), this.getMdAttributeDAO(TypeTupleDAOIF.STATE_MASTER), msg, this.getAttributeIF(TypeTupleDAOIF.STATE_MASTER)));
    }

    if(problems.size() > 0)
    {
      ProblemException.throwProblemException(problems);
    }
  }

  /**
   * Validate that the metadata and statemaster combination is unique.
   */
  protected void validateUniqueness()
  {
    MetadataDAOIF metadata = this.getMetaData();
    StateMasterDAOIF statemaster = this.getStateMaster();

    TypeTupleDAOIF tuple = TypeTupleDAO.findTuple(metadata.getId(), statemaster.getId());

    if(tuple != null)
    {
      String msg = "A [" + this.getType() + "] already exists with the combination of [" + metadata.getId() + "] and [" + statemaster.getId() + "]";
      throw new RuntimeException(msg);
    }
  }

  protected void validateMetadata()
  {
    //Validate that the Metadata attribute is either a MdAttribute or a MdRelationship
    if (!this.getAttribute(TypeTupleDAO.METADATA).getValue().equals(""))
    {
      MetadataDAOIF metadata = this.getMetaData();

      if (! ( metadata instanceof MdRelationshipDAO || metadata instanceof MdAttributeConcreteDAOIF ))
      {
        String msg = "Invalid value [" + metadata.getId() + "].  The attribute [" + TypeTupleDAOIF.METADATA + "] must reference a ["
            + MdAttributeConcreteInfo.CLASS + "] or a [" + MdRelationshipInfo.CLASS + "]";

        throw new TupleDefinitionException(msg, this);
      }
    }
  }

  @Override
  public String apply()
  {
    validateUniqueness();

    validateMetadata();

    this.setKey(this.getPermissionKey());
    
    return super.apply();
  }

  public String getPermissionKey()
  {
    String metadataId = this.getAttribute(DomainTupleDAOIF.METADATA).getValue();
    String stateId = this.getAttribute(DomainTupleDAOIF.STATE_MASTER).getValue();

    return TypeTupleDAO.buildKey(metadataId, stateId);
  }

  public static String buildKey(String metadataId, String stateId)
  {
    return metadataId + DELIMETER + stateId;
  }
}
