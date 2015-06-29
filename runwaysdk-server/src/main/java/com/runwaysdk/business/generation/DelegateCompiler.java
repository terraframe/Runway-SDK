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
package com.runwaysdk.business.generation;

import java.util.Collection;

import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.generation.CommonMarker;

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
/**
 * This delegate compiler uses the EclipseCompiler for common and client code and the
 * AspectJCompiler for server code.
 * 
 * @author Richard Rowlands
 */
public class DelegateCompiler extends AbstractCompiler
{
  /**
   * Compiler used to compile code on the server
   */
  AspectJCompiler ajc;

  /**
   * Compiler used to compile code on the client
   */
  EclipseCompiler javac;

  Arguments       emptyArgs;

  public DelegateCompiler()
  {
    // Intentionally do not super
    ajc = new AspectJCompiler();
    javac = new EclipseCompiler();
    emptyArgs = new Arguments();
  }

  @Override
  protected void compile(Collection<? extends MdTypeDAOIF> types)
  {
    // No need to go further if there is nothing to compile
    if (types.size() == 0)
    {
      return;
    }

    for (MdTypeDAOIF mdType : types)
    {
      for (String source : GenerationManager.getServerFiles(mdType))
      {
        ajc.arguments.server.addSourceFile(source);
      }

      for (String source : GenerationManager.getCommonFiles(mdType))
      {
        /*
         * Both the server and client code needs to include the common source
         * files in-order to compile.
         */
        // ajc.arguments.common.addSourceFile(source);
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
  protected void compileAllNoOutput()
  {
    javac.arguments.common.setDestination("none");
    ajc.arguments.server.setDestination("none");
    javac.arguments.client.setDestination("none");

    compileAll();
  }

  @Override
  protected void compileAll()
  {
    /*
     * Have the common source be compiled by the javac compiler and add the
     * destination folder to the classpath of the ajc compiler. Common source
     * isn't woven into.
     */

    javac.arguments.common.addSourceDir(CommonMarker.BASE_DIRECTORY);
    javac.arguments.common.addSourceDir(CommonMarker.SOURCE_DIRECTORY);
    javac.arguments.client.addSourceDir(ClientMarker.BASE_DIRECTORY);
    javac.arguments.client.addSourceDir(ClientMarker.SOURCE_DIRECTORY);

    ajc.arguments.server.addSourceDir(ServerMarker.BASE_DIRECTORY);
    ajc.arguments.server.addSourceDir(ServerMarker.SOURCE_DIRECTORY);

    execute();
  }

  /**
   * @see com.runwaysdk.business.generation.AbstractCompiler#execute()
   */
  @Override
  void execute()
  {
    javac.arguments.server = emptyArgs.server;

    /*
     * Only compile the server arguments with the AspectJ compiler. We do not
     * weave into the common or client class files.
     */
    ajc.arguments.client = emptyArgs.client;
    ajc.arguments.common = emptyArgs.common;
    ajc.arguments.server.addClasspath(javac.arguments.common.getDestination());

    if (!canCompileClient)
    {
      javac.arguments.client = emptyArgs.client;
    }

    /*
     * javac must be executed first because its common generated class files
     * must be on the classpath of the ajc compiler.
     */
    javac.execute();
    ajc.execute();
  }

}
