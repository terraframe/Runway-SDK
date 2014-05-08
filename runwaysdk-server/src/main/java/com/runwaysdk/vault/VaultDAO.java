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
package com.runwaysdk.vault;

import java.util.Map;

import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class VaultDAO extends BusinessDAO implements VaultDAOIF, SpecializedDAOImplementationIF
{
  /**
   * An eclipse auto-generated id
   */
  private static final long serialVersionUID = -2338064958702734232L;

  public VaultDAO()
  {
    super();
  }

  public VaultDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static VaultDAOIF get(String vaultId)
  {
    EntityDAOIF entityDAO = EntityDAO.get(vaultId);
    VaultDAOIF role = (VaultDAOIF) entityDAO;

    return role;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public VaultDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new VaultDAO(attributeMap, type);
  }

  public static VaultDAO newInstance()
  {
    return (VaultDAO) BusinessDAO.newInstance(VaultInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public VaultDAO getBusinessDAO()
  {
    return (VaultDAO) super.getBusinessDAO();
  }

  /**
   * Sets the root path of the Vault. This value is immutable, once the value is
   * set it cannot be changed.
   *
   * @param path The root path of the vault
   */
  public void setVaultPath(String path)
  {
    this.setValue(VaultInfo.VAULT_PATH, path);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.vault.VaultIF#getVaultPath()
   */
  public String getVaultPath()
  {
    return this.getValue(VaultInfo.VAULT_PATH);
  }

  @Override
  public void delete(boolean businessContext)
  {
    QueryFactory queryFactory = new QueryFactory();
    BusinessDAOQuery query = queryFactory.businessDAOQuery(VaultFileInfo.CLASS);
    query.WHERE(query.aReference(VaultFileInfo.VAULT_REF).EQ(this.getId()));
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      VaultFileDAO file = ( (VaultFileDAOIF) iterator.next() ).getBusinessDAO();
      file.deleteNoNotify(businessContext);
    }

    VaultCommand command = new VaultCommand(this.getVaultPath());
    command.doIt();

    super.delete(businessContext);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.vault.VaultIF#incrementByteCount(long)
   */
  public void incrementByteCount(long byteCount)
  {
    long existingCount = Long.parseLong(this.getValue(VaultInfo.BYTE_COUNT));
    long newCount = existingCount + byteCount;

    this.setValue(VaultInfo.BYTE_COUNT, new Long(newCount).toString());
    this.apply();
  }

  @Override
  public String apply()
  {
    if (this.isNew())
    {
      this.setValue(VaultInfo.BYTE_COUNT, "0");
    }

    this.getAttribute(VaultInfo.VAULT_PATH).setValue(this.buildKey());

    return super.apply();
  }

  protected String buildKey()
  {
    return this.getAttributeIF(VaultInfo.VAULT_PATH).getValue();
  }

}
