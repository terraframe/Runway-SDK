package com.runwaysdk.dataaccess.graph;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class VertexObjectDAO extends GraphObjectDAO
{
  /**
   * 
   */
  private static final long serialVersionUID = -7425379312301977450L;

  /**
   * The default constructor, does not set any attributes
   */
  public VertexObjectDAO()
  {
    super();
  }

  /**
   * Constructs a {@link VertexObjectDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public VertexObjectDAO(Map<String, Attribute> attributeMap, MdVertexDAOIF mdVertexDAOIF)
  {
    super(attributeMap, mdVertexDAOIF);
  }
  
  /**
   * Returns a {@link MdVertexDAOIF}  that defines this Component's class.
   *
   * <br/><b>Precondition:</b> true <br/><b>Postcondition:</b> true
   *
   * @return a {@link MdVertexDAOIF}   that defines this Component's class.
   */
  @Override
  public MdVertexDAOIF getMdClassDAO()
  {
    return (MdVertexDAOIF)super.getMdClassDAO();
  }
  
  public static VertexObjectDAO newInstance(String vertexType)
  {
    // get the meta data for the given class
    MdVertexDAOIF mdVertexDAOIF = MdVertexDAO.getMdVertexDAO(vertexType);
    
    return newInstance(mdVertexDAOIF);
  }
  
  /**
   * Returns a new {@link VertexObjectDAO} instance of the given class name. Default values
   * are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param mdVertexDAOIF
   *          Class name of the new BusinessDAO to instantiate
   * @return new  {@link VertexObjectDAO} of the given type
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static VertexObjectDAO newInstance(MdVertexDAOIF mdVertexDAOIF)
  {
    if (mdVertexDAOIF.isAbstract())
    {
      String errMsg = "Class [" + mdVertexDAOIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(errMsg, mdVertexDAOIF);
    }
    
    Hashtable<String, Attribute> attributeMap = new Hashtable<String, Attribute>();
    
    // get list of all classes in inheritance relationship
    List<MdVertexDAOIF> superMdVertexList = mdVertexDAOIF.getSuperClasses();
    superMdVertexList.forEach(md -> attributeMap.putAll(GraphObjectDAO.createRecordsForEntity(md)));
    
    VertexObjectDAO vertexObjectDAO = new VertexObjectDAO(attributeMap, mdVertexDAOIF);
    
    attributeMap.values().forEach(a -> a.setContainingComponent(vertexObjectDAO));
    
    vertexObjectDAO.setIsNew(true);
    
    return vertexObjectDAO;
  }
  
}
