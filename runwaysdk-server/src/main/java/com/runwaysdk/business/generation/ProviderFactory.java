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
package com.runwaysdk.business.generation;

import com.runwaysdk.business.generation.facade.ControllerStubGeneratorIF;
import com.runwaysdk.business.generation.view.ContentListener;
import com.runwaysdk.business.generation.view.ContentProviderIF;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * Factory which delegates to a custom or default {@link ProviderBuilderIF} in
 * order to construct a {@link ContentProviderIF} and
 * {@link ControllerStubGeneratorIF}. A custom {@link ProviderBuilderIF} is used
 * if and only if the server property 'provider.builder' specifies the fully
 * qualified package and name of a type which implements the
 * {@link ProviderBuilderIF} interface. Otherwise, the default implementation,
 * {@link ProviderBuilder}, is used.
 * 
 * @author Justin Smethie
 */
public class ProviderFactory
{
  /**
   * Builder used to construct the appropriate {@link ContentProviderIF} and
   * {@link ControllerStubGeneratorIF}
   */
  private ProviderBuilderIF builder;

  /**
   * 
   */
  public ProviderFactory()
  {
    // Check to see if the property was defined before trying to instiate it.
    // This is faster than performing a try/catch when the property does not
    // exist.
    String klass = ServerProperties.getProviderBuilder();
    if (klass == null || klass.equals(""))
    {
      builder = new ProviderBuilder();
    }
    else
    {
      try
      {
        Class<?> c = LoaderDecorator.load(klass);

        if (ProviderBuilderIF.class.isAssignableFrom(c))
        {
          builder = (ProviderBuilderIF) c.newInstance();
        }
        else
        {
          String error = "The provider builder class ["+klass+"] does not implement ["+ProviderBuilderIF.class.getName()+"].";
          throw new ProgrammingErrorException(error);
        }
      }
      catch (Throwable t)
      {
        String error;
        if(t instanceof ClassNotFoundException ||
          (t.getCause() != null && t.getCause() instanceof ClassNotFoundException))
        {
          // Be specific that the class was not found to avoid a painful debugging process
          error = "The provider builder class ["+klass+"] was not found.";
        }
        else
        {
          error = "An error occurred while processing the provider builder class ["+klass+"].";
        }
        
        throw new ProgrammingErrorException(error, t);
      }
    }
  }

  /**
   * This method is responsible for creating and registering all of the
   * {@link ContentListener} which will be used when generating the view content
   * of the given {@link MdEntityDAOIF}.
   * 
   * @param mdEntity
   * @return Fully registered {@link ContentProviderIF}.
   */
  public ContentProviderIF getProvider(MdEntityDAOIF mdEntity)
  {
    return builder.getProvider(mdEntity);
  }

  /**
   * @param mdController
   * @return The {@link ControllerStubGeneratorIF} which generates the stub
   *         source for the given {@link MdControllerDAOIF}.
   */
  public ControllerStubGeneratorIF getControllerStubGenerator(MdControllerDAOIF mdController)
  {
    return builder.getControllerStubGenerator(mdController);
  }
}
