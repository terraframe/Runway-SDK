/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.business.graph.EdgeObjectTest;
import com.runwaysdk.business.graph.VertexObjectPermissionTest;
import com.runwaysdk.business.graph.VertexObjectTest;
import com.runwaysdk.business.graph.generation.VertexObjectGeneratorTest;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOTest;
import com.runwaysdk.dataaccess.graph.MdGraphClassChangeOverTimeTest;
import com.runwaysdk.dataaccess.graph.MdGraphClassTest;
import com.runwaysdk.dataaccess.graph.VertexInheritanceDAOTest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOChangeOverTimeTest;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOTest;
import com.runwaysdk.gis.dataaccess.graph.GeoVertexTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  MdGraphClassTest.class, 
  MdGraphClassChangeOverTimeTest.class, 
  GeoVertexTest.class, 
  VertexObjectDAOTest.class,
  VertexObjectDAOChangeOverTimeTest.class,
  VertexInheritanceDAOTest.class,
  EdgeObjectDAOTest.class,
  VertexObjectTest.class,
  EdgeObjectTest.class,
  VertexObjectGeneratorTest.class,
  VertexObjectPermissionTest.class
})
public class GraphTestSuite
{
  // nothing
}