package com.runwaysdk.dataaccess.graph;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class MdGraphClassChangeOverTimeTest extends MdGraphClassTest
{
  protected MdVertexDAO createVertexClass(String vertexName)
  {
    MdVertexDAO mdVertexDAO = TestFixtureFactory.createMdVertex(vertexName);
    mdVertexDAO.setValue(MdVertexInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdVertexDAO.setValue(MdVertexInfo.ENABLE_CHANGE_OVER_TIME, MdAttributeBooleanInfo.TRUE);
    mdVertexDAO.apply();

    return mdVertexDAO;
  }

}
