package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;

/**
 * Metadata classes that implement this interface reference a database table.
 * 
 * @author nathan
 *
 */
public interface MdTableClassIF extends MdClassDAOIF
{
  /**
   * Returns the name of the table that this metadata represents.
   * @return name of the table that this metadata represents.
   */
  public String getTableName();
  
  /**
   * Returns an {@link MdTableClassIF} representing the super class of this class, or null if
   * it does not have one.
   *
   * @return an  {@link MdTableClassIF} representing the super class of this class, or null if
   * it does not have one.
   */
  public MdTableClassIF getSuperClass();
  
  /**
   * Returns an array of {@link MdTableClassIF}  that defines immediate subclasses of this class.
   * @return an array of {@link MdTableClassIF}  that defines immediate subclasses of this class.
   */
  public List<? extends MdTableClassIF> getSubClasses();

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF} objects defined by this entity.
   * Key: attribute name in lower case Value: {@link MdAttributeConcreteDAOIF} 
   * @return map of {@link MdAttributeConcreteDAOIF} objects defined by this entity.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getDefinedMdAttributeMap();
  
  /**
   * Returns a complete list of {@link MdAttributeConcreteDAOIF} objects for this
   * instance of {@link MdBusinessDAOIF}. This list includes attributes inherited
   * from supertypes.
   *
   * @return a list of MdAttributeIF objects
   */
  public List<? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributes();
  

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF}  objects defined by the given entity type plus all
   * attributes defined by parent entities.  The key of the map is the attribute name
   * in lower case.
   *
   * @param  type Name of the entity
   * @return map of {@link MdAttributeConcreteDAOIF}  objects defined by the given entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeMap();

  /**
   * Returns a map of {@link MdAttributeConcreteDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   * <p/>
   * <br/>Map Key: mdAttributeID
   * <br/>Map Value: {@link MdAttributeConcreteDAOIF} 
   * <p/>
   * @return map of {@link MdAttributeConcreteDAOIF} objects defined by this entity type plus all
   * attributes defined by parent entities.
   */
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeIDMap();


}
