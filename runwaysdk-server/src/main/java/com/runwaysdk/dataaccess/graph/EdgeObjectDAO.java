package com.runwaysdk.dataaccess.graph;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdEdgeDAO;

public class EdgeObjectDAO extends GraphObjectDAO implements EdgeObjectDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 801720319418056258L;

  /**
   * oid of the parent BusinessDAO in the relationship. <br/>
   * <b>invariant </b> parentOid != null <br/>
   * <b>invariant </b> !parentOid.trim().equals("") <br/>
   */
  private String            parentOid;

  /**
   * The old oid of the parent if the parent has changed.
   */
  private String            oldParentOid;

  /**
   * oid of the child BusinessDAO in the relationship. <br/>
   * <b>invariant </b> childOid != null <br/>
   * <b>invariant </b> !childOid().equals("") <br/>
   */
  private String            childOid;

  /**
   * The old oid of the parent if the child has changed.
   */
  private String            oldChildOid;

  /**
   * The default constructor, does not set any attributes
   */
  public EdgeObjectDAO()
  {
    super();
  }

  /**
   *
   */
  public EdgeObjectDAO(Map<String, Attribute> attributeMap, MdEdgeDAOIF mdEdgeDAOIF, String parentOid, String childOid)
  {
    super(attributeMap, mdEdgeDAOIF);

    this.parentOid = parentOid;
    this.childOid = childOid;

    this.oldParentOid = null;
    this.oldChildOid = null;
  }

  /**
   * Returns a {@link MdEdgeDAOIF} that defines this Component's class.
   *
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   *
   * @return a {@link MdEdgeDAOIF} that defines this Component's class.
   */
  @Override
  public MdEdgeDAOIF getMdClassDAO()
  {
    return (MdEdgeDAOIF) super.getMdClassDAO();
  }

  /**
   * Clears the old ids, to be called only when this object has been applied to
   * the global cache at the end of a transaction.
   */
  public void clearOldRelIds()
  {
    this.oldParentOid = null;
    this.oldChildOid = null;
  }

  /**
   * Returns the oid of the parent BusinessDAO in this relationship.
   * 
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   * 
   * @return oid of the parent BusinessDAO in this relationship
   */
  public String getParentOid()
  {
    return this.parentOid;
  }

  /**
   * Sets the parent oid to a new oid. ONLY CALL THIS ONCE IN A TRANSACTION!
   * 
   * @param newParentOid
   */
  public void setParentOid(String newParentOid)
  {
    if (!this.getParentOid().equals(newParentOid))
    {
      this.oldParentOid = this.parentOid;
      this.parentOid = newParentOid;
    }
  }

  /**
   * @return the oldParentOid
   */
  public String getOldParentOid()
  {
    return oldParentOid;
  }

  /**
   * @param oldParentOid
   *          the oldParentOid to set
   */
  public void setOldParentOid(String oldParentOid)
  {
    this.oldParentOid = oldParentOid;
  }

  /**
   * Return true if the child oid has changed, false otherwise.
   * 
   * @return true if the child oid has changed, false otherwise.
   */
  public boolean hasChildOidChanged()
  {
    if (this.oldChildOid != null && !this.oldChildOid.equals(this.childOid))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Return true if the parent oid has changed, false otherwise.
   * 
   * @return true if the parent oid has changed, false otherwise.
   */
  public boolean hasParentOidChanged()
  {
    if (this.oldParentOid != null && !this.oldParentOid.equals(this.parentOid))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the oid of the child BusinessDAO in this relationship.
   * 
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> return value != null
   * 
   * @return oid of the child BusinessDAO in this relationship
   */
  public String getChildOid()
  {
    return this.childOid;
  }

  /**
   * Sets the child oid to a new oid. ONLY CALL THIS ONCE IN A TRANSACTION!
   * 
   * @param newChildOid
   */
  public void setChildOid(String newChildOid)
  {
    if (!this.getChildOid().equals(newChildOid))
    {
      this.oldChildOid = this.childOid;
      this.childOid = newChildOid;
    }
  }

  /**
   * @return the oldChildOid
   */
  public String getOldChildOid()
  {
    return oldChildOid;
  }

  /**
   * @param oldChildOid
   *          the oldChildOid to set
   */
  public void setOldChildOid(String oldChildOid)
  {
    this.oldChildOid = oldChildOid;
  }

  public static EdgeObjectDAO newInstance(String parentOid, String childOid, String edgeType)
  {
    // get the meta data for the given class
    MdEdgeDAOIF mdEdgeDAOIF = MdEdgeDAO.getMdEdgeDAO(edgeType);

    return newInstance(parentOid, childOid, mdEdgeDAOIF);
  }

  /**
   * Returns a new {@link EdgeObjectDAO} instance of the given class name.
   * Default values are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param mdEdgeDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * @return new {@link EdgeObjectDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static EdgeObjectDAO newInstance(String parentOid, String childOid, MdEdgeDAOIF mdEdgeDAOIF)
  {
    if (mdEdgeDAOIF.isAbstract())
    {
      String errMsg = "Class [" + mdEdgeDAOIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdEdgeDAOIF);
    }

    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<? extends MdEdgeDAOIF> superMdEdgeList = mdEdgeDAOIF.getSuperClasses();
    superMdEdgeList.forEach(md -> attributeMap.putAll(GraphObjectDAO.createRecordsForEntity(md)));

    EdgeObjectDAO edgeObjectDAO = new EdgeObjectDAO(attributeMap, mdEdgeDAOIF, parentOid, childOid);

    attributeMap.values().forEach(a -> a.setContainingComponent(edgeObjectDAO));

    edgeObjectDAO.setIsNew(true);
    
    edgeObjectDAO.setAppliedToDB(false);

    return edgeObjectDAO;
  }

}
