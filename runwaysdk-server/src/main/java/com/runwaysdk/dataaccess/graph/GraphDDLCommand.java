package com.runwaysdk.dataaccess.graph;

import com.runwaysdk.dataaccess.Command;

public class GraphDDLCommand implements Command
{
  /**
   * <code><b>true</b></code> if this DDLCommand is a drop and the drop should happen at the end of the transaction,.
   * <code><b>false</b></code> if the drop should happen immediately.
   */
  private boolean               dropOnEndOfTransaction;
  
  private GraphDDLCommandAction doItAction;
  
  private GraphDDLCommandAction undoItAction;
  
  private GraphDDLCommandAction finallyAction;
  
  /**
   * @param doItAction
   * @param undoItAction
   * @param finallyAction
   * @param dropOnEndOfTransaction true if this action should occur at the end of the transaction, false otherwise
   */
  public GraphDDLCommand(GraphDDLCommandAction doItAction, GraphDDLCommandAction undoItAction, boolean dropOnEndOfTransaction)
  {    
    this.init(doItAction, undoItAction, null, dropOnEndOfTransaction);
  }
  
  /**
   * @param doItAction
   * @param undoItAction
   * @param finallyAction
   * @param dropOnEndOfTransaction true if this action should occur at the end of the transaction, false otherwise
   */
  public GraphDDLCommand(GraphDDLCommandAction doItAction, GraphDDLCommandAction undoItAction, GraphDDLCommandAction finallyAction, boolean dropOnEndOfTransaction)
  {    
    this.init(doItAction, undoItAction, finallyAction, dropOnEndOfTransaction);
  }

  private void init(GraphDDLCommandAction doItAction, GraphDDLCommandAction undoItAction,
      GraphDDLCommandAction finallyAction, boolean dropOnEndOfTransaction)
  {
    this.doItAction = doItAction;
    this.undoItAction = undoItAction;
    this.finallyAction = finallyAction;
    
    this.dropOnEndOfTransaction = dropOnEndOfTransaction;
  }
  
  @Override
  public void doIt()
  {
    doItAction.execute();
  }

  @Override
  public void undoIt()
  {
    undoItAction.execute();
  }

  @Override
  public void doFinally()
  {
    if (finallyAction != null)
    {
      finallyAction.execute();
    }
  }

  @Override
  public String doItString()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String undoItString()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Indicates if this DDLCommand removes data from the database.
   * @return <code><b>false</b></code> if this DDLCommand is a drop or a delete
   */
  @Override
  public boolean isUndoable()
  {
    return !dropOnEndOfTransaction;
  }
}
