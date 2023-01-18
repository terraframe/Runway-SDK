/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business.graph;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.graph.EmbeddedGraphObjectDAO;
import com.runwaysdk.dataaccess.graph.EmbeddedGraphObjectDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class EmbeddedGraphObject extends GraphObject
{

  public EmbeddedGraphObject()
  {
    super();
    setGraphObjectDAO(EmbeddedGraphObjectDAO.newInstance(getDeclaredType()));
  }

  public EmbeddedGraphObject(String type)
  {
    super();
    setGraphObjectDAO(EmbeddedGraphObjectDAO.newInstance(type));
  }

  EmbeddedGraphObject(EmbeddedGraphObjectDAO vertexDAO)
  {
    super();
    setGraphObjectDAO(vertexDAO);
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @return the GraphObjectDAO
   */
  public EmbeddedGraphObjectDAO getGraphObjectDAO()
  {
    return (EmbeddedGraphObjectDAO) super.getGraphObjectDAO();
  }

  @Override
  protected String getDeclaredType()
  {
    return EmbeddedGraphObject.class.getName();
  }

  @SuppressWarnings("unchecked")
  protected <T> List<T> convert(List<EmbeddedGraphObjectDAOIF> children, Class<T> clazz)
  {
    LinkedList<T> list = new LinkedList<T>();

    for (EmbeddedGraphObjectDAOIF child : children)
    {
      list.add((T) EmbeddedGraphObject.instantiate((EmbeddedGraphObjectDAO) child));
    }

    return list;
  }

  public static EmbeddedGraphObject instantiate(EmbeddedGraphObjectDAO vertexDAO)
  {
    MdEmbeddedGraphClassDAOIF mdVertexDAO = vertexDAO.getMdClassDAO();

    if (mdVertexDAO.isGenerateSource())
    {
      EmbeddedGraphObject object;

      try
      {
        Class<?> clazz = LoaderDecorator.load(vertexDAO.getType());

        Constructor<?> con = clazz.getConstructor();
        object = (EmbeddedGraphObject) con.newInstance();
        object.setGraphObjectDAO(vertexDAO);
      }
      catch (Exception e)
      {
        throw new ClassLoaderException(vertexDAO.getMdClassDAO(), e);
      }

      return object;
    }

    return new EmbeddedGraphObject((EmbeddedGraphObjectDAO) vertexDAO);
  }
}
