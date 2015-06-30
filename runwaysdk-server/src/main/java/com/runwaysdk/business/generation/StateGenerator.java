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
package com.runwaysdk.business.generation;

import java.util.Map;
import java.util.TreeMap;

import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.generation.CommonGenerationUtil;


public class StateGenerator extends Java5EnumGenerator implements ServerMarker
{
  public static final String ENTRY_ENUM = Constants.SYSTEM_PACKAGE + ".AllEntryEnumeration";

  public static void main(String args[])
  {
    generateStates(MdStateMachineDAO.get("7fbz0pfccjp5ym2os3c2xzza5nkfhlz7"), false);
  }

  @Override
  protected MdStateMachineDAOIF getMdTypeDAOIF()
  {
    return (MdStateMachineDAOIF)super.getMdTypeDAOIF();
  }

  public StateGenerator(MdStateMachineDAOIF machine)
  {
    super(machine, machine.getTypeName());
  }

  protected static void generateStates(MdStateMachineDAOIF mdStateMachine, boolean regenerateStubSource)
  {
    StateGenerator generator = new StateGenerator(mdStateMachine);
    generator.go(false);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do regenerate if the existing file is symantically the same
    if (LocalProperties.isKeepBaseSource() &&
        AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath()))
    {
      return;
    }

    addPackage(getMdTypeDAOIF().getPackage());
    addSignatureAnnotation();
    addEnumName(getMdTypeDAOIF().getValue(MdTypeInfo.NAME));
    addEnumItems();
    addFields();
    addSerialVersionUID();
    addConstructor();
    addGetters();
    addGet(getMdTypeDAOIF().getValue(MdTypeInfo.NAME));
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("STATE", this.getSignature());
  }

  private void addEnumItems()
  {
    Map<String, String> map = new TreeMap<String, String>();
    for (StateMasterDAOIF state : getMdTypeDAOIF().definesStateMasters())
      map.put(state.getName(), getParameters(state));
    writeEnumItems(map);
  }

  private String getParameters(StateMasterDAOIF state)
  {
    String parameters = new String();

    // id
    parameters += '"' + state.getId() + "\", ";

    // entry/default/nonentry
    parameters += '"' + state.getValue(StateMasterDAOIF.ENTRY_STATE) + '"';

    return parameters;
  }

  private void addFields()
  {
    getWriter().writeLine("public static final " + String.class.getName() + " CLASS = \"" + this.getMdTypeDAOIF().definesType() + "\";");

    addField("String", "id");
    addField(ENTRY_ENUM, "entry");
  }

  private void addConstructor()
  {
    String names[] = { "id", "entry" };
    String types[] = { "String", "String" };
    String values[] = { "id", ENTRY_ENUM + ".get(entry)" };

    addConstructorDeclaration(this.getMdTypeDAOIF().getTypeName(), types, names);
    addConstructorBody(names, values);
  }

  private void addGetters()
  {
    addGetter("String", "id");
    addGetter(ENTRY_ENUM, "entry");
  }

  @Override
  protected String getRootSourceDirectory()
  {
    String pack = CommonGenerationUtil.replacePackageDotsWithSlashes(this.getMdTypeDAOIF().getPackage());
    return AbstractServerGenerator.getRootServerStubDirectory(pack);
  }

  @Override
  protected String getRootClassDirectory()
  {
    return AbstractServerGenerator.getRootServerBinDirectory(getPackage());
  }
}
