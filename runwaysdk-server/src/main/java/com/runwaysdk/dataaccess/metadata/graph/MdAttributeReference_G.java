package com.runwaysdk.dataaccess.metadata.graph;

import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;

public class MdAttributeReference_G extends MdAttributeConcrete_G
{
  /**
   * 
   */
  private static final long serialVersionUID = -6108473202975565175L;

  /**
   * @param {@link MdAttributeReferenceDAO}
   */
  public MdAttributeReference_G(MdAttributeReferenceDAO mdAttribute)
  {
    super(mdAttribute);
  }

  /**
   * Returns the {@link MdAttributeReferenceDAO}.
   *
   * @return the {@link MdAttributeReferenceDAO}
   */
  protected MdAttributeReferenceDAO getMdAttribute()
  {
    return (MdAttributeReferenceDAO)this.mdAttribute;
  }
  
  
  /**
   * Adds the attribute to the graph database
   *
   */
  @Override
  protected void createDbAttribute()
  {
    String dbClassName = this.definedByClass().getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeReferenceInfo.COLUMN_NAME).getValue();
    boolean required = ((AttributeBooleanIF)this.getMdAttribute().getAttributeIF(MdAttributeReferenceInfo.REQUIRED)).getBooleanValue(); 
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime());
    
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
    String dbAttrName = this.getMdAttribute().getAttributeIF(MdAttributeReferenceInfo.COLUMN_NAME).getValue();
    boolean required = ((AttributeBooleanIF)this.getMdAttribute().getAttributeIF(MdAttributeReferenceInfo.REQUIRED)).getBooleanValue(); 
    int maxLength = MdAttributeUUIDInfo.UUID_STRING_LENGTH;
    
    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();
    
    GraphDDLCommandAction doItAction = GraphDBService.getInstance().dropAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, this.isChangeOverTime());    
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createCharacterAttribute(graphRequest, graphDDLRequest, dbClassName, dbAttrName, required, maxLength, this.isChangeOverTime());
    
    GraphDDLCommand graphCommand = new GraphDDLCommand(doItAction, undoItAction, true);
    graphCommand.doIt();
  }
}
