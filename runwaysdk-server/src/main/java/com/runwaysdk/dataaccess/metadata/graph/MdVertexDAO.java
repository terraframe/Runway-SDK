package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.graph.generation.JavaArtifactMdGraphClassCommand;
import com.runwaysdk.business.graph.generation.VertexObjectBaseGenerator;
import com.runwaysdk.business.graph.generation.VertexObjectStubGenerator;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdTransientInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.graph.MdGraphClassInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class MdVertexDAO extends MdGraphClassDAO implements MdVertexDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 4801188807525986815L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdVertexDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdVertexDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdVertexDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdVertexDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdVertexDAO(attributeMap, MdVertexInfo.CLASS);
  }

  /**
   * Returns a new {@link MdVertexDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of {@link MdVertexDAO}.
   */
  public static MdVertexDAO newInstance()
  {
    return (MdVertexDAO) BusinessDAO.newInstance(MdVertexInfo.CLASS);
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false
   * otherwise.
   * 
   * @return true if this class is the root class of a hierarchy, false
   *         otherwise.
   */
  @Override
  public boolean isRootOfHierarchy()
  {
    String superMdViewId = this.getAttributeIF(MdVertexInfo.SUPER_MD_VERTEX).getValue();
    if (superMdViewId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the {@link MdVertexDAOIF} that is the root of the hierarchy that
   * this type belongs to. returns a reference to itself if it is the root.
   * 
   * @return {@link MdVertexDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  @Override
  public MdVertexDAOIF getRootMdClassDAO()
  {
    return (MdVertexDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdVertexDAOIF#getSubClasses()
   */
  @Override
  public List<MdVertexDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.VERTEX_INHERITANCE.getType());

    List<MdVertexDAOIF> mdViewList = new LinkedList<MdVertexDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdViewList.add((MdVertexDAOIF) relationship.getChild());
    }

    return mdViewList;
  }

  /**
   * @see MdVertexDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdVertexDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdVertexDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdVertexDAOIF#getAllSubClasses().
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdVertexDAOIF> getAllSubClasses()
  {
    return (List<? extends MdVertexDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdVertexDAOIF#getSuperClass()
   */
  public MdVertexDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdVertexId = this.getAttributeIF(MdVertexInfo.SUPER_MD_VERTEX).getValue();
      return MdVertexDAO.get(superMdVertexId);
    }
    else
    {
      return null;
    }
  }

  /**
   * @see MdVertexDAOIF#getSuperClasses()
   */
  @SuppressWarnings("unchecked")
  public List<MdVertexDAOIF> getSuperClasses()
  {
    return (List<MdVertexDAOIF>) super.getSuperClasses();
  }

  @Override
  public boolean isAbstract()
  {
    AttributeBooleanIF attributeBoolean = (AttributeBooleanIF) this.getAttributeIF(MdTransientInfo.ABSTRACT);
    return AttributeBoolean.getBooleanValue(attributeBoolean);
  }

  /**
   * Returns true if the type can be extended, false otherwise.
   *
   * @return true if the type can be extended, false otherwise.
   */
  @Override
  public boolean isExtendable()
  {
    return true;
  }

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    String oid = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      // Inheritance relationship will be imported if this object is imported
      if (this.createInheritanceRelationship() && !this.isImport())
      {
        // extend the new class with the given super class
        MdVertexDAO superMdVertex = this.getSuperClass().getBusinessDAO();
        superMdVertex.addSubMdVertex(this);
      }

    }
    // !this.isNew() || applied
    else if (this.getAttribute(EntityInfo.KEY).isModified())
    {
      this.updateInheritanceRelationshipKey();
    }

    return oid;
  }

  /**
   * Deletes all child classes.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   */
  protected void deleteAllChildClasses(boolean businessContext)
  {
    // delete all child classes
    for (MdVertexDAOIF subMdVertexDAOIF : getSubClasses())
    {
      if (!subMdVertexDAOIF.definesType().equals(this.definesType()))
      {
        MdVertexDAO subMdBusinessDAO = subMdVertexDAOIF.getBusinessDAO();
        subMdBusinessDAO.delete(businessContext);
      }
    }
  }

  /**
   * 
   */
  @Override
  protected void createClassInDB()
  {
    String dbClassName = this.getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createVertexClass(graphRequest, graphDDLRequest, dbClassName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().deleteVertexClass(graphRequest, graphDDLRequest, dbClassName);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, false);
    command.doIt();
  }

  protected void deleteClassInDB()
  {
    String dbClassName = this.getAttributeIF(MdGraphClassInfo.DB_CLASS_NAME).getValue();
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().deleteVertexClass(graphRequest, graphDDLRequest, dbClassName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createVertexClass(graphRequest, graphDDLRequest, dbClassName);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, true);
    command.doIt();
  }

  /**
   * Creates the relationship such that this class becomes superclass of the
   * given class.
   * 
   * @param childMdVertexDAOIF
   *          to become a subclass of this class.
   */
  protected void addSubMdVertex(MdVertexDAOIF childMdVertexDAOIF)
  {
    RelationshipDAO newChildRelDAO = this.addChild(childMdVertexDAOIF, RelationshipTypes.VERTEX_INHERITANCE.getType());
    newChildRelDAO.setKey(childMdVertexDAOIF.getKey());
    newChildRelDAO.save(true);
  }

  /**
   * Updates the key on the inheritance relationship.
   * 
   * <br/>
   * <b>Precondition:</b>the key has been modified
   */
  protected void updateInheritanceRelationshipKey()
  {
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.VERTEX_INHERITANCE.getType());

    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdVertexDAOIF get(String oid)
  {
    return (MdVertexDAOIF) BusinessDAO.get(oid);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdVertexDAO getBusinessDAO()
  {
    return (MdVertexDAO) super.getBusinessDAO();
  }

  /**
   * Returns an {@link MdVertexDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> vertexType != null <br/>
   * <b>Precondition:</b> !vertexType.trim().equals("") <br/>
   * <b>Precondition:</b> vertexType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdVertexDAOIF} instance of the
   * metadata for the given class
   * ({@link MdVertexDAOIF}().definesType().equals(vertexType)
   * 
   * @param vertexType
   * @return {@link MdVertexDAOIF} instance of the metadata for the given type.
   */
  public static MdVertexDAOIF getMdVertexDAO(String vertexType)
  {
    return ObjectCache.getMdVertexDAO(vertexType);
  }

  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }

  /**
   * Returns a list of all generators used to generate source for this
   * {@link MdVertexDAOIF}.
   * 
   * @return list of all generators used to generate source for this
   *         {@link MdVertexDAOIF}.
   */
  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Don't generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

    list.add(new VertexObjectBaseGenerator(this));
    list.add(new VertexObjectStubGenerator(this));

    return list;
  }

  /**
   * Returns a command object that either creates or updates Java artifacts for
   * this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that either creates or updates Java artifacts for
   *         this type.
   */
  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    if (this.isNew())
    {
      return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE);
    }

    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE);
  }

  /**
   * Returns a command object that deletes Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that deletes Java artifacts for this type.
   */
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE);
  }

  /**
   * Returns a command object that cleans Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that cleans Java artifacts for this type.
   */
  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN);
  }

  @Override
  public List<MdEdgeDAOIF> getChildMdEdges()
  {
    List<MdEdgeDAOIF> mdRelationships = new LinkedList<MdEdgeDAOIF>();

    for (String mdRelationshipDAOid : ObjectCache.getChildMdEdgeDAOids(this.getOid()))
    {
      mdRelationships.add(MdEdgeDAO.get(mdRelationshipDAOid));
    }

    Collections.sort(mdRelationships, new Comparator<MdEdgeDAOIF>()
    {
      public int compare(MdEdgeDAOIF o1, MdEdgeDAOIF o2)
      {
        return o1.getTypeName().compareTo(o2.getTypeName());
      }
    });

    return mdRelationships;
  }

  @Override
  public List<MdEdgeDAOIF> getParentMdEdges()
  {
    List<MdEdgeDAOIF> mdRelationships = new LinkedList<MdEdgeDAOIF>();

    for (String mdRelationshipDAOid : ObjectCache.getParentMdEdgeDAOids(this.getOid()))
    {
      mdRelationships.add(MdEdgeDAO.get(mdRelationshipDAOid));
    }

    Collections.sort(mdRelationships, new Comparator<MdEdgeDAOIF>()
    {
      public int compare(MdEdgeDAOIF o1, MdEdgeDAOIF o2)
      {
        return o1.getTypeName().compareTo(o2.getTypeName());
      }
    });

    return mdRelationships;
  }

}
