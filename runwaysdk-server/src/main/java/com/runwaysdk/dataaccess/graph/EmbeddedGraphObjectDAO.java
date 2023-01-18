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
package com.runwaysdk.dataaccess.graph;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDAO;

public class EmbeddedGraphObjectDAO extends GraphObjectDAO implements EmbeddedGraphObjectDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -7425379312301977450L;

  /**
   * The default constructor, does not set any attributes
   */
  public EmbeddedGraphObjectDAO()
  {
    super();
  }

  /**
   * Constructs a {@link EmbeddedGraphObjectDAO} from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public EmbeddedGraphObjectDAO(Map<String, Attribute> attributeMap, MdEmbeddedGraphClassDAOIF mdVertexDAOIF)
  {
    super(attributeMap, mdVertexDAOIF);
  }

  /**
   * Returns a {@link MdEmbeddedGraphClassDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdEmbeddedGraphClassDAOIF} that defines this Component's class.
   */
  @Override
  public MdEmbeddedGraphClassDAOIF getMdClassDAO()
  {
    return (MdEmbeddedGraphClassDAOIF) super.getMdClassDAO();
  }

  public static EmbeddedGraphObjectDAO newInstance(String vertexType)
  {
    // get the meta data for the given class
    MdEmbeddedGraphClassDAOIF mdVertexDAOIF = MdEmbeddedGraphClassDAO.getMdEmbeddedGraphClassDAO(vertexType);

    return newInstance(mdVertexDAOIF);
  }

  /**
   * Returns a new {@link EmbeddedGraphObjectDAO} instance of the given class name.
   * Default values are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param mdVertexDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * @return new {@link EmbeddedGraphObjectDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static EmbeddedGraphObjectDAO newInstance(MdEmbeddedGraphClassDAOIF mdVertexDAOIF)
  {
    checkIsAbstract(mdVertexDAOIF);

    Hashtable<String, Attribute> attributeMap = getAttributeMap(mdVertexDAOIF);

    EmbeddedGraphObjectDAO embeddedGraphObjectDAO = new EmbeddedGraphObjectDAO(attributeMap, mdVertexDAOIF);

    initialize(attributeMap, embeddedGraphObjectDAO);

    return embeddedGraphObjectDAO;
  }

  /**
   * Checks if the defining type is abstract. If so, a {@link VectorObjectDAO}
   * cannot be instantiated.
   * 
   * @param mdVertexDAOIF
   * @throws AbstractInstantiationException
   */
  protected static void checkIsAbstract(MdEmbeddedGraphClassDAOIF mdVertexDAOIF)
  {
    if (mdVertexDAOIF.isAbstract())
    {
      String errMsg = "Class [" + mdVertexDAOIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdVertexDAOIF);
    }
  }

  /**
   * Returns an {@link Attribute} map to be used by a a {@link EmbeddedGraphObjectDAO}.
   * 
   * @param mdEmbeddedDAOIF
   * @return
   */
  protected static Hashtable<String, Attribute> getAttributeMap(MdEmbeddedGraphClassDAOIF mdEmbeddedDAOIF)
  {
    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<? extends MdGraphClassDAOIF> superMdVertexList = mdEmbeddedDAOIF.getSuperClasses();
    superMdVertexList.forEach(md -> attributeMap.putAll(GraphObjectDAO.createRecordsForEntity(md)));

    return attributeMap;
  }

  /**
   * Initializes the given {@link EmbeddedGraphObjectDAO} and {@link Attribute}
   * objects.
   * 
   * @param attributeMap
   * @param embeddedGraphObjectDAO
   */
  protected static void initialize(Hashtable<String, Attribute> attributeMap, EmbeddedGraphObjectDAO embeddedGraphObjectDAO)
  {
    attributeMap.values().forEach(a -> a.setContainingComponent(embeddedGraphObjectDAO));

    embeddedGraphObjectDAO.setIsNew(true);

    embeddedGraphObjectDAO.setAppliedToDB(false);
  }

}
