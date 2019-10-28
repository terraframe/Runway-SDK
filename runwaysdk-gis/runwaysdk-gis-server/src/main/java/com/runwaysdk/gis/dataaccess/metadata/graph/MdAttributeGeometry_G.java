package com.runwaysdk.gis.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.graph.MdAttributeConcrete_G;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeGeometryDAO;

public class MdAttributeGeometry_G extends MdAttributeConcrete_G
{
  /**
   * 
   */
  private static final long serialVersionUID = -6108473202975565175L;

  /**
   * @param {@link MdAttributeGeometryDAO}
   */
  public MdAttributeGeometry_G(MdAttributeGeometryDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the {@link MdAttributeGeometryDAO}.
   *
   * @return the {@link MdAttributeGeometryDAO}
   */
  protected MdAttributeGeometryDAO getMdAttribute()
  {
    return (MdAttributeGeometryDAO)this.mdAttribute;
  }

  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.dbColumnType, required);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);

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
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.COLUMN_NAME).getValue();
    boolean required = ( (AttributeBooleanIF) this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.REQUIRED) ).getBooleanValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createGeometryAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.dbColumnType, required);

    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();
  }
}
