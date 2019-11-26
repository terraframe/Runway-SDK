package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBDeleteClassAction extends OrientDBDDLAction
{
  private String  className;

  private boolean includeSequence;

  public OrientDBDeleteClassAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, boolean includeSequence)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.includeSequence = includeSequence;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    db.getMetadata().getSchema().dropClass(className);

    if (includeSequence)
    {
      OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
      sequenceLibrary.dropSequence(className + "Seq");
    }
  }
}
