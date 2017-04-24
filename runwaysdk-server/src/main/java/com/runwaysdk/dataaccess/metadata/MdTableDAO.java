package com.runwaysdk.dataaccess.metadata;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTableDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.transaction.LockObject;

public class MdTableDAO extends MdClassDAO implements MdTableDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2492504220203490692L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdTableDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.metadata.MdTypeDAO
   */
  public MdTableDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }
  
  /**
   * Returns a new <code>MdTableDAO</code>. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of <code>MdTableDAO</code>.
   */
  public static MdTableDAO newInstance()
  {
    return (MdTableDAO) BusinessDAO.newInstance(MdTableInfo.CLASS);
  }

  /**
   * Creates an empty BusinessDAO. For subclasses creates a subtype based on the
   * classType, and fills the attributes with the attribute map
   * 
   * @param attributeMap
   *          The attribute mappings of the class
   * @return The new class created
   */
  public MdTableDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdTableDAO(attributeMap, classType);
  }
  
  public String apply()
  {
    // Set the table name to lower case. 
    String tableName = this.getAttribute(MdTableInfo.TABLE_NAME).getValue();
    this.getAttribute(MdTableInfo.TABLE_NAME).setValue(tableName.toLowerCase());
    this.getAttribute(MdTableInfo.GENERATE_SOURCE).setValue(MdAttributeBooleanInfo.FALSE);
    
    return super.apply();
  }
  
  
  /**
   * No code is generated for instances of {@link MdTableDAO}.
   */
  @Override
  public List<GeneratorIF> getGenerators()
  {
    return new LinkedList<GeneratorIF>();
  }

  /**
   * Currently {@link MdTableDAO}s cannot be abstract.
   */
  @Override
  public boolean isAbstract()
  {
    return false;
  }

  /**
   * Currently {@link MdTableDAO}s cannot be extended.
   */
  @Override
  public boolean isExtendable()
  {
    return false;
  }

  /**
   * {@link com.runwaysdk.dataaccess.MdTableDAOIF#getSubClasses()}
   */
  @Override
  public List<? extends MdTableDAOIF> getSubClasses()
  {
    return new LinkedList<MdTableDAOIF>();
  }

  /**
   * {@link com.runwaysdk.dataaccess.MdTableDAOIF#getSuperClass()}
   */
  @Override
  public MdTableDAOIF getSuperClass()
  {
    return null;
  }

  /**
   * Currently {@link MdTableDAO}s cannot be extended so this is the root of the hierarchy.
   */
  @Override
  public boolean isRootOfHierarchy()
  {
    return true;
  }

  /**
   * No code is generated for instances of {@link MdTableDAO}.
   */
  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    return null;
  }

  /**
   * No code is generated for instances of {@link MdTableDAO}.
   */
  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return null;
  }

  /**
   * No code is generated for instances of {@link MdTableDAO}.
   */
  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return null;
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdTableDAOIF get(String id)
  {
    return (MdTableDAOIF) BusinessDAO.get(id);
  }
  
  /**
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  @Override
  public void delete(boolean businessContext)
  {
    this.deleteAllChildClasses(businessContext);
    
    // Delete all attribute MdAttribue objects that this type defines
    // delete all attribute metadata for this class
    this.dropAllAttributes(businessContext);
    
    // Delete all permission tuples that this class participates in
    this.dropTuples();
    
    // Delete all MdMethods defined by this type
    this.dropAllMdMethods();
    
    // Delete this BusinessDAO
    super.delete(businessContext);
  }
  
  /**
   * Returns a {@link MdTableDAOIF}  instance of the metadata for the given class.
   * 
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("") <br/>
   * <b>Precondition:</b> classType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> return value is not null <br/>
   * <b>Postcondition:</b> Returns a MdBusinessIF instance of the metadata for
   * the given class (MdTableDAOIF().definesType().equals(classType)
   * 
   * @param classType
   *          class type
   * @return MdBusinessIF instance of the metadata for the given class type.
   */
  public static MdTableDAOIF getMdTableDAO(String classType)
  {
    return (MdTableDAOIF)ObjectCache.getMdClassDAO(classType);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTableDAO getBusinessDAO()
  {
    return (MdTableDAO) super.getBusinessDAO();
  }
}
