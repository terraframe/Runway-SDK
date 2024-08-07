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
package com.runwaysdk.dataaccess.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class MdClassStrategy extends MetaDataObjectStrategy
{
  /**
   * 
   */
  private static final long        serialVersionUID = 2320911715091172328L;

  /**
   * Maps class types to the MdClass objects that define them. <br/>
   * <b>invariant</b> this.mdClassTypeMap.size() == this.entityDAOMap.size()
   * <br/>
   * <b>invariant</b> this.mdClassTypeMap references the same objects as
   * this.eMap (i.e. they are in-sync)
   */
  private Map<String, MdClassDAO>  mdClassTypeMap;

  /**
   * Maps class root ids to MdClass objects. <br/>
   * <b>invariant</b> this.mdClassRootIdMap.size() == this.entityDAOMap.size()
   * <br/>
   * <b>invariant</b> this.mdClassRootIdMap references the same objects as
   * this.eMap (i.e. they are in-sync)
   */
  private Map<String, MdClassDAO>  mdClassRootIdMap;

  /**
   * Maps entity table names to their corresponding <code>MdEntityDAO</code>.
   */
  private Map<String, MdClassDAO>  mdClassTableNameMap;

  /**
   * key: {@link MdBusinessDAOIF} oid. Value: Set of {@link MdRelationshipDAOIF}
   * ids where MdBusiness participates as a parent.
   */
  private Map<String, Set<String>> mdBusinessParentMdRelationships;

  /**
   * key:{@link MdBusinessDAOIF} oid. Value: Set of {@link MdRelationshipDAOIF}
   * ids where MdBusiness participates as a child.
   */
  private Map<String, Set<String>> mdBusinessChildMdRelationships;

  /**
   * key: {@link MdVertexDAOIF} oid. Value: Set of {@link MdRelationshipDAOIF}
   * ids where MdBusiness participates as a parent.
   */
  private Map<String, Set<String>> mdVertexParentMdEdges;

  /**
   * key: {@link MdVertexDAOIF} oid. Value: Set of {@link MdRelationshipDAOIF}
   * ids where MdBusiness participates as a child.
   */
  private Map<String, Set<String>> mdVertexChildMdEdges;

  /**
   * Initializes the BusinessDAOCollection with the all MdClass objects.
   *
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("")
   *
   * @param classType
   *          name of the type of the objects in this collection
   */
  public MdClassStrategy(String classType)
  {
    super(classType);

    this.mdClassTypeMap = new HashMap<String, MdClassDAO>();
    this.mdClassRootIdMap = new HashMap<String, MdClassDAO>();

    this.mdClassTableNameMap = new HashMap<String, MdClassDAO>();

    this.mdBusinessParentMdRelationships = new HashMap<String, Set<String>>();
    this.mdBusinessChildMdRelationships = new HashMap<String, Set<String>>();

    this.mdVertexParentMdEdges = new HashMap<String, Set<String>>();
    this.mdVertexChildMdEdges = new HashMap<String, Set<String>>();
  }

  /**
   * Returns a MdClassIF instance that defines the class of the given type.
   *
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("") <br/>
   * <b>Precondition:</b> type is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * (mdClass.getType().equals(type)
   *
   * @param type
   *          Name of the class.
   * @return MdClassIF instance that defines the class of the given type.
   */
  public synchronized MdClassDAOIF getMdClass(String type)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdClassDAOIF mdClass = this.getMdClassReturnNull(type);
    if (mdClass == null)
    {
      String error = "The MdClass that defines [" + type + "] was not found.";

      throw new DataNotFoundException(error, MdClassDAO.getMdClassDAO(MdClassInfo.CLASS));
    }

    return mdClass;
  }

  /**
   * returns null if the type is not known.
   * 
   * @param type
   * @return
   */
  public synchronized MdClassDAOIF getMdClassReturnNull(String type)
  {
    if (reload == true)
    {
      this.reload();
    }

    return this.mdClassTypeMap.get(type);
  }

  /**
   * Returns a <code>MdClassDAOIF</code> instance with a root oid that matches
   * the given value.
   *
   * <br/>
   * <b>Precondition:</b> rootId != null <br/>
   * <b>Precondition:</b> !rootId.trim().equals("") <br/>
   * <b>Precondition:</b> rootId is the root of an oid that is a valid class
   * defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdClassIF where
   * IdParser.parseRootFromId(mdClass.getOid()).equals(rootId)
   *
   * @param rootId
   *          of the MdClass.
   * @return MdClassIF instance with a root oid that matches the given value.
   */
  public synchronized MdClassDAOIF getMdClassByRootId(String rootId)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdClassDAO mdClass = this.mdClassRootIdMap.get(rootId);
    if (mdClass == null)
    {
      String error = "The " + MdClassInfo.CLASS + " with root oid [" + rootId + "] was not found.";

      throw new DataNotFoundException(error, MdClassDAO.getMdClassDAO(MdClassInfo.CLASS));
    }

    return mdClass;
  }

  /**
   * Returns a <code>MdEntityDAOIF</code> instance that defines the given table
   * name.
   * 
   * @param tableName
   * @return <code>MdEntityDAOIF</code> that defines the table with the given
   *         name.
   */
  public synchronized MdClassDAOIF getMdClassByTableName(String tableName)
  {
    if (reload == true)
    {
      this.reload();
    }

    MdClassDAO mdClassDAO = this.mdClassTableNameMap.get(tableName);
    if (mdClassDAO == null)
    {
      String error = "No " + MdEntityInfo.CLASS + " defines a table with name [" + tableName + "].";

      throw new DataNotFoundException(error, MdClassDAO.getMdClassDAO(MdEntityInfo.CLASS));
    }

    return mdClassDAO;
  }

  public boolean hasClassByTableName(String tableName)
  {
    if (reload == true)
    {
      this.reload();
    }

    return this.mdClassTableNameMap.containsKey(tableName);
  }

  /**
   * Reloads the cache of this collection. The cache is cleared. All EntityDAOs
   * of this type stored in this collection are instantiated an put in the
   * cache. This method uses the database instead of the metadata cache.
   *
   * <br/>
   * <b>Precondition:</b> true
   *
   * <br/>
   * <b>Postcondition:</b> Cache contains all EntityDAOs of this type that are
   * to be stored in this collection
   *
   */
  public synchronized void reload()
  {
    this.removeCacheStrategy();

    this.entityDAOIdSet.clear();
    this.entityDAOIdByKeyMap.clear();
    this.mdClassTypeMap.clear();
    this.mdClassRootIdMap.clear();
    this.mdClassTableNameMap.clear();

    super.reload();

    reload = false;
  }

  /**
   * Returns a list of sub types(including this type).
   * 
   * @return a list of sub types(including this type).
   */
  protected List<String> getSubTypesFromDatabase()
  {
    return Database.getConcreteSubClasses(MdClassInfo.ID_VALUE);
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given oid participates as a
   * parent.
   *
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  protected synchronized Set<String> getParentMdRelationshipDAOids(String mdBusinessDAOid)
  {
    if (this.mdBusinessParentMdRelationships.containsKey(mdBusinessDAOid))
    {
      return this.mdBusinessParentMdRelationships.get(mdBusinessDAOid);
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Returns a set of <code>MdRelationshipDAOIF</code> ids for relationships in
   * which the <code>MdBusinessDAOIF</code> with the given oid participates as a
   * child.
   *
   * @return set of <code>MdRelationshipDAOIF</code> ids
   */
  protected synchronized Set<String> getChildMdRelationshipDAOids(String mdBusinessDAOid)
  {
    if (this.mdBusinessChildMdRelationships.containsKey(mdBusinessDAOid))
    {
      return this.mdBusinessChildMdRelationships.get(mdBusinessDAOid);
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Returns a set of <code>MdEdgeDAOIF</code> ids for relationships in which
   * the <code>MdVertexDAOIF</code> with the given oid participates as a parent.
   *
   * @return set of <code>MdEdgeDAOIF</code> ids
   */
  protected synchronized Set<String> getParentMdEdgeDAOids(String mdVertexsDAOid)
  {
    if (this.mdVertexParentMdEdges.containsKey(mdVertexsDAOid))
    {
      return this.mdVertexParentMdEdges.get(mdVertexsDAOid);
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Returns a set of <code>MdEdgeDAOIF</code> ids for relationships in which
   * the <code>MdVertexDAOIF</code> with the given oid participates as a child.
   *
   * @return set of <code>MdEdgeDAOIF</code> ids
   */
  protected synchronized Set<String> getChildMdEdgeDAOids(String mdVertexDAOid)
  {
    if (this.mdVertexChildMdEdges.containsKey(mdVertexDAOid))
    {
      return this.mdVertexChildMdEdges.get(mdVertexDAOid);
    }
    else
    {
      return new HashSet<String>();
    }
  }

  /**
   * Places the given EntityDAO into the cache.
   *
   * <br/>
   * <b>Precondition:</b> mdClass != null <br/>
   * <b>Precondition:</b> mdClass.getTypeName().equals(MdClassInfo.CLASS)
   *
   * <br/>
   * <b>Postcondition:</b> cache contains the given EntityDAO
   *
   * @param mdType
   *          EntityDAO to add to this collection
   */
  public void updateCache(EntityDAO entityDAO)
  {
    synchronized (entityDAO.getOid())
    {
      super.updateCache(entityDAO);

      MdClassDAO mdClassDAO = (MdClassDAO) entityDAO;

      this.mdClassTypeMap.put(mdClassDAO.definesType(), mdClassDAO);
      // this.mdClassRootIdMap.put(IdParser.parseRootFromId(mdClassDAO.getOid()),
      // mdClassDAO);
      this.mdClassRootIdMap.put(mdClassDAO.getRootId(), mdClassDAO);

      if (mdClassDAO instanceof MdEntityDAO)
      {
        MdEntityDAO mdEntityDAO = (MdEntityDAO) mdClassDAO;
        this.mdClassTableNameMap.put(mdEntityDAO.getTableName(), mdEntityDAO);
      }
      else if (mdClassDAO instanceof MdGraphClassDAO)
      {
        MdGraphClassDAO mdEntityDAO = (MdGraphClassDAO) mdClassDAO;
        this.mdClassTableNameMap.put(mdEntityDAO.getDBClassName(), mdEntityDAO);
      }

      if (mdClassDAO instanceof MdRelationshipDAO)
      {
        MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO) mdClassDAO;

        String parentMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.PARENT_MD_BUSINESS).getValue();
        Set<String> parentRelationshipSet;
        if (this.mdBusinessParentMdRelationships.containsKey(parentMdBusinessId))
        {
          parentRelationshipSet = this.mdBusinessParentMdRelationships.get(parentMdBusinessId);
        }
        else
        {
          parentRelationshipSet = new HashSet<String>();
          this.mdBusinessParentMdRelationships.put(parentMdBusinessId, parentRelationshipSet);
        }
        parentRelationshipSet.add(mdRelationshipDAO.getOid());

        String childMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.CHILD_MD_BUSINESS).getValue();
        Set<String> childRelationshipSet;
        if (this.mdBusinessChildMdRelationships.containsKey(childMdBusinessId))
        {
          childRelationshipSet = this.mdBusinessChildMdRelationships.get(childMdBusinessId);
        }
        else
        {
          childRelationshipSet = new HashSet<String>();
          this.mdBusinessChildMdRelationships.put(childMdBusinessId, childRelationshipSet);
        }
        childRelationshipSet.add(mdRelationshipDAO.getOid());
      }
      else if (mdClassDAO instanceof MdEdgeDAO)
      {
        MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) mdClassDAO;

        String parentMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.PARENT_MD_VERTEX).getValue();
        Set<String> parentEdgeSet;
        if (this.mdVertexParentMdEdges.containsKey(parentMdVertexId))
        {
          parentEdgeSet = this.mdVertexParentMdEdges.get(parentMdVertexId);
        }
        else
        {
          parentEdgeSet = new HashSet<String>();
          this.mdVertexParentMdEdges.put(parentMdVertexId, parentEdgeSet);
        }
        parentEdgeSet.add(mdEdgeDAO.getOid());

        String childMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.CHILD_MD_VERTEX).getValue();
        Set<String> childEdgeSet;
        if (this.mdVertexChildMdEdges.containsKey(childMdVertexId))
        {
          childEdgeSet = this.mdVertexChildMdEdges.get(childMdVertexId);
        }
        else
        {
          childEdgeSet = new HashSet<String>();
          this.mdVertexChildMdEdges.put(childMdVertexId, childEdgeSet);
        }
        childEdgeSet.add(mdEdgeDAO.getOid());

      }
    }
  }

  /**
   * Removes the given EntityDAO from the cache.
   *
   * <br/>
   * <b>Precondition:</b> mdClass != null <br/>
   * <b>Precondition:</b> mdClass.getTypeName().equals(MdClassInfo.CLASS)
   *
   * <br/>
   * <b>Postcondition:</b> cache no longer contains the given EntityDAO
   *
   * @param mdClass
   *          EntityDAO to remove from this collection
   */
  public void removeCache(EntityDAO mdClassDAO)
  {
    synchronized (mdClassDAO.getOid())
    {
      super.removeCache(mdClassDAO);

      this.mdClassTypeMap.remove( ( (MdClassDAOIF) mdClassDAO ).definesType());
      this.mdClassRootIdMap.remove( ( (MdClassDAOIF) mdClassDAO ).getRootId());

      if (mdClassDAO instanceof MdEntityDAO)
      {
        MdEntityDAO mdEntityDAO = (MdEntityDAO) mdClassDAO;
        this.mdClassTableNameMap.remove(mdEntityDAO.getTableName());
      }
      else if (mdClassDAO instanceof MdGraphClassDAO)
      {
        MdGraphClassDAO mdEntityDAO = (MdGraphClassDAO) mdClassDAO;
        this.mdClassTableNameMap.remove(mdEntityDAO.getDBClassName());
      }

      if (mdClassDAO instanceof MdRelationshipDAO)
      {
        MdRelationshipDAO mdRelationshipDAO = (MdRelationshipDAO) mdClassDAO;

        String parentMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.PARENT_MD_BUSINESS).getValue();
        if (this.mdBusinessParentMdRelationships.containsKey(parentMdBusinessId))
        {
          this.mdBusinessParentMdRelationships.get(parentMdBusinessId).remove(mdRelationshipDAO.getOid());
        }

        String childMdBusinessId = mdRelationshipDAO.getAttribute(MdRelationshipInfo.CHILD_MD_BUSINESS).getValue();
        if (this.mdBusinessChildMdRelationships.containsKey(childMdBusinessId))
        {
          this.mdBusinessChildMdRelationships.get(childMdBusinessId).remove(mdRelationshipDAO.getOid());
        }
      }
      else if (mdClassDAO instanceof MdBusinessDAO)
      {
        MdBusinessDAO mdBusinessDAO = (MdBusinessDAO) mdClassDAO;

        this.mdBusinessParentMdRelationships.remove(mdBusinessDAO.getOid());
        this.mdBusinessChildMdRelationships.remove(mdBusinessDAO.getOid());
      }
    }

    if (mdClassDAO instanceof MdEdgeDAO)
    {
      MdEdgeDAO mdEdgeDAO = (MdEdgeDAO) mdClassDAO;

      String parentMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.PARENT_MD_VERTEX).getValue();
      if (this.mdVertexParentMdEdges.containsKey(parentMdVertexId))
      {
        this.mdVertexParentMdEdges.get(parentMdVertexId).remove(mdEdgeDAO.getOid());
      }

      String childMdVertexId = mdEdgeDAO.getAttribute(MdEdgeInfo.CHILD_MD_VERTEX).getValue();
      if (this.mdVertexChildMdEdges.containsKey(childMdVertexId))
      {
        this.mdVertexChildMdEdges.get(childMdVertexId).remove(mdEdgeDAO.getOid());
      }
    }
    else if (mdClassDAO instanceof MdVertexDAO)
    {
      MdVertexDAO mdVertexDAO = (MdVertexDAO) mdClassDAO;

      this.mdVertexParentMdEdges.remove(mdVertexDAO.getOid());
      this.mdVertexChildMdEdges.remove(mdVertexDAO.getOid());
    }
  }
}
