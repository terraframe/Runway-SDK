package com.runwaysdk.dataaccess.metadata.graph;

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

public abstract class MdGraphClassDAO extends MdClassDAO implements MdGraphClassDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -2511111344204829156L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdGraphClassDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdGraphClassDAO} from the given hashtable of
   * Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   */
  public MdGraphClassDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see MdGraphClassDAOIF#getRootMdClassDAO()
   */
  @Override
  public MdGraphClassDAOIF getRootMdClassDAO()
  {
    return (MdGraphClassDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdGraphClassDAOIF#getSubClasses()
   */
  @Override
  public abstract List<? extends MdGraphClassDAOIF> getSubClasses();

  /**
   * @see MdGraphClassDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdGraphClassDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdGraphClassDAOIF#getAllSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getAllSubClasses()
  {
    return (List<? extends MdVertexDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdGraphClassDAOIF#getSuperClass()
   */
  public abstract MdGraphClassDAOIF getSuperClass();

  /**
   * @see MdGraphClassDAOIF#getSuperClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGraphClassDAOIF> getSuperClasses()
  {
    return (List<MdGraphClassDAOIF>) super.getSuperClasses();
  }

  /**
   * Returns a list of <code>MdAttributeDAOIF</code> objects that this
   * <code>MdClassDAOIF</code> defines.
   * 
   * @return an List of <code>MdAttributeDAOIF</code> objects that this
   *         <code>MdClassDAOIF</code> defines.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeConcreteDAOIF> definesAttributes()
  {
    return (List<? extends MdAttributeConcreteDAOIF>) super.definesAttributes();
  }

  /**
   * Returns an {@link MdGraphClassDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> graphClassType != null <br/>
   * <b>Precondition:</b> !graphClassType.trim().equals("") <br/>
   * <b>Precondition:</b> graphClassType is a valid class defined in the
   * database <br/>
   * <b>Postcondition:</b> Returns a {@link MdGraphClassDAOIF} instance of the
   * metadata for the given class
   * (MdGraphClassDAOIF().definesType().equals(graphClassType)
   * 
   * @param graphClassType
   * @return {@link MdGraphClassDAOIF} instance of the metadata for the given
   *         type.
   */
  public static MdGraphClassDAOIF getMdGraphClassDAO(String transientType)
  {
    return ObjectCache.getMdGraphClassDAO(transientType);
  }

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    if (this.isNew() && !applied)
    {
      // Supply a class name for the grpah database if one was not provided
      Attribute dbClassNameAttr = this.getAttribute(MdGraphClassInfo.DB_CLASS_NAME);
      if (!dbClassNameAttr.isModified() || dbClassNameAttr.getValue().trim().length() == 0)
      {
        // Create a dbClassName
        String dbClassName = MdTypeDAO.createTableName(MetadataDAO.convertCamelCaseToUnderscore(this.getTypeName()));
        dbClassNameAttr.setValue(dbClassName);
      }
      else
      {
        dbClassNameAttr.setValue(dbClassNameAttr.getValue().toLowerCase());
      }
    }

    String oid = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      this.createClassInDB();

      // Define default attributes.
      if (this.isRootOfHierarchy() && !this.isImport())
      {
        this.createDefaultAttributes();
      }
    }

    return oid;
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
    // Delete subclasses
    this.deleteAllChildClasses(businessContext);

    // Delete all MdMethods defined by this type
    this.dropAllMdMethods();

    // Delete all attribute MdAttribue objects that this type defines
    // delete all attribute metadata for this class
    this.dropAllAttributes(businessContext);

    // Delete all permission tuples that this class participates in
    this.dropTuples();

    // Delete this BusinessDAO
    super.delete(businessContext);

    this.deleteClassInDB();
  }

  protected void createDefaultAttributes()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ComponentInfo.CLASS);

    MdAttributeDAO oidMdAttribute = (MdAttributeDAO) mdBusinessIF.definesAttribute(ComponentInfo.OID).getBusinessDAO();

    this.copyAttribute(oidMdAttribute);
  }

  /**
   * Creates the class in the graph database.
   */
  protected abstract void createClassInDB();

  /**
   * Deletes the class from the graph database.
   */
  protected abstract void deleteClassInDB();

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdGraphClassDAO getBusinessDAO()
  {
    return (MdGraphClassDAO) super.getBusinessDAO();
  }

  @Override
  public String getDBClassName()
  {
    return this.getAttribute(MdGraphClassInfo.DB_CLASS_NAME).getValue();
  }

  /*
   * @Override public List<GeneratorIF> getGenerators() { // TODO Auto-generated
   * method stub return null; }
   * 
   * 
   * @Override public boolean isAbstract() { // TODO Auto-generated method stub
   * return false; }
   * 
   * @Override public boolean isExtendable() { // TODO Auto-generated method
   * stub return false; }
   * 
   * 
   * @Override public boolean isRootOfHierarchy() { // TODO Auto-generated
   * method stub return false; }
   * 
   * @Override public Command getCreateUpdateJavaArtifactCommand(Connection
   * conn) { // TODO Auto-generated method stub return null; }
   * 
   * @Override public Command getDeleteJavaArtifactCommand(Connection conn) { //
   * TODO Auto-generated method stub return null; }
   * 
   * @Override public Command getCleanJavaArtifactCommand(Connection conn) { //
   * TODO Auto-generated method stub return null; }
   */
}
