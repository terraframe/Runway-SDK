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

import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;

public abstract class ElementDTO extends EntityDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -2115078028942453244L;

  public final static String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".ElementDTO";

  /**
   * Flag denoting if this entity is locked.
   */
  private boolean                   lockedByCurrentUser = false;

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ElementDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ElementDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ElementDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected ElementDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**
   * Returns the value of a struct attribute.
   *
   * @param structAttributeName
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public String getStructValue(String structAttributeName, String attributeName)
  {
    if (attributeMap.containsKey(structAttributeName))
    {
      AttributeDTO attribute = attributeMap.get(structAttributeName);

      this.checkAttributeReadPermission(attribute);

      if (attribute instanceof AttributeStructDTO)
      {
        AttributeStructDTO attributeStruct = (AttributeStructDTO)attribute;

        return attributeStruct.getValue(attributeName);
      }
      else
      {
        return ComponentDTOIF.EMPTY_VALUE;
      }
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE;
    }
  }

  /**
   * Sets the value of a struct attribute.
   *
   * @param structAttributeName
   * @param attributeName
   */
  public void setStructValue(String structAttributeName, String attributeName, String value)
  {
    if (attributeMap.containsKey(structAttributeName))
    {
      AttributeDTO attribute = this.getAttributeDTO(structAttributeName);

      if (attribute instanceof AttributeStructDTO)
      {
        AttributeStructDTO attributeStruct = (AttributeStructDTO)attribute;
        attributeStruct.setValue(attributeName, value);
        this.setModified(true);
      }
    }
  }

  /**
   * Checks if this DTO is locked by the current user.
   *
   * @return true if the DTO is locked by the user, false otherwise.
   */
  public boolean isLockedByCurrentUser()
  {
    return lockedByCurrentUser;
  }


  /**
   * Sets the flat denoting if this DTO is locked by the current user.
   *
   * @param locked
   */
  public void setLockedByCurrentUser(boolean locked)
  {
    this.lockedByCurrentUser = locked;
  }

  /**
   * Copies over properties from the given componentDTO.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);

    if (componentDTOIF instanceof ElementDTO)
    {
      ElementDTO elementDTO = (ElementDTO)componentDTOIF;
      this.setLockedByCurrentUser(elementDTO.isLockedByCurrentUser());
    }
  }

  /**********************************************************************************/
  /**                      Generated accessor methods                              **/
  /**********************************************************************************/
  public static java.lang.String TYPE = "type";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
  }

  public void setCreatedBy(com.runwaysdk.system.SingleActorDTO value)
  {
    if(value == null)
    {
      setValue(CREATEDBY, "");      
    }
    else
    {
      setValue(CREATEDBY, value.getId());
    }
  }

  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }

  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }

  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }

  public com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO("createdBy").getAttributeMdDTO();
  }

  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }

  public void setCreateDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(CREATEDATE, "");
    }
    else
    {
      setValue(CREATEDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }

  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }

  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }

  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }

  public com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO("createDate").getAttributeMdDTO();
  }

  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
  }

  public void setLastUpdatedBy(com.runwaysdk.system.SingleActorDTO value)
  {
    setValue(LASTUPDATEDBY, value.getId());
  }

  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }

  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }

  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }

  public com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO("lastUpdatedBy").getAttributeMdDTO();
  }

  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }

  public void setLastUpdateDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(LASTUPDATEDATE, "");
    }
    else
    {
      setValue(LASTUPDATEDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }

  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }

  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }

  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }

  public com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO("lastUpdateDate").getAttributeMdDTO();
  }

  public com.runwaysdk.system.UsersDTO getLockedBy()
  {
    return com.runwaysdk.system.UsersDTO.get(getRequest(), getValue(LOCKEDBY));
  }

  public void setLockedBy(com.runwaysdk.system.UsersDTO value)
  {
    if(value == null)
    {
      setValue(LOCKEDBY, "");
    }
    else
    {
      setValue(LOCKEDBY, value.getId());
    }
  }

  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }

  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }

  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }

  public com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO("lockedBy").getAttributeMdDTO();
  }

  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }

  public void setSeq(Long value)
  {
    if(value == null)
    {
      setValue(SEQ, "");
    }
    else
    {
      setValue(SEQ, java.lang.Long.toString(value));
    }
  }

  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }

  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }

  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }

  public com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO("seq").getAttributeMdDTO();
  }

  public com.runwaysdk.system.ActorDTO getOwner()
  {
    return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
  }

  public void setOwner(com.runwaysdk.system.ActorDTO value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getId());
    }    
  }

  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }

  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }

  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }

  public com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO("owner").getAttributeMdDTO();
  }

  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
  }

  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getId());
    }
  }

  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }

  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }

  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }

  public com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO("entityDomain").getAttributeMdDTO();
  }
}
