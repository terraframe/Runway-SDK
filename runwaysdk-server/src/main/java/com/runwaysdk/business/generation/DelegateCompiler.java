/**
 * 
 */
package com.runwaysdk.business.generation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.generation.CommonMarker;

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
/**
 * This compiler will delegate client and common compilations to EclipseCompiler and server to AspectJCompiler
 */
public class DelegateCompiler extends AbstractCompiler
{
  AspectJCompiler ajc;
  EclipseCompiler javac;
  Arguments emptyArgs;
  
  public DelegateCompiler() {
    // Intentionally do not super
    ajc = new AspectJCompiler();
    javac = new EclipseCompiler();
    emptyArgs = new Arguments();
  }
  
  @Override
  protected void compile(Collection<? extends MdTypeDAOIF> types) {
    // No need to go further if there is nothing to compile
    if (types.size() == 0)
      return;

    for (MdTypeDAOIF mdType : types)
    {
      for (String source : GenerationManager.getServerFiles(mdType))
      {
        ajc.arguments.server.addSourceFile(source);
      }

      for (String source : GenerationManager.getCommonFiles(mdType))
      {
        javac.arguments.common.addSourceFile(source);
      }

      for (String source : GenerationManager.getClientFiles(mdType))
      {
        javac.arguments.client.addSourceFile(source);
      }
    }

    execute();
  }
  
  @Override
  protected void compileAllNoOutput() {
    javac.arguments.common.setDestination("none");
    ajc.arguments.server.setDestination("none");
    javac.arguments.client.setDestination("none");

    compileAll();
  }
  
  @Override
  protected void compileAll() {
    javac.arguments.common.addSourceDir(CommonMarker.BASE_DIRECTORY);
    javac.arguments.common.addSourceDir(CommonMarker.SOURCE_DIRECTORY);
    ajc.arguments.server.addSourceDir(ServerMarker.BASE_DIRECTORY);
    ajc.arguments.server.addSourceDir(ServerMarker.SOURCE_DIRECTORY);
    javac.arguments.client.addSourceDir(ClientMarker.BASE_DIRECTORY);
    javac.arguments.client.addSourceDir(ClientMarker.SOURCE_DIRECTORY);

    execute();
  }
  
  
  /**
   * @see com.runwaysdk.business.generation.AbstractCompiler#execute()
   */
  @Override
  void execute()
  {
    ajc.arguments.client = emptyArgs.client;
    ajc.arguments.common = emptyArgs.common;
    javac.arguments.server = emptyArgs.server;
    
    if (!canCompileClient) {
      javac.arguments.client = emptyArgs.client;
    }
    
    ajc.execute();
    javac.execute();
  }
  
  private boolean containsAspectJRT(List<String> classpath) {
    for (Iterator<String> i = classpath.iterator(); i.hasNext();) {
      String jarPath = i.next();
      if (jarPath.contains("aspectjrt")) {
        return true;
      }
    }
    return false;
  }

}
