package com.runwaysdk.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;

public class MdAttributeEmbedded_G extends MdAttributeConcrete_G
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -8831375476104335207L;

  /**
   * Precondition: 
   * 
   * @param {@link MdAttributeEmbedded_G}
   */
  public MdAttributeEmbedded_G(MdAttributeEmbeddedDAO mdAttribute)
  {
    super(mdAttribute);
  }
  
  /**
   * Returns the {@link MdGraphClassDAOIF} that defines this MdAttribute.
   *
   * @return the {@link MdGraphClassDAOIF} that defines this MdAttribute.
   */
  public MdGraphClassDAOIF definedByClass()
  {
    return (MdGraphClassDAOIF) this.getMdAttribute().definedByClass();
  }

  /**
   * Returns the MdAttribute.
   * 
   * @return the MdAttribute
   */
  protected MdAttributeEmbeddedDAO getMdAttribute()
  {
    return (MdAttributeEmbeddedDAO) super.getMdAttribute();
  }
  
  
  protected String getEmbeddedClassType()
  {
    return this.dbColumnType;
  }
  
  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String embeddedClassType = this.getEmbeddedClassType();
 
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createEmbeddedAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, embeddedClassType, required);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }
  
  /**
   * Drops the attribute from the graph database
   *
   */
  @Override
  protected void dropDbAttribute()
  {
    String embeddedClassType = this.getEmbeddedClassType();
    
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createEmbeddedAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, embeddedClassType, required);

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();
  }
}
