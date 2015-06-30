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

import com.runwaysdk.business.generation.facade.ControllerStubGenerator;
import com.runwaysdk.business.generation.facade.ControllerStubGeneratorIF;
import com.runwaysdk.business.generation.view.ContentListener;
import com.runwaysdk.business.generation.view.ContentProvider;
import com.runwaysdk.business.generation.view.ContentProviderIF;
import com.runwaysdk.business.generation.view.CreateComponentListener;
import com.runwaysdk.business.generation.view.FormListener;
import com.runwaysdk.business.generation.view.NewRelationshipComponentListener;
import com.runwaysdk.business.generation.view.UpdateComponentListener;
import com.runwaysdk.business.generation.view.ViewAllComponentListener;
import com.runwaysdk.business.generation.view.ViewComponentListener;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;

/**
 * Default implementation of {@link ProviderBuilderIF}. This implementation
 * builds a {@link ContentProviderIF} with all of the {@link ContentListener}s
 * in runway.  Additionally, this supplies the default {@link ControllerStubGeneratorIF}
 * implementation which generating controller stub source.
 * 
 * @author Justin Smethie
 */
public class ProviderBuilder implements ProviderBuilderIF
{
  public ContentProviderIF getProvider(MdEntityDAOIF mdEntity)
  {
    ContentProviderIF provider = new ContentProvider();

    provider.registerContentListener(new ViewAllComponentListener(mdEntity));
    provider.registerContentListener(new ViewComponentListener(mdEntity));
    provider.registerContentListener(new FormListener(mdEntity));
    provider.registerContentListener(new CreateComponentListener(mdEntity));
    provider.registerContentListener(new UpdateComponentListener(mdEntity));

    if (mdEntity instanceof MdRelationshipDAOIF)
    {
      provider.registerContentListener(new NewRelationshipComponentListener((MdRelationshipDAOIF) mdEntity));
    }

    return provider;
  }

  public ControllerStubGeneratorIF getControllerStubGenerator(MdControllerDAOIF mdController)
  {
    return new ControllerStubGenerator(mdController);
  }
}
