package com.runwaysdk.business.graph;

import java.util.List;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.Session;

public abstract class GraphObject
{

  public final static String CLASS = GraphObject.class.getName();

  /**
   * All interaction with the core is delegated through this object. This should
   * NOT be accessed outside of this class.
   */
  GraphObjectDAO             graphObjectDAO;

  /**
   * Blank constructor can be used for new or existing instances. It is
   * <b>critical</b> that subclasses call
   * {@link GraphObject#setDataGraphObject(GraphObjectDAO)} to correclty
   * initialize the graphObject.
   */
  GraphObject()
  {
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @param graphObjectDAO
   */
  void setGraphObjectDAO(GraphObjectDAO graphObjectDAO)
  {
    this.graphObjectDAO = graphObjectDAO;
  }

  /**
   * Default visibility is on purpose: we don't want all generated classes to
   * see this method.
   * 
   * @return the GraphObjectDAO
   */
  GraphObjectDAO getGraphObjectDAO()
  {
    return graphObjectDAO;
  }

  /**
   * Indicates if this is a new instance. If it is new, then the records that
   * represent this ComponentIF have not been created.
   */
  public boolean isNew()
  {
    return graphObjectDAO.isNew();
  }

  /**
   * Returns a MdClassIF that defines this Component's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a MdClassIF that defines this Component's class.
   */
  public MdClassDAOIF getMdClass()
  {
    return MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * Returns a BusinessDAO representing the attribute metadata of the attribute
   * with the given name.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> name is a valid attribute with respect the this
   * Component's class.
   * 
   * @param name
   *          of the attribute.
   * @return BusinessDAO representing the attribute metadata of the attribute
   *         with the given name
   */
  public MdAttributeDAOIF getMdAttributeDAO(String name)
  {
    return this.graphObjectDAO.getMdAttributeDAO(name);
  }

  /**
   * Returns a LinkedList of BusinessDAOs representing metadata for each
   * attribute defined for this object's class.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   * @return List of BusinessDAOs representing metadata for each attribute
   *         defined for this object's class.
   */
  public List<? extends MdAttributeDAOIF> getMdAttributeDAOs()
  {
    return this.graphObjectDAO.getMdAttributeDAOs();
  }

  /**
   * Returns true if the object has an attribute with the given name, false
   * otherwise. It is case sensitive.
   * 
   * @param name
   *          name of the attribute.
   * @return true if the object has an attribute with the given name, false
   *         otherwise. It is case sensitive.
   */
  public boolean hasAttribute(String name)
  {
    return this.graphObjectDAO.hasAttribute(name);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getValue(String name)
  {
    return this.graphObjectDAO.getObjectValue(name);
  }

  /**
   * Validates the attribute with the given name. If the attribute is not valid,
   * then an AttributeException exception is thrown.
   * 
   * <br/>
   * <b>Precondition:</b> name != null <br/>
   * <b>Precondition:</b> !name.trim().equals("") <br/>
   * <b>Precondition:</b> An attribute of the given name exists for instances of
   * this class
   * 
   * @param name
   *          name of the attribute
   * @throws AttributeException
   *           if the attribute is not valid.
   */
  public void validateAttribute(String name)
  {
    this.graphObjectDAO.validateAttribute(name);
  }

  /**
   * A generic, type-unsafe setter that takes the attribute name a and value as
   * an Object.
   * 
   * @param name
   *          String name of the attribute
   * @param value
   *          String representation of the value
   */
  public void setValue(String name, Object _object)
  {
    graphObjectDAO.setValue(name, _object);
  }

  /**
   * Returns the type of this GraphObject. Generic graphObject objects can
   * represent specific types - this method returns the declared type of the
   * object.
   * 
   * @return The declared type of this object
   */
  protected abstract String getDeclaredType();

  /**
   * Returns the Universally Unique OID (UUID) for this graphObject.
   * 
   * @return <b>this</b> graphObject's UUID
   */
  public String getOid()
  {
    return graphObjectDAO.getOid();
  }

  /**
   * Returns the Id used for AttributeProblems (not messages). New instances
   * that fail will have a different OID on the client.
   * 
   * @return problem notification oid.
   */
  public String getProblemNotificationId()
  {
    return graphObjectDAO.getProblemNotificationId();
  }

  public String getType()
  {
    return graphObjectDAO.getType();
  }

  public String getKey()
  {
    return graphObjectDAO.getKey();
  }

  /**
   * Returns the display label of the class.
   * 
   * @return <b>this</b> graphObject's UUID
   */
  public String getClassDisplayLabel()
  {
    return graphObjectDAO.getMdClassDAO().getDisplayLabel(Session.getCurrentLocale());
  }

  /**
   * Persists this graphObject and all changes to the database.
   * <code><b>new</b></code> instances of GraphObject are <i>not</i> persisted
   * until <code>apply()</code> is called. Similarly, changes made to instances
   * through the generated java classes are not persisted until
   * <code>apply()</code> is called.
   * 
   * <b>Precondition:</b> Session user has a lock on this object, assuming this
   * object has a ComponentIF.LOCKED_BY field.
   */
  public void apply()
  {
    graphObjectDAO.apply();
  }

  /**
   * Deletes this graphObject from the database. Any attempt to
   * {@link GraphObject#apply()} this graphObject will throw an exception, so it
   * is the responsibility of the developer to remove references to deleted
   * instances of GraphObject.
   */
  public void delete()
  {
    graphObjectDAO.delete();
  }

  /**
   * When an object at the business layer is converted into a DTO, this method
   * is invoked to ensure there are not any READ violations that are enforced
   * programatically. This method should be ovewritten in business classes if
   * special programatic READ permissions need to be implemented. This method
   * should throw an exception if declarative READ permissions are not adequate.
   */
  public void customReadCheck()
  {
  }

  /**
   * Writes to standard out all attribute names and their values of this
   * BusinessDAO instance. All values that are keys are dereferenced and the
   * values referenced by those keys are returned.
   * 
   * <br/>
   * <b>Precondition:</b> true
   * 
   */
  public void printAttributes()
  {
    this.graphObjectDAO.printAttributes();
  }

  /**
   * Returns if an attribute of the GraphObject has been modified from its
   * orginal value loaded from the database.
   * 
   * @param name
   *          The name of the attribute
   * @return
   */
  public boolean isModified(String name)
  {
    return graphObjectDAO.getAttribute(name).isModified();
  }

  /**
   * @return Key is a required field, but the default implementation is an empty
   *         string. However, this method should be overwritten in child classes
   *         to return meaningful key values. Key values must be unique for all
   *         entities which are part of the same type hierarchy.
   */
  protected String buildKey()
  {
    return "";
  }

  @Override
  public boolean equals(Object obj)
  {
    if (! ( obj instanceof ComponentIF ))
    {
      return false;
    }

    ComponentIF comp = (ComponentIF) obj;
    return this.getOid().equals(comp.getOid());
  }

  // /**
  // * Returns an object of the specified type with the specified oid from the
  // * database without using reflection. The returned GraphObject is not
  // * typesafe, meaning that its actual type just a GraphObject.
  // *
  // * @param oid
  // * OID of the instance to get.
  // * @return Typesafe GraphObject representing the oid in the database.
  // */
  // public static GraphObject getGraphObject(String oid)
  // {
  // GraphObjectDAO graphObjectDAO =
  // GraphObjectDAO.get(oid).getGraphObjectDAO();
  //
  // return GraphObject.getGraphObject(graphObjectDAO);
  // }
  //
  // public static GraphObject getGraphObject(GraphObjectDAO graphObjectDAO)
  // {
  // if (graphObjectDAO instanceof VertexObjectDAO)
  // {
  // return new VertexObject((VertexObjectDAO) graphObjectDAO);
  // }
  //// else if (graphObjectDAO instanceof EdgeObjectDAO)
  //// {
  //// return new EdgeObject((EdgeObjectDAO) graphObjectDAO);
  //// }
  //
  // throw new UnexpectedTypeException("OID [" + graphObjectDAO.getOid() + "] is
  // not an GraphObject");
  // }
  //
  // /**
  // * Using reflection, get returns an object of the specified type with the
  // * specified oid from the database. The returned GraphObject is typesafe,
  // * meaning that its actual type is that specified by the type parameter.
  // *
  // * @param oid
  // * OID of the instance to get
  // * @return Typesafe Business representing the oid in the database
  // */
  // public static GraphObject get(String oid, String type)
  // {
  // // GraphObjectDAOIF graphObjectDAOIF = GraphObjectDAO.(oid);
  // //
  // // if (graphObjectDAOIF instanceof RelationshipDAO)
  // // {
  // // return Relationship.instantiate((RelationshipDAOIF) graphObjectDAOIF);
  // // }
  // // else if (graphObjectDAOIF instanceof BusinessDAO)
  // // {
  // // return Business.instantiate((BusinessDAOIF) graphObjectDAOIF);
  // // }
  // // else if (graphObjectDAOIF instanceof StructDAO)
  // // {
  // // return Struct.instantiate((StructDAOIF) graphObjectDAOIF);
  // // }
  //
  // throw new UnexpectedTypeException("OID [" + oid + "] is not an
  // GraphObject");
  // }
}
