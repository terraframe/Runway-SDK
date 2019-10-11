package com.runwaysdk.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeUUIDDAO;

public class MdAttributeUUID_G extends MdAttributeConcrete_G
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1619277479919028965L;

  /**
   * @param {@link MdAttributeUUIDDAO}
   */
  public MdAttributeUUID_G(MdAttributeUUIDDAO mdAttribute)
  {
    super(mdAttribute);
  }
  
  /**
   * Returns the {@link MdAttributeUUIDDAO}.
   *
   * @return the {@link MdAttributeUUIDDAO}
   */
  protected MdAttributeUUIDDAO getMdAttribute()
  {
    return (MdAttributeUUIDDAO)this.mdAttribute;
  }
  
  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeUUIDInfo.COLUMN_NAME).getValue();
    boolean required = true; 
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);
    
    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, false);
    graphCommand.doIt();
  }

  @Override
  protected void dropDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeUUIDInfo.COLUMN_NAME).getValue();
    boolean required = true; 
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();
    
    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName);    
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength);
    
    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();  
  }

}
