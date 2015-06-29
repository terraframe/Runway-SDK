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
 * 
 */
package com.runwaysdk.query;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AttributeMultiTerm extends AttributeMultiReference implements SelectableMultiReference, HasAttributeFactory
{
  private static Map<String, AttributeMultiTerm.PluginIF> pluginMap = new ConcurrentHashMap<String, AttributeMultiTerm.PluginIF>();

  public static void registerPlugin(AttributeMultiTerm.PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  protected AttributeMultiTerm(MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNameSpace, String definingTableName, String definingTableAlias, String mdMultiTermTableName, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    this(mdAttributeIF, attributeNameSpace, definingTableName, definingTableAlias, mdMultiTermTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, null);
  }

  protected AttributeMultiTerm(MdAttributeMultiReferenceDAOIF mdAttributeIF, String attributeNameSpace, String definingTableName, String definingTableAlias, String mdMultiTermTableName, MdBusinessDAOIF referenceMdBusinessIF, String referenceTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNameSpace, definingTableName, definingTableAlias, mdMultiTermTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }
}