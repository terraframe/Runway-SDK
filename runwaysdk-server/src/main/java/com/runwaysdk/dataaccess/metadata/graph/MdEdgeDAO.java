package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class MdEdgeDAO extends MdGraphClassDAO implements MdEdgeDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 6394194987307320439L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdEdgeDAO()
  {
    super();
  }
  
  /**
   * Constructs a {@link MdEdgeDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdEdgeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdEdgeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdEdgeDAO(attributeMap, MdEdgeInfo.CLASS);
  }
  
  
  /**
   * Returns a new {@link MdEdgeDAO}. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of {@link MdVertexDAO}.
   */
  public static MdEdgeDAO newInstance()
  {
    return (MdEdgeDAO) BusinessDAO.newInstance(MdEdgeInfo.CLASS);
  }
  
  
  /**
   * Always returns true because Edge classes do not inherit from one another
   * otherwise.
   * 
   * @return true if this class is the root class of a hierarchy, false
   *         otherwise.
   */
  @Override
  public boolean isRootOfHierarchy()
  {
    return true;
  }
  
  /**
   * Returns the {@link MdEdgeDAOIF} that is the root of the hierarchy that this type
   * belongs to. returns a reference to itself if it is the root.
   * 
   * @return {@link MdEdgeDAOIF}  that is the root of the hierarchy that this type belongs
   *         to. returns a reference to itself if it is the root.
   */
  @Override
  public MdEdgeDAOIF getRootMdClassDAO()
  {
    return (MdEdgeDAOIF) super.getRootMdClassDAO();
  }
  
  /**
   * @see MdEdgeDAOIF#getSubClasses() 
   */
  @Override
  public List<MdEdgeDAOIF> getSubClasses()
  {
    return new LinkedList<MdEdgeDAOIF>();
  }
  
  /**
   * @see MdEdgeDAOIF#getAllConcreteSubClasses() 
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdEdgeDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdEdgeDAOIF>) super.getAllConcreteSubClasses();
  }
  
  /**
   * @see MdEdgeDAOIF#getAllSubClasses().
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdEdgeDAOIF> getAllSubClasses()
  {
    return (List<? extends MdEdgeDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdEdgeDAOIF#getSuperClass() 
   */
  public MdEdgeDAOIF getSuperClass()
  {
    return null;
  }
  
  /**
   * @see MdEdgeDAOIF#getSuperClasses() 
   */
  @SuppressWarnings("unchecked")
  public List<MdEdgeDAOIF> getSuperClasses()
  {
    return (List<MdEdgeDAOIF>) super.getSuperClasses();
  }
  
  @Override
  public boolean isAbstract()
  {
    return false;
  }
  
  @Override
  public boolean isExtendable()
  {
    return false;
  }
  
// Nothing special happening here yet.
//  /**
//   *
//   */
//  public String save(boolean validateRequired)
//  { 
//    boolean applied = this.isAppliedToDB();
//
//    String oid = super.save(validateRequired);
//
//
//    return oid;
//  }
   

  @Override
  protected void createClassInDB()
  {
    String edgeClass = this.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();
    
    MdVertexDAOIF parentMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).getValue());
    String parentVertexClass = parentMdVertex.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();
    
    MdVertexDAOIF childMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX).getValue());
    String childVertexClass = childMdVertex.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();
    
    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createEdgeClass(graphRequest, graphDDLRequest, edgeClass, parentVertexClass, childVertexClass);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().deleteEdgeClass(graphRequest, graphDDLRequest, edgeClass);

    GraphDDLCommand command = new GraphDDLCommand(doItAction,  undoItAction, false);
    command.doIt();
  }
  
  @Override
  protected void deleteClassInDB()
  {
    String edgeClass = this.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();

    MdVertexDAOIF paretMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).getValue());
    String parentVertexClass = paretMdVertex.getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();

    MdVertexDAOIF childMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX).getValue());
    String childVertexClass = childMdVertex.getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().deleteEdgeClass(graphRequest, graphDDLRequest, edgeClass);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createEdgeClass(graphRequest, graphDDLRequest, edgeClass, parentVertexClass, childVertexClass);

    GraphDDLCommand command = new GraphDDLCommand(doItAction,  undoItAction, true);
    command.doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdEdgeDAOIF get(String oid)
  {
    return (MdEdgeDAOIF) BusinessDAO.get(oid);
  } 
 
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdEdgeDAO getBusinessDAO()
  {
    return (MdEdgeDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns an {@link MdEdgeDAOIF} instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> edgeType != null <br/>
   * <b>Precondition:</b> !edgeType.trim().equals("") <br/>
   * <b>Precondition:</b> edgeType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdEdgeDAOIF} instance of the metadata for the
   * given class ({@link MdEdgeDAOIF}().definesType().equals(edgeType)
   * 
   * @param edgeType
   * @return {@link MdEdgeDAOIF} instance of the metadata for the given type.
   */
  public static MdEdgeDAOIF getMdEdgeDAO(String edgeType)
  {
    return ObjectCache.getMdEdgeDAO(edgeType);
  }
  
  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }
  
  /**
   * Returns a list of all generators used to generate source for this {@link MdEdgeDAOIF}.
   * 
   * @return list of all generators used to generate source for this {@link MdEdgeDAOIF}.
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
    Command command = null;

//    if (this.isNew())
//    {
//      command = new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE, conn);
//
//    }
//    else
//    {
//      command = new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
//    }

    return command;
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
//    return new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
    return null;
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
//    return new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN, conn);
    
    return null;
  }

}
