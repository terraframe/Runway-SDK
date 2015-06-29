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
import com.runwaysdk.constants.EntityDTOInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;

public abstract class EntityDTO extends MutableDTO
{

  /**
   * Auto-generated serial id.
   */
  private static final long  serialVersionUID = -7535525506459007659L;

  public final static String CLASS            = EntityDTOInfo.CLASS;

  /**
   * Flag denoting if this DTO is a non-persisting disconnected object
   */
  private boolean            disconnected     = false;

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected EntityDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected EntityDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected EntityDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected EntityDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**********************************************************************************/
  /** Generated accessor methods **/
  /**********************************************************************************/

  public static java.lang.String KEYNAME    = "keyName";

  public static java.lang.String SITEMASTER = "siteMaster";

  public String getKeyName()
  {
    return getValue(KEYNAME);
  }

  public void setKeyName(String value)
  {
    if (value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }

  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }

  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }

  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }

  public com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO("keyName").getAttributeMdDTO();
  }

  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }

  public void setSiteMaster(String value)
  {
    if (value == null)
    {
      setValue(SITEMASTER, "");
    }
    else
    {
      setValue(SITEMASTER, value);
    }
  }

  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }

  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }

  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }

  public com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO("siteMaster").getAttributeMdDTO();
  }

  /**
   * @return the disconnected
   */
  public boolean isDisconnected()
  {
    return this.disconnected;
  }

  /**
   * @param _disconnected
   *          the disconnected to set
   */
  public void setDisconnected(boolean _disconnected)
  {
    this.disconnected = _disconnected;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.MutableDTO#copyProperties(com.runwaysdk.business
   * .ComponentDTOIF)
   */
  @Override
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);

    this.disconnected = ( (EntityDTO) componentDTOIF ).isDisconnected();
  }

}
