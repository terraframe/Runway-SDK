package com.runwaysdk.business.graph.generation;

import com.runwaysdk.business.generation.ClassStubGenerator;
import com.runwaysdk.business.generation.StubMarker;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;

public abstract class GraphObjectStubGenerator extends ClassStubGenerator implements StubMarker
{
  protected GraphObjectStubGenerator(MdGraphClassDAOIF mdEntityIF)
  {
    super(mdEntityIF);
  }

  @Override
  protected MdGraphClassDAOIF getMdTypeDAOIF()
  {
    return (MdGraphClassDAOIF) super.getMdTypeDAOIF();
  }
}
